package com.example.off.domain.partnerRecruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplyProjectResponse {
    private Long applicationId;

    public static ApplyProjectResponse of(Long applicationId) {
        return new ApplyProjectResponse(applicationId);
    }
}
