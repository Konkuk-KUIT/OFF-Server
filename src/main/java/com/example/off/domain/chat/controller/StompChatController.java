package com.example.off.domain.chat.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.chat.dto.SendMessageRequest;
import com.example.off.domain.chat.dto.SendMessageResponse;
import com.example.off.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller // 실시간 통신을 위해 @Controller 사용
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * 채팅 메시지 전송
     * 클라이언트가 /pub/chat/message 로 메시지를 보내면 호출됨
     */
    @MessageMapping("/chat/message")
    @CustomExceptionDescription(SwaggerResponseDescription.SEND_MESSAGES)
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        // 1. 인터셉터에서 심어준 StompPrincipal에서 memberId 추출
        Long memberId = Long.parseLong(principal.getName());

        // 2. 서비스 로직 수행 (DB 저장 등)
        SendMessageResponse response = chatService.sendMessage(memberId, request.roomId(), request.content());

        // 3. 해당 채팅방을 구독 중인(/sub/chat/room/{roomId}) 모든 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.roomId(), response);
    }
}
