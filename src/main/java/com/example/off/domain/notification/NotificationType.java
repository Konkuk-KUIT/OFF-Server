package com.example.off.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    INVITE("파트너 매칭"),
    CHAT("채팅"),
    PAY("결제"),
    APPLICATION("지원 알림"),
    PROJECT_COMPLETE("프로젝트 완료");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
