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
import com.example.off.domain.project.ProjectStatus;
import com.example.off.domain.project.ProjectType;
import com.example.off.domain.project.dto.*;
import com.example.off.domain.project.repository.ProjectRepository;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.role.Role;
import com.example.off.domain.task.Task;
import com.example.off.domain.task.ToDo;
import com.example.off.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private static final DateTimeFormatter END_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final ProjectRepository projectRepository;
    private final PartnerRecruitRepository partnerRecruitRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final GeminiService geminiService;

    public CreateProjectResponse estimateProject(Long memberId, CreateProjectRequest request) {
        // 1. 검증 및 데이터 조회
        memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        ProjectType projectType = parseProjectType(request.getProjectTypeId());

        List<RecruitmentInfo> recruitments = new ArrayList<>();
        for (CreateProjectRequest.RecruitmentRequest r : request.getRecruitmentList()) {
            Role role = parseRole(r.getRoleId());
            List<Member> candidates = memberRepository.findAllByRole(role);
            recruitments.add(new RecruitmentInfo(role, r.getCount(), candidates));
        }

        // 2. 외부 API 호출 (LLM)
        String serviceSummary = generateServiceSummary(request.getDescription(), request.getRequirement());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = estimateEndDate(startDate, request.getDescription(), request.getRequirement());

        // 전체 일 수
        long days = ChronoUnit.DAYS.between(startDate, endDate);

        // 30일 = 1개월
        double months = days / 30.0;

        for (RecruitmentInfo info : recruitments) {
            info.cost = (int)(estimateCostPerRole(info.role, request.getDescription(), request.getRequirement()) * months);
        }


        // 3. 견적 계산
        int totalEstimate = recruitments.stream()
                .mapToInt(r -> r.cost * r.count)
                .sum();

        // 4. 응답 구성 (DB 저장 없음 — 미리보기용)
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

    @Transactional
    public ConfirmProjectResponse confirmProject(Long memberId, ConfirmProjectRequest request) {
        // 1. 검증
        Member creator = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        ProjectType projectType = parseProjectType(request.getProjectTypeId());

        // 2. 날짜 파싱
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.parse(request.getEndDate(), END_DATE_FORMATTER);

        // 3. 프로젝트 진행 상태 반영
        creator.startWorking();

        // 4. DB 저장 (totalEstimate는 estimateProject에서 이미 months를 포함한 값)
        Project project = new Project(
                request.getName(),
                request.getDescription(),
                request.getRequirement(),
                (long) request.getTotalEstimate(),
                startDate,
                endDate,
                projectType,
                creator);
        projectRepository.save(project);

        for (ConfirmProjectRequest.RecruitmentRequest r : request.getRecruitmentList()) {
            Role role = parseRole(r.getRoleId());
            partnerRecruitRepository.save(
                    new PartnerRecruit(project, role, r.getCount(), RecruitStatus.OPEN, r.getCost()));
        }

        return ConfirmProjectResponse.of(project.getId());
    }

    @Transactional(readOnly = true)
    public HomeResponse getHome(Long memberId, Pageable pageable) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        // 진행 중인 내 프로젝트
        List<Project> projects = projectRepository.findAllByProjectMembers_Member_IdAndStatus(
                memberId, ProjectStatus.IN_PROGRESS);

        // creator로서의 프로젝트도 포함
        List<Project> creatorProjects = projectRepository.findAllByProjectMembers_Member_IdAndStatus(
                memberId, ProjectStatus.IN_PROGRESS);

        // 중복 제거: member + creator 프로젝트 합치기
        List<HomeResponse.MyProjectSummary> projectSummaries = projects.stream()
                .map(p -> {
                    long dDay = ChronoUnit.DAYS.between(LocalDate.now(), p.getEnd());
                    int progress = calculateProjectProgress(p);
                    return new HomeResponse.MyProjectSummary(
                            p.getId(), p.getName(),
                            p.getEnd().format(END_DATE_FORMATTER),
                            dDay, progress);
                })
                .toList();

        // 파트너 추천: 내 OPEN 공고의 역할과 매칭되는 멤버
        Set<Role> neededRoles = projects.stream()
                .flatMap(p -> p.getPartnerRecruits().stream())
                .filter(r -> r.getRecruitStatus() == RecruitStatus.OPEN)
                .map(PartnerRecruit::getRole)
                .collect(Collectors.toSet());

        List<HomeResponse.PartnerRecommendation> partners = List.of();
        boolean hasMore = false;
        if (!neededRoles.isEmpty()) {
            Page<Member> partnerPage = memberRepository.findAllByRoleIn(neededRoles, pageable);
            partners = partnerPage.getContent().stream()
                    .filter(m -> !m.getId().equals(memberId))
                    .map(m -> new HomeResponse.PartnerRecommendation(
                            m.getId(), m.getNickname(), m.getProfileImage(),
                            m.getRole(), m.getSelfIntroduction()))
                    .toList();
            hasMore = partnerPage.hasNext();
        }

        return new HomeResponse(projectSummaries, partners, hasMore);
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        long dDay = ChronoUnit.DAYS.between(LocalDate.now(), project.getEnd());

        List<ProjectDetailResponse.RecruitSummary> recruits = project.getPartnerRecruits().stream()
                .map(r -> new ProjectDetailResponse.RecruitSummary(
                        r.getId(), r.getRole(), r.getNumberOfPerson(),
                        r.getRecruitStatus().name()))
                .toList();

        List<Task> tasks = taskRepository.findAllByProject_IdOrderByCreatedAtAsc(projectId);
        List<ProjectDetailResponse.TaskSummary> taskSummaries = tasks.stream()
                .map(t -> {
                    List<ProjectDetailResponse.ToDoSummary> todos = t.getToDoList().stream()
                            .map(td -> new ProjectDetailResponse.ToDoSummary(
                                    td.getId(), td.getContent(), td.getIsDone()))
                            .toList();
                    int taskProgress = t.getToDoList().isEmpty() ? 0
                            : (int) (t.getToDoList().stream().filter(ToDo::getIsDone).count() * 100 / t.getToDoList().size());
                    return new ProjectDetailResponse.TaskSummary(
                            t.getId(), t.getName(), t.getDescription(),
                            t.getProjectMember().getMember().getNickname(),
                            taskProgress, todos);
                })
                .toList();

        List<ProjectDetailResponse.MemberSummary> members = project.getProjectMembers().stream()
                .map(pm -> new ProjectDetailResponse.MemberSummary(
                        pm.getMember().getId(), pm.getMember().getNickname(),
                        pm.getMember().getProfileImage(), pm.getRole()))
                .toList();

        int progressPercent = calculateProjectProgress(project);

        return new ProjectDetailResponse(
                project.getId(), project.getName(), project.getDescription(),
                project.getIntroduction(),
                project.getStart().format(END_DATE_FORMATTER),
                project.getEnd().format(END_DATE_FORMATTER),
                dDay, project.getStatus(), progressPercent,
                recruits, taskSummaries, members);
    }

    @Transactional
    public UpdateIntroductionResponse updateIntroduction(Long memberId, Long projectId, UpdateIntroductionRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        if (!project.getCreator().getId().equals(memberId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        project.updateIntroduction(request.getIntroduction());
        return UpdateIntroductionResponse.of(project.getId(), project.getIntroduction());
    }

    @Transactional
    public void completeProject(Long memberId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new OffException(ResponseCode.PROJECT_NOT_FOUND));

        if (!project.getCreator().getId().equals(memberId)) {
            throw new OffException(ResponseCode.UNAUTHORIZED_ACCESS);
        }

        if (project.getStatus() == ProjectStatus.COMPLETED) {
            throw new OffException(ResponseCode.PROJECT_ALREADY_COMPLETED);
        }

        project.complete();
    }

    private int calculateProjectProgress(Project project) {
        List<Task> tasks = taskRepository.findAllByProject_IdOrderByCreatedAtAsc(project.getId());
        if (tasks.isEmpty()) return 0;
        long totalTodos = tasks.stream().mapToLong(t -> t.getToDoList().size()).sum();
        if (totalTodos == 0) return 0;
        long doneTodos = tasks.stream()
                .flatMap(t -> t.getToDoList().stream())
                .filter(ToDo::getIsDone)
                .count();
        return (int) (doneTodos * 100 / totalTodos);
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

    private LocalDate estimateEndDate(LocalDate startDate, String description, String requirement) {
        String prompt = """
                당신은 IT 프로젝트 일정 산정 전문가입니다.
                아래 프로젝트 정보를 바탕으로, 시작일로부터 프로젝트 완료까지 필요한 예상 기간을 **일(day) 단위 숫자만** 답해주세요.
                예시: 90

                [시작일]
                %s

                [서비스 설명]
                %s

                [요구사항]
                %s
                """.formatted(startDate.format(END_DATE_FORMATTER), description, requirement);

        try {
            String result = geminiService.generateText(prompt);
            int days = Integer.parseInt(result.replaceAll("[^0-9]", ""));
            if (days < 7) days = 30;
            return startDate.plusDays(days);
        } catch (Exception e) {
            log.error("Gemini 마감일 산정 실패: {}", e.getMessage(), e);
            return startDate.plusDays(30);
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
