package com.example.off.domain.chat.dto;

import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatInitialSendResponse {
    private Long chatRoomId;
    private Long messageId;
    private String content;
    private LocalDateTime createdAt;
    private boolean isMine;

    public static ChatInitialSendResponse of(ChatRoom room, Message message) {
        return new ChatInitialSendResponse(
                room.getId(),
                message.getId(),
                message.getContent(),
                message.getCreatedAt(),
                true
        );
    }
}