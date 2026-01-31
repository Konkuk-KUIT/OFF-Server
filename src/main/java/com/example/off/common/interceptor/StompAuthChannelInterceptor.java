package com.example.off.common.interceptor;

import com.example.off.common.auth.StompPrincipal;
import com.example.off.common.exception.OffException;
import com.example.off.common.jwt.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.example.off.common.response.ResponseCode.INVALID_TOKEN;
import static com.example.off.common.response.ResponseCode.TOKEN_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenService jwtTokenService;

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) { // 연결 시점에만 검증!
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || token.isBlank()) {
                throw new OffException(TOKEN_NOT_FOUND);
            }
            Long memberId = jwtTokenService.getMemberIdFromToken(token);
            if(memberId == null){
                throw new OffException(INVALID_TOKEN);
            }
            accessor.setUser(new StompPrincipal(memberId.toString()));
        }
        return message;
    }
}
