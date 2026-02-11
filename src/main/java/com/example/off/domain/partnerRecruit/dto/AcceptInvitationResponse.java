package com.example.off.domain.partnerRecruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptInvitationResponse {
    private Long projectMemberId;

    public static AcceptInvitationResponse of(Long projectMemberId) {
        return new AcceptInvitationResponse(projectMemberId);
    }
}
