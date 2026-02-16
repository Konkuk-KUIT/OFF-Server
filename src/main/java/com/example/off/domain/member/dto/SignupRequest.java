package com.example.off.domain.member.dto;

import com.example.off.domain.member.ProjectCountType;
import com.example.off.domain.role.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.off.domain.member.Member.NICKNAME_MAX_LENGTH;
import static com.example.off.domain.member.Member.SELF_INTRO_MAX_LENGTH;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    //기본 회원 정보
    @NotNull
    @NotBlank
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate birth;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    //프로필 정보
    private String profileImage;

    @NotNull
    @NotBlank
    @Size(max = NICKNAME_MAX_LENGTH, message = "닉네임은 최대 50자까지 가능합니다.")
    private String nickname;

    @NotNull
    private Role role;

    @NotNull
    private ProjectCountType projectCount;

    @Size(max = SELF_INTRO_MAX_LENGTH, message = "자기소개는 최대 1000자까지 가능합니다.")
    private String selfIntroduction;

    private List<PortfolioRequest> portfolioList;
}
