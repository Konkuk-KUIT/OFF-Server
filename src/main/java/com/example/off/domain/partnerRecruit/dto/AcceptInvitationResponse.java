package com.example.off.domain.partnerRecruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptInvitationResponse {
    private Long applicationId;

    public static AcceptInvitationResponse of(Long applicationId) {
        return new AcceptInvitationResponse(applicationId);
    }
}
