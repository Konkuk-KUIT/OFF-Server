package com.example.off.domain.project.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.swagger.SwaggerResponseDescription;
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

    @Operation(summary = "프로젝트 생성", description = "프로젝트를 생성하고 견적/요약 정보를 반환합니다.")
    @PostMapping("/create")
    @CustomExceptionDescription(SwaggerResponseDescription.CREATE_PROJECT)
    public BaseResponse<CreateProjectResponse> createProject(
            @Parameter(hidden = true) @RequestParam(defaultValue = "1") Long memberId,
            @RequestBody CreateProjectRequest request
    ) {
        CreateProjectResponse data = projectService.createProject(memberId, request);
        return BaseResponse.ok(data);
    }
}
