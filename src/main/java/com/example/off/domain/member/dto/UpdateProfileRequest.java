package com.example.off.domain.member.dto;

import com.example.off.domain.member.ProjectCountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

import static com.example.off.domain.member.Member.NICKNAME_MAX_LENGTH;
import static com.example.off.domain.member.Member.SELF_INTRO_MAX_LENGTH;

public record UpdateProfileRequest(
        @Size(max = NICKNAME_MAX_LENGTH)
        @NotBlank
        String nickname,

        ProjectCountType projectCountType,
        List<PortfolioRequest> portfolioRequests,

        @Size(max = SELF_INTRO_MAX_LENGTH)
        String selfIntroduction
) {
}
