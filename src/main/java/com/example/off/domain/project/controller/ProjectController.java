package com.example.off.domain.project.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.project.dto.*;
import com.example.off.domain.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Project", description = "프로젝트 관련 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "프로젝트 견적 미리보기", description = "LLM 기반으로 기획안/견적을 생성하여 미리보기를 제공합니다. (DB 저장 없음)")
    @PostMapping("/estimate")
    @CustomExceptionDescription(SwaggerResponseDescription.ESTIMATE_PROJECT)
    public BaseResponse<CreateProjectResponse> estimateProject(
            @Parameter(hidden = true) @RequestParam(defaultValue = "1") Long memberId,
            @RequestBody CreateProjectRequest request
    ) {
        CreateProjectResponse data = projectService.estimateProject(memberId, request);
        return BaseResponse.ok(data);
    }

    @Operation(summary = "프로젝트 확정 생성", description = "사용자가 수정한 견적/기획안을 확정하여 프로젝트를 생성합니다.")
    @PostMapping("/confirm")
    @CustomExceptionDescription(SwaggerResponseDescription.CONFIRM_PROJECT)
    public BaseResponse<ConfirmProjectResponse> confirmProject(
            @Parameter(hidden = true) @RequestParam(defaultValue = "1") Long memberId,
            @RequestBody ConfirmProjectRequest request
    ) {
        ConfirmProjectResponse data = projectService.confirmProject(memberId, request);
        return BaseResponse.ok(data);
    }

    @Operation(summary = "프로젝트 상세 조회", description = "프로젝트 상세 정보(D-Day, 모집 현황, Task+ToDo, 진행률)를 조회합니다.")
    @GetMapping("/{projectId}")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_PROJECT_DETAIL)
    public BaseResponse<ProjectDetailResponse> getProjectDetail(
            @PathVariable Long projectId
    ) {
        return BaseResponse.ok(projectService.getProjectDetail(projectId));
    }

    @Operation(summary = "프로젝트 소개 수정", description = "프로젝트 소개를 수정합니다.")
    @PatchMapping("/{projectId}")
    @CustomExceptionDescription(SwaggerResponseDescription.UPDATE_INTRODUCTION)
    public BaseResponse<UpdateIntroductionResponse> updateIntroduction(
            @PathVariable Long projectId,
            @Valid @RequestBody UpdateIntroductionRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(projectService.updateIntroduction(memberId, projectId, request));
    }

    @Operation(summary = "프로젝트 완료 처리", description = "프로젝트를 완료 상태로 변경합니다. (creator만 가능)")
    @PatchMapping("/{projectId}/status")
    @CustomExceptionDescription(SwaggerResponseDescription.COMPLETE_PROJECT)
    public BaseResponse<Void> completeProject(
            @PathVariable Long projectId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        projectService.completeProject(memberId, projectId);
        return BaseResponse.ok();
    }

    private Long getMemberId(HttpServletRequest req) {
        Object attr = req.getAttribute("memberId");
        if (attr instanceof Long id) return id;
        throw new OffException(ResponseCode.INVALID_TOKEN);
    }
}
