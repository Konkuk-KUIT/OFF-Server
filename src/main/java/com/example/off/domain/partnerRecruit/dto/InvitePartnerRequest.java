package com.example.off.domain.partnerRecruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvitePartnerRequest {
    @NotNull
    private Long partnerId;

    @NotBlank
    private String role;
}
