package com.example.off.domain.member.dto;

import com.example.off.domain.member.ProjectCountType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @Size(max = 50)
    private String nickname;
    private ProjectCountType projectCountType;
    private List<PortfolioRequest> portfolioRequests;
    @Size(max = 1000)
    private String selfIntroduction;
}
