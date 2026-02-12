package com.example.off.domain.project.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.gemini.GeminiService;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.notification.NotificationType;
import com.example.off.domain.notification.service.NotificationService;
import com.example.off.domain.partnerRecruit.PartnerApplication;
import com.example.off.domain.partnerRecruit.PartnerRecruit;
import com.example.off.domain.partnerRecruit.RecruitStatus;
import com.example.off.domain.partnerRecruit.repository.PartnerApplicationRepository;
import com.example.off.domain.partnerRecruit.repository.PartnerRecruitRepository;
import com.example.off.domain.project.Project;
import com.example.off.domain.project.ProjectStatus;
import com.example.off.domain.project.ProjectType;
import com.example.off.domain.project.dto.*;
import com.example.off.domain.project.repository.ProjectRepository;
import com.example.off.domain.projectMember.ProjectMember;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import com.example.off.domain.role.Role;
import com.example.off.domain.task.Task;
import com.example.off.domain.task.ToDo;
import com.example.off.domain.task.repository.TaskRepository;
import com.example.off.domain.task.repository.ToDoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final ToDoRepository toDoRepository;
    private final GeminiService geminiService;
    private final NotificationService notificationService;

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

        // 3. Gemini로 파트너 추천 및 개별 가격 산정
        List<CreateProjectResponse.EstimateResponse> estimateList = new ArrayList<>();
        int totalEstimate = 0;

        for (RecruitmentInfo info : recruitments) {
            List<CreateProjectResponse.PartnerResponse> recommendedPartners =
                    recommendPartners(info.role, info.candidates, request.getDescription(),
                                    request.getRequirement(), info.cost, months);

            estimateList.add(CreateProjectResponse.EstimateResponse.of(
                    info.role.name(), info.cost, info.count, recommendedPartners));

            totalEstimate += info.cost * info.count;
        }

        // 4. 응답 구성 (DB 저장 없음 — 미리보기용)
        List<String> recruitmentRoles = recruitments.stream()
                .map(r -> r.role.name())
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

        // 3. DB 저장 (totalEstimate는 estimateProject에서 이미 months를 포함한 값)
        // isWorking 상태는 첫 파트너 매칭 완료 시점에 변경됨
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

        // 4. creator를 ProjectMember로 추가 (기획자도 프로젝트 멤버여야 태스크 생성 가능)
        ProjectMember creatorMember = ProjectMember.of(project, creator);
        projectMemberRepository.save(creatorMember);

        // 5. 파트너 모집 공고 생성 및 선택한 파트너에게 제안
        for (ConfirmProjectRequest.RecruitmentRequest r : request.getRecruitmentList()) {
            Role role = parseRole(r.getRoleId());
            PartnerRecruit recruit = partnerRecruitRepository.save(
                    new PartnerRecruit(project, role, r.getCount(), RecruitStatus.OPEN, r.getCost()));

            // 선택한 파트너들에게 제안
            if (r.getSelectedPartnerIds() != null && !r.getSelectedPartnerIds().isEmpty()) {
                for (Long partnerId : r.getSelectedPartnerIds()) {
                    Member partner = memberRepository.findById(partnerId)
                            .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

                    // 이미 지원했는지 확인
                    if (partnerApplicationRepository.existsByMemberAndPartnerRecruit(partner, recruit)) {
                        continue;
                    }

                    // PartnerApplication 생성 (기획자 제안, 가격은 recruit의 cost 사용)
                    PartnerApplication application = PartnerApplication.of(partner, recruit, true);
                    partnerApplicationRepository.save(application);

                    // 파트너에게 알림
                    notificationService.sendNotification(
                            partner.getId(),
                            project.getName() + " 프로젝트에서 파트너 제안이 도착했어요!",
                            "/invitations/" + application.getId(),
                            NotificationType.INVITE
                    );
                }
            }
        }

        // 6. Gemini로 Task 자동 생성
        generateAndSaveTasks(project, creatorMember, request.getDescription(), request.getRequirement());

        return ConfirmProjectResponse.of(project.getId());
    }

    private void generateAndSaveTasks(Project project, ProjectMember creator, String description, String requirement) {
        String prompt = """
                당신은 프로젝트 관리 전문가입니다.
                아래 프로젝트를 성공적으로 완료하기 위한 주요 Task와 세부 ToDo를 추천해주세요.

                [프로젝트 정보]
                - 프로젝트명: %s
                - 서비스 설명: %s
                - 요구사항: %s

                다음 형식의 JSON으로 응답해주세요:
                {
                  "tasks": [
                    {
                      "name": "Task 제목 (50자 이내)",
                      "description": "Task 설명 (200자 이내)",
                      "todos": ["할일1", "할일2", "할일3"]
                    }
                  ]
                }

                주의사항:
                - Task는 3~7개 정도로 구성
                - 각 Task는 구체적이고 실행 가능해야 함
                - 각 Task마다 3~5개의 세부 ToDo 포함
                - 개발 프로세스 순서에 맞게 구성 (기획 → 설계 → 개발 → 테스트)
                - JSON 형식만 출력하고 다른 텍스트는 포함하지 마세요
                """.formatted(project.getName(), description, requirement);

        try {
            String result = geminiService.generateTextSafe(prompt, "{}");
            parseTasks(result, project, creator);
        } catch (Exception e) {
            // Gemini 실패 시 기본 Task 생성
            createDefaultTasks(project, creator);
        }
    }

    private void parseTasks(String jsonResult, Project project, ProjectMember creator) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResult);
            JsonNode tasksNode = root.get("tasks");

            if (tasksNode != null && tasksNode.isArray()) {
                for (JsonNode taskNode : tasksNode) {
                    String name = taskNode.get("name").asText();
                    String description = taskNode.get("description").asText();

                    Task task = Task.of(name, description, project, creator);
                    taskRepository.save(task);

                    JsonNode todosNode = taskNode.get("todos");
                    if (todosNode != null && todosNode.isArray()) {
                        for (JsonNode todoNode : todosNode) {
                            ToDo toDo = ToDo.of(todoNode.asText(), task);
                            toDoRepository.save(toDo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // JSON 파싱 실패 시 기본 Task 생성
            createDefaultTasks(project, creator);
        }
    }

    private void createDefaultTasks(Project project, ProjectMember creator) {
        // 폴백: 기본 Task 생성
        String[][] defaultTasks = {
            {"요구사항 분석 및 기획", "프로젝트 요구사항을 상세히 분석하고 기획서를 작성합니다.",
             "요구사항 정리", "사용자 플로우 설계", "기능 명세서 작성"},
            {"UI/UX 디자인", "사용자 인터페이스와 사용자 경험을 설계합니다.",
             "와이어프레임 작성", "프로토타입 제작", "디자인 시스템 구축"},
            {"개발 환경 구축", "개발에 필요한 환경을 설정합니다.",
             "개발 도구 설치", "데이터베이스 설계", "프로젝트 구조 설정"},
            {"핵심 기능 개발", "주요 기능을 개발합니다.",
             "API 설계", "핵심 로직 구현", "데이터 연동"},
            {"테스트 및 배포", "개발된 기능을 테스트하고 배포합니다.",
             "단위 테스트 작성", "통합 테스트", "배포 환경 설정"}
        };

        for (String[] taskData : defaultTasks) {
            Task task = Task.of(taskData[0], taskData[1], project, creator);
            taskRepository.save(task);

            for (int i = 2; i < taskData.length; i++) {
                ToDo toDo = ToDo.of(taskData[i], task);
                toDoRepository.save(toDo);
            }
        }
    }

    @Transactional(readOnly = true)
    public HomeResponse getHome(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        // PM이고 진행 중인 본인 프로젝트가 없으면 프로젝트 생성 버튼 표시
        boolean showCreateButton = false;
        if (member.getRole() == Role.PM) {
            List<Project> myProjects = projectRepository.findAllByCreator_IdAndStatus(
                    memberId, ProjectStatus.IN_PROGRESS);
            showCreateButton = myProjects.isEmpty();
        }

        // 다른 사람들의 진행 중인 프로젝트 조회 (본인이 생성한 프로젝트 제외)
        List<Project> allProjects = projectRepository.findAllByStatus(ProjectStatus.IN_PROGRESS);
        List<HomeResponse.MyProjectSummary> projectSummaries = allProjects.stream()
                .filter(p -> !p.getCreator().getId().equals(memberId))
                .map(p -> {
                    long dDay = ChronoUnit.DAYS.between(LocalDate.now(), p.getEnd());
                    int progress = calculateProjectProgress(p);

                    // 모집 중인 역할 목록
                    List<HomeResponse.RecruitInfo> recruitList = p.getPartnerRecruits().stream()
                            .filter(r -> r.getRecruitStatus() == RecruitStatus.OPEN)
                            .map(r -> new HomeResponse.RecruitInfo(r.getRole(), r.getNumberOfPerson()))
                            .toList();

                    boolean isRecruiting = !recruitList.isEmpty();

                    return new HomeResponse.MyProjectSummary(
                            p.getId(), p.getName(),
                            p.getCreator().getNickname(),
                            p.getEnd().format(END_DATE_FORMATTER),
                            dDay, progress, isRecruiting, recruitList);
                })
                .toList();

        // 파트너 추천: 모든 진행 중인 프로젝트의 OPEN 공고와 매칭되는 멤버
        Set<Role> neededRoles = allProjects.stream()
                .flatMap(p -> p.getPartnerRecruits().stream())
                .filter(r -> r.getRecruitStatus() == RecruitStatus.OPEN)
                .map(PartnerRecruit::getRole)
                .collect(Collectors.toSet());

        List<HomeResponse.PartnerRecommendation> partners = List.of();
        if (!neededRoles.isEmpty()) {
            Page<Member> partnerPage = memberRepository.findAllByRoleIn(neededRoles, pageable);
            partners = partnerPage.getContent().stream()
                    .filter(m -> !m.getId().equals(memberId))
                    .map(m -> new HomeResponse.PartnerRecommendation(
                            m.getId(), m.getNickname(), m.getProfileImage(),
                            m.getRole(), m.getSelfIntroduction(),
                            m.getProjectCountType().getValue(),
                            m.getPortfolios().size()))
                    .toList();
        }

        return new HomeResponse(showCreateButton, projectSummaries, partners);
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

    private List<CreateProjectResponse.PartnerResponse> recommendPartners(
            Role role, List<Member> candidates, String description,
            String requirement, int avgCost, double months) {

        if (candidates.isEmpty()) {
            return List.of();
        }

        // 후보 파트너 정보 포맷팅
        StringBuilder candidateInfo = new StringBuilder();
        for (int i = 0; i < candidates.size(); i++) {
            Member m = candidates.get(i);
            candidateInfo.append(String.format(
                    "%d. 닉네임: %s, 자기소개: %s, 프로젝트 경험: %d회\n",
                    i + 1, m.getNickname(), m.getSelfIntroduction(),
                    m.getProjectCountType().getCount()
            ));
        }

        String prompt = """
                당신은 IT 프로젝트 파트너 매칭 전문가입니다.
                아래 프로젝트에 적합한 %s 파트너를 추천하고, 각 파트너의 적정 비용을 산정해주세요.

                [프로젝트 정보]
                - 서비스 설명: %s
                - 요구사항: %s
                - 평균 예상 비용: %d만원 (프로젝트 전체 기간)

                [후보 파트너 목록]
                %s

                출력 형식 (JSON):
                각 파트너별로 추천 여부와 적정 비용을 다음 형식으로 답해주세요.
                추천하고 싶은 파트너만 포함하되, 최소 3명, 최대 10명을 선택하세요.
                비용은 경험과 프로젝트 적합도에 따라 평균 비용의 70%%~150%% 범위 내에서 산정하세요.

                [
                  {"nickname": "파트너닉네임", "cost": 숫자만}
                ]

                예시:
                [
                  {"nickname": "박개발", "cost": 6000000},
                  {"nickname": "김코딩", "cost": 5500000}
                ]
                """.formatted(
                role.getValue(), description, requirement, avgCost, candidateInfo.toString()
        );

        try {
            String result = geminiService.generateTextSafe(prompt, "[]");
            return parsePartnerRecommendations(result, candidates, avgCost);
        } catch (Exception e) {
            log.error("Gemini 파트너 추천 실패 ({}): {}", role.name(), e.getMessage(), e);
            return fallbackRecommendPartners(candidates, avgCost);
        }
    }

    private List<CreateProjectResponse.PartnerResponse> parsePartnerRecommendations(
            String jsonResult, List<Member> candidates, int avgCost) {
        try {
            // JSON 파싱 (간단한 방식)
            List<CreateProjectResponse.PartnerResponse> result = new ArrayList<>();

            // JSON 배열 추출
            int start = jsonResult.indexOf('[');
            int end = jsonResult.lastIndexOf(']');
            if (start == -1 || end == -1) {
                return fallbackRecommendPartners(candidates, avgCost);
            }

            String jsonArray = jsonResult.substring(start + 1, end);
            String[] items = jsonArray.split("\\},\\s*\\{");

            for (String item : items) {
                item = item.replaceAll("[\\[\\]{}]", "").trim();
                if (item.isEmpty()) continue;

                String nickname = extractJsonValue(item, "nickname");
                String costStr = extractJsonValue(item, "cost");

                if (nickname != null && costStr != null) {
                    int cost = Integer.parseInt(costStr.replaceAll("[^0-9]", ""));

                    // 닉네임으로 Member 찾기
                    candidates.stream()
                            .filter(m -> m.getNickname().equals(nickname))
                            .findFirst()
                            .ifPresent(m -> result.add(
                                    CreateProjectResponse.PartnerResponse.of(m, cost)));
                }
            }

            return result.isEmpty() ? fallbackRecommendPartners(candidates, avgCost) : result;
        } catch (Exception e) {
            log.warn("파트너 추천 JSON 파싱 실패, 기본 방식 사용: {}", e.getMessage());
            return fallbackRecommendPartners(candidates, avgCost);
        }
    }

    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"?([^,}\"]+)\"?";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? m.group(1).trim() : null;
    }

    private List<CreateProjectResponse.PartnerResponse> fallbackRecommendPartners(
            List<Member> candidates, int avgCost) {
        // Gemini 실패 시 폴백: 경험 많은 순으로 추천
        return candidates.stream()
                .sorted((a, b) -> b.getProjectCountType().getCount()
                        .compareTo(a.getProjectCountType().getCount()))
                .limit(10)
                .map(m -> {
                    // 경험에 따른 가격 차등 (70% ~ 130%)
                    int count = m.getProjectCountType().getCount();
                    double multiplier = 0.7 + (count * 0.1);
                    if (multiplier > 1.3) multiplier = 1.3;
                    int cost = (int) (avgCost * multiplier);
                    return CreateProjectResponse.PartnerResponse.of(m, cost);
                })
                .toList();
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
