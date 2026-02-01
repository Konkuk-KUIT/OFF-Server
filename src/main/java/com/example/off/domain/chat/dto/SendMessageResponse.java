package com.example.off.domain.chat.dto;

import com.example.off.domain.chat.Message;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendMessageResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private boolean isMine;

    public SendMessageResponse(Long id, String content, LocalDateTime createdAt, boolean isMine) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.isMine = isMine;
    }

    public static SendMessageResponse of(Message m, boolean isMine){
        return new SendMessageResponse(m.getId(), m.getContent(), m.getCreatedAt(), isMine);
    }
}
