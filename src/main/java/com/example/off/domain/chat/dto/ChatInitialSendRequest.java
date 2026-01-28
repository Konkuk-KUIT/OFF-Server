package com.example.off.domain.chat.dto;

public record ChatInitialSendRequest(
        Long opponentId,
        String content
) {
}
