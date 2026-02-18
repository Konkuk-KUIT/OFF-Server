package com.example.off.domain.chat.dto;

import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.chat.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatInitialSendResponse {
    private Long chatRoomId;
    private Long messageId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonProperty("isMine")
    private boolean isMine;

    public static ChatInitialSendResponse of(ChatRoom room, SendMessageResponse response) {
        return new ChatInitialSendResponse(
                room.getId(),
                response.getId(),
                response.getContent(),
                response.getCreatedAt(),
                true
        );
    }
}