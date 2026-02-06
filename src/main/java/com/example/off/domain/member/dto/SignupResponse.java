package com.example.off.domain.member.dto;

import com.example.off.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SignupResponse {
    private Long memberId;
    private String email;
    private LocalDateTime createdAt;

    public static SignupResponse of(Member member){
        return new SignupResponse(
                member.getId(),
                member.getEmail(),
                member.getCreatedAt()
        );
    }
}
