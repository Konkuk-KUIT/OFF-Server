package com.example.off.domain.task.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.task.dto.*;
import com.example.off.domain.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task", description = "태스크 관리 API")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "태스크 생성", description = "프로젝트에 새 태스크를 생성합니다.")
    @PostMapping("/projects/{projectId}/tasks")
    @CustomExceptionDescription(SwaggerResponseDescription.CREATE_TASK)
    public BaseResponse<CreateTaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(taskService.createTask(memberId, projectId, request));
    }

    @Operation(summary = "태스크 수정", description = "태스크의 제목, 설명, 담당자를 변경합니다.")
    @PatchMapping("/tasks/{taskId}")
    @CustomExceptionDescription(SwaggerResponseDescription.UPDATE_TASK)
    public BaseResponse<UpdateTaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(taskService.updateTask(memberId, taskId, request));
    }

    @Operation(summary = "태스크 삭제", description = "태스크 및 하위 할일을 삭제합니다.")
    @DeleteMapping("/tasks/{taskId}")
    @CustomExceptionDescription(SwaggerResponseDescription.DELETE_TASK)
    public BaseResponse<Void> deleteTask(
            @PathVariable Long taskId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        taskService.deleteTask(memberId, taskId);
        return BaseResponse.ok();
    }

    @Operation(summary = "할일 완료 토글", description = "할일의 완료 상태를 토글합니다.")
    @PatchMapping("/tasks/{taskId}/mini-tasks/{miniTaskId}/status")
    @CustomExceptionDescription(SwaggerResponseDescription.TOGGLE_TODO)
    public BaseResponse<ToggleToDoResponse> toggleToDo(
            @PathVariable Long taskId,
            @PathVariable Long miniTaskId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(taskService.toggleToDo(memberId, taskId, miniTaskId));
    }

    private Long getMemberId(HttpServletRequest req) {
        Object attr = req.getAttribute("memberId");
        if (attr instanceof Long id) return id;
        throw new OffException(ResponseCode.INVALID_TOKEN);
    }
}
