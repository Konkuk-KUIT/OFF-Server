package com.example.off.domain.chat.dto;

public record SendMessageRequest(
        String content,
        Long roomId
) {
}
