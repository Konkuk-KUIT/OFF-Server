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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private static final DateTimeFormatter END_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final ProjectRepository projectRepository;
    private final PartnerRecruitRepository partnerRecruitRepository;
    private final MemberRepository memberRepository;
    private final GeminiService geminiService;

    @Transactional
    public CreateProjectResponse createProject(Long memberId, CreateProjectRequest request) {
        Member creator = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        ProjectType projectType = parseProjectType(request.getProjectTypeId());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);

        Project project = new Project(
                request.getName(),
                request.getDescription(),
                request.getRequirement(),
                0L,
                startDate,
                endDate,
                projectType,
                creator
        );
        projectRepository.save(project);

        List<String> recruitmentRoles = new ArrayList<>();
        List<CreateProjectResponse.EstimateResponse> estimateList = new ArrayList<>();

        for (CreateProjectRequest.RecruitmentRequest recruitment : request.getRecruitmentList()) {
            Role role = parseRole(recruitment.getRoleId());
            recruitmentRoles.add(role.name());

            PartnerRecruit partnerRecruit = new PartnerRecruit(project, role, recruitment.getCount(), RecruitStatus.OPEN);
            partnerRecruitRepository.save(partnerRecruit);

            List<Member> candidateMembers = memberRepository.findAllByRole(role);

            int costPerPerson = estimateCostPerRole(role, request.getDescription(), request.getRequirement());

            estimateList.add(CreateProjectResponse.EstimateResponse.of(
                    role.name(), costPerPerson, recruitment.getCount(), candidateMembers
            ));
        }

        int totalEstimate = estimateList.stream()
                .mapToInt(e -> e.getCost() * e.getCount())
                .sum();

        String serviceSummary = generateServiceSummary(request.getDescription(), request.getRequirement());

        return new CreateProjectResponse(
                projectType.getDisplayName(),
                recruitmentRoles,
                endDate.format(END_DATE_FORMATTER),
                serviceSummary,
                totalEstimate,
                estimateList
        );
    }

    private ProjectType parseProjectType(Long projectTypeId) {
        try {
            return ProjectType.fromId(projectTypeId);
        } catch (IllegalArgumentException e) {
            throw new OffException(ResponseCode.INVALID_PROJECT_TYPE);
        }
    }

    private Role parseRole(String roleId) {
        try {
            return Role.valueOf(roleId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OffException(ResponseCode.INVALID_ROLE);
        }
    }

    private String generateServiceSummary(String description, String requirement) {
        String prompt = """
                당신은 IT 프로젝트 전문 PM입니다.
                사용자가 입력한 `서비스 설명`과 `요구사항`을 분석하여 다음 두 가지 섹션으로 구성된 상세 기획안을 작성해주세요.

                **Section 1. 서비스 상세 정의**
                - 이 서비스가 무엇인지, 주요 타겟과 핵심 가치가 무엇인지 명확하게 요약 설명.

                **Section 2. 단계별 프로젝트 실행 계획**
                - 프로젝트를 완성하기 위한 과정을 **[기획 - 디자인 - 개발 - 테스트/배포]** 등의 단계(Phase)로 나누세요.
                - 각 단계별로 수행해야 할 **핵심 Task**와 그에 따른 **세부 할일(Sub-task)**을 구체적으로 나열하세요.
                - 개발 단계에서는 기술적인 내용(DB설계, API구현 등)을 포함하세요.

                **출력 형식:**
                - 마크다운(Markdown) 형식을 사용하여 가독성 좋게 출력할 것.
                - 불필요한 서론/결론 없이 본론 내용만 출력할 것.

                ---
                [서비스 설명]
                %s

                [요구사항]
                %s
                """.formatted(description, requirement);

        try {
            return geminiService.generateText(prompt);
        } catch (Exception e) {
            log.error("Gemini 서비스 요약 생성 실패: {}", e.getMessage(), e);
            return DEFAULT_SERVICE_SUMMARY_TEMPLATE;
        }
    }

    private int estimateCostPerRole(Role role, String description, String requirement) {
        String prompt = """
                당신은 IT 프로젝트 비용 산정 전문가입니다.
                아래 프로젝트 정보를 바탕으로 '%s' 직무의 1인당 월 예상 비용(만원 단위)을 숫자만 답해주세요.
                예시: 500

                [서비스 설명]
                %s

                [요구사항]
                %s
                """.formatted(role.getValue(), description, requirement);

        try {
            String result = geminiService.generateText(prompt);
            return Integer.parseInt(result.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            log.error("Gemini 비용 산정 실패 ({}): {}", role.name(), e.getMessage(), e);
            return 0;
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
