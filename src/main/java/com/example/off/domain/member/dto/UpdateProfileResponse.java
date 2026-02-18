package com.example.off.domain.member.dto;

import com.example.off.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileResponse {
    private Long memberId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static UpdateProfileResponse from(Member member) {
        return new UpdateProfileResponse(
                member.getId(),
                member.getUpdatedAt()
        );
    }
}
