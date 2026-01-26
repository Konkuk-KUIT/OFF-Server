package com.example.off.domain.chat.dto;

import com.example.off.domain.chat.Message;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageDetailResponse {
    private Long id;
    private OpponentResponse opponentResponse;
    private List<ChatMessageResponse> chatMessageResponses;
    private boolean hasNext;

    public static class ChatMessageResponse{
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private boolean isMine;

        public ChatMessageResponse(Long id, String content, LocalDateTime createdAt, boolean isMine) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.isMine = isMine;
        }
        public static ChatMessageResponse of(Message m, boolean isMine){
            return new ChatMessageResponse(m.getId(), m.getContent(), m.getCreatedAt(), isMine);
        }
    }

    public ChatMessageDetailResponse(Long id, OpponentResponse opponentResponse, List<ChatMessageResponse> chatMessageResponses, boolean hasNext) {
        this.id = id;
        this.opponentResponse = opponentResponse;
        this.chatMessageResponses = chatMessageResponses;
        this.hasNext = hasNext;
    }
}
