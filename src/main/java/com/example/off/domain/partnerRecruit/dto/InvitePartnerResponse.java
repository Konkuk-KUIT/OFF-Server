package com.example.off.domain.partnerRecruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvitePartnerResponse {
    private Long applicationId;

    public static InvitePartnerResponse of(Long applicationId) {
        return new InvitePartnerResponse(applicationId);
    }
}
