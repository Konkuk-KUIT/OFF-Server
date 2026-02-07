package com.example.off.domain.member.dto;

import com.example.off.domain.member.ProjectCountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.off.domain.member.Member.NICKNAME_MAX_LENGTH;
import static com.example.off.domain.member.Member.SELF_INTRO_MAX_LENGTH;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @Size(max = NICKNAME_MAX_LENGTH)
    @NotBlank
    private String nickname;

    private ProjectCountType projectCountType;
    private List<PortfolioRequest> portfolioRequests;

    @Size(max = SELF_INTRO_MAX_LENGTH)
    private String selfIntroduction;
}
