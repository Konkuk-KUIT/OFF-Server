package com.example.off.domain.partnerRecruit.dto;

import com.example.off.domain.partnerRecruit.PartnerApplication;
import com.example.off.domain.role.Role;

public record ApplicationDetailResponse(
        Long applicationId,
        Long projectId,
        String projectName,
        String projectDescription,
        Long partnerId,
        String partnerNickname,
        String partnerIntroduction,
        Role role,
        Long cost,
        String status,
        boolean isFromProject
) {
    public static ApplicationDetailResponse from(PartnerApplication application) {
        return new ApplicationDetailResponse(
                application.getId(),
                application.getPartnerRecruit().getProject().getId(),
                application.getPartnerRecruit().getProject().getName(),
                application.getPartnerRecruit().getProject().getDescription(),
                application.getMember().getId(),
                application.getMember().getNickname(),
                application.getMember().getSelfIntroduction(),
                application.getPartnerRecruit().getRole(),
                application.getPartnerRecruit().getCost(),
                application.getApplicationStatus().name(),
                application.getIsFromProject()
        );
    }
}
