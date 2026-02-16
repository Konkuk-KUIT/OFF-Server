package com.example.off.domain.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateIntroductionResponse {
    private Long projectId;
    private String introduction;

    public static UpdateIntroductionResponse of(Long projectId, String introduction) {
        return new UpdateIntroductionResponse(projectId, introduction);
    }
}
