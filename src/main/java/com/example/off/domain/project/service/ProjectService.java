package com.example.off.domain.project.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.gemini.GeminiService;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.partnerRecruit.PartnerRecruit;
import com.example.off.domain.partnerRecruit.RecruitStatus;
import com.example.off.domain.partnerRecruit.repository.PartnerRecruitRepository;
import com.example.off.domain.project.Project;
import com.example.off.domain.project.ProjectType;
import com.example.off.domain.project.dto.CreateProjectRequest;
import com.example.off.domain.project.dto.CreateProjectResponse;
import com.example.off.domain.project.repository.ProjectRepository;
import com.example.off.domain.role.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private static final DateTimeFormatter END_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final ProjectRepository projectRepository;
    private final PartnerRecruitRepository partnerRecruitRepository;
    private final MemberRepository memberRepository;
    private final GeminiService geminiService;
    private final PlatformTransactionManager transactionManager;
    private final ObjectMapper objectMapper;

    public CreateProjectResponse createProject(Long memberId, CreateProjectRequest request) {
        // 1. 검증 및 데이터 조회
        Member creator = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        ProjectType projectType = parseProjectType(request.getProjectTypeId());

        List<RecruitmentInfo> recruitments = new ArrayList<>();
        for (CreateProjectRequest.RecruitmentRequest r : request.getRecruitmentList()) {
            Role role = parseRole(r.getRoleId());
            List<Member> candidates = memberRepository.findAllByRole(role);
            recruitments.add(new RecruitmentInfo(role, r.getCount(), candidates));
        }

        // 2. 외부 API 호출 1회 (트랜잭션 밖 — DB 커넥션 점유 없음)
        LocalDate startDate = LocalDate.now();
        List<String> roleNames = recruitments.stream()
                .map(r -> r.role.name())
                .collect(Collectors.toList());

        GeminiEstimation estimation = getProjectEstimation(
                request.getDescription(), request.getRequirement(), roleNames, startDate);

        String serviceSummary = estimation.serviceSummary();
        LocalDate endDate = startDate.plusDays(estimation.estimatedDays());
        for (RecruitmentInfo info : recruitments) {
            info.cost = estimation.costs().getOrDefault(info.role.name(), 0);
        }

        // 3. 견적 계산
        int totalEstimate = recruitments.stream()
                .mapToInt(r -> r.cost * r.count)
                .sum();

        // 4. DB 저장 (짧은 트랜잭션)
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            Project project = new Project(
                    request.getName(),
                    request.getDescription(),
                    request.getRequirement(),
                    (long) totalEstimate,
                    startDate,
                    endDate,
                    projectType,
                    creator);
            projectRepository.save(project);

            for (RecruitmentInfo info : recruitments) {
                partnerRecruitRepository.save(
                        new PartnerRecruit(project, info.role, info.count, RecruitStatus.OPEN));
            }
        });

        // 5. 응답 구성
        List<String> recruitmentRoles = recruitments.stream()
                .map(r -> r.role.name())
                .toList();

        List<CreateProjectResponse.EstimateResponse> estimateList = recruitments.stream()
                .map(r -> CreateProjectResponse.EstimateResponse.of(
                        r.role.name(), r.cost, r.count, r.candidates))
                .toList();

        return new CreateProjectResponse(
                projectType.getDisplayName(),
                recruitmentRoles,
                endDate.format(END_DATE_FORMATTER),
                serviceSummary,
                totalEstimate,
                estimateList);
    }

    private ProjectType parseProjectType(Long projectTypeId) {
        try {
            return ProjectType.fromId(projectTypeId);
        } catch (IllegalArgumentException e) {
            throw new OffException(ResponseCode.INVALID_PROJECT_TYPE);
        }
    }

    private Role parseRole(String roleId) {
        return switch (roleId.toLowerCase()) {
            case "planner" -> Role.PM;
            case "developer" -> Role.DEV;
            case "designer" -> Role.DES;
            case "marketer" -> Role.MAR;
            default -> {
                try {
                    yield Role.valueOf(roleId.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new OffException(ResponseCode.INVALID_ROLE);
                }
            }
        };
    }

    private GeminiEstimation getProjectEstimation(String description, String requirement,
                                                   List<String> roleNames, LocalDate startDate) {
        String rolesJson = roleNames.stream()
                .map(r -> "\"" + r + "\": 0")
                .collect(Collectors.joining(", "));

        String prompt = """
                당신은 IT 프로젝트 전문 PM입니다.
                아래 프로젝트 정보를 분석하여, 다음 3가지를 포함하는 JSON 객체를 **하나만** 응답해주세요.

                1. serviceSummary: 서비스 상세 정의와 단계별 프로젝트 실행 계획을 마크다운으로 작성
                   - Section 1: 서비스가 무엇인지, 주요 타겟과 핵심 가치 요약
                   - Section 2: [기획 - 디자인 - 개발 - 테스트/배포] 단계별 핵심 Task와 세부 할일
                2. costs: 각 직군별 1인당 월 예상 비용(만원 단위, 정수)
                3. estimatedDays: 시작일(%s)로부터 프로젝트 완료까지 예상 소요일(정수)

                **반드시 아래 JSON 형식만 출력하세요. 다른 텍스트는 절대 포함하지 마세요.**
                {"serviceSummary": "마크다운 내용", "costs": {%s}, "estimatedDays": 90}

                ---
                [서비스 설명]
                %s

                [요구사항]
                %s
                """.formatted(startDate.format(END_DATE_FORMATTER), rolesJson, description, requirement);

        try {
            String result = geminiService.generateText(prompt);
            return parseEstimation(result);
        } catch (Exception e) {
            log.error("Gemini 통합 견적 요청 실패: {}", e.getMessage(), e);
            return getDefaultEstimation();
        }
    }

    private GeminiEstimation parseEstimation(String raw) {
        try {
            // Gemini가 ```json ... ``` 코드블록으로 감싸는 경우 strip
            String json = raw.strip();
            if (json.startsWith("```")) {
                json = json.replaceFirst("```(?:json)?\\s*", "");
                json = json.replaceFirst("\\s*```$", "");
                json = json.strip();
            }

            JsonNode root = objectMapper.readTree(json);

            // serviceSummary
            String serviceSummary = root.path("serviceSummary").asText(DEFAULT_SERVICE_SUMMARY_TEMPLATE);

            // costs
            Map<String, Integer> costs = new HashMap<>();
            JsonNode costsNode = root.path("costs");
            if (costsNode.isObject()) {
                costsNode.fields().forEachRemaining(entry ->
                        costs.put(entry.getKey(), entry.getValue().asInt(0)));
            }

            // estimatedDays
            int estimatedDays = root.path("estimatedDays").asInt(30);
            if (estimatedDays < 7) {
                estimatedDays = 30;
            }

            return new GeminiEstimation(serviceSummary, costs, estimatedDays);
        } catch (Exception e) {
            log.error("Gemini 응답 JSON 파싱 실패: {}", e.getMessage(), e);
            return getDefaultEstimation();
        }
    }

    private GeminiEstimation getDefaultEstimation() {
        return new GeminiEstimation(DEFAULT_SERVICE_SUMMARY_TEMPLATE, Map.of(), 30);
    }

    private record GeminiEstimation(String serviceSummary, Map<String, Integer> costs, int estimatedDays) {
    }

    private static class RecruitmentInfo {
        final Role role;
        final int count;
        final List<Member> candidates;
        int cost;

        RecruitmentInfo(Role role, int count, List<Member> candidates) {
            this.role = role;
            this.count = count;
            this.candidates = candidates;
        }
    }

    private static final String DEFAULT_SERVICE_SUMMARY_TEMPLATE = """
            > AI 서비스 연결 상태가 원활하지 않아 기본 기획안 템플릿을 제공합니다.

            ## Section 1. 서비스 상세 정의
            - 서비스 개요: (작성 필요)
            - 주요 타겟: (작성 필요)
            - 핵심 가치: (작성 필요)

            ## Section 2. 단계별 프로젝트 실행 계획

            ### Phase 1. 기획
            - [ ] 요구사항 분석 및 정의
            - [ ] 화면 설계(Wireframe) 작성
            - [ ] 기능 명세서 작성

            ### Phase 2. 디자인
            - [ ] UI/UX 디자인
            - [ ] 디자인 시스템 구축
            - [ ] 프로토타입 제작 및 검수

            ### Phase 3. 개발
            - [ ] DB 설계 및 ERD 작성
            - [ ] API 설계 및 구현
            - [ ] 프론트엔드 개발
            - [ ] API 연동 및 통합

            ### Phase 4. 테스트 / 배포
            - [ ] 단위 테스트 및 통합 테스트
            - [ ] QA 및 버그 수정
            - [ ] 배포 환경 구성 및 릴리즈
            """;
}
