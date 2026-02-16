package com.example.off.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTaskResponse {
    private Long taskId;

    public static UpdateTaskResponse of(Long taskId) {
        return new UpdateTaskResponse(taskId);
    }
}
