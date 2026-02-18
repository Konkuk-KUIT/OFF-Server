package com.example.off.domain.member.dto;

import com.example.off.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileResponse {
    private Long memberId;
    private LocalDateTime updatedAt;

    public static UpdateProfileResponse from(Member member) {
        return new UpdateProfileResponse(
                member.getId(),
                member.getUpdatedAt()
        );
    }
}
