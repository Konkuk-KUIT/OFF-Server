package com.example.off.common.config;

import com.example.off.common.interceptor.StompAuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompAuthChannelInterceptor authInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*").withSockJS(); // 임시로 모든 도메인 허용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. [구독] 서버가 해당 주소를 구독한 '모든' 클라이언트에게 메시지를 배달하는 통로
        // /sub/chat/1 (채팅방), /queue/notifications (알림)
        registry.enableSimpleBroker("/sub", "/queue");

        // 2. [발행] 클라이언트가 메시지를 서버(Controller)로 보낼 때 쓰는 접두사
        // /pub/chat/message 처럼 사용
        registry.setApplicationDestinationPrefixes("/pub");

        // 3. [개인 전용] 알림이나 개인 메시지를 보낼 때 사용하는 식별자
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}
