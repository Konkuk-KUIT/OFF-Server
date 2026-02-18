package com.example.off.common.interceptor;

import com.example.off.common.auth.StompPrincipal;
import com.example.off.common.exception.OffException;
import com.example.off.common.jwt.JwtTokenProvider;
import com.example.off.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || token.isBlank()) {
                throw new OffException(ResponseCode.TOKEN_NOT_FOUND);
            }
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
            if (memberId == null) {
                throw new OffException(ResponseCode.INVALID_TOKEN);
            }
            accessor.setUser(new StompPrincipal(memberId.toString()));
        }
        return message;
    }
}
