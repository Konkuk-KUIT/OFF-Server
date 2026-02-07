package com.example.off.domain.project.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.project.dto.ConfirmProjectRequest;
import com.example.off.domain.project.dto.ConfirmProjectResponse;
import com.example.off.domain.project.dto.CreateProjectRequest;
import com.example.off.domain.project.dto.CreateProjectResponse;
import com.example.off.domain.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
