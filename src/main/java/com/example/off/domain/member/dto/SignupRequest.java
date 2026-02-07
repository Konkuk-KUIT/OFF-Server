package com.example.off.domain.member.dto;

import com.example.off.domain.member.ProjectCountType;
import com.example.off.domain.role.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    //가본 회원 정보
    @NotNull
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate birth;

    @NotNull
    private String email;
    @NotNull
    private String password;

    //프로필 정보
    private String profileImage;
    @NotNull
    private String nickname;
    @NotNull
    private Role role;
    @NotNull
    private ProjectCountType projectCount;
    private String selfIntroduction;
    private List<PortfolioRequest> portfolioList;
}
