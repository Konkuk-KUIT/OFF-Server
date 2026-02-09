package com.example.off.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmProjectResponse {
    private Long projectId;

    public static ConfirmProjectResponse of(Long projectId) {
        return new ConfirmProjectResponse(projectId);
    }
}
