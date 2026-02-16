package com.example.off.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTaskResponse {
    private Long taskId;

    public static CreateTaskResponse of(Long taskId) {
        return new CreateTaskResponse(taskId);
    }
}
