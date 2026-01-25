package com.example.off.domain.notification;

public enum NotificationType {
    INVITE("파트너 매칭"),
    CHAT("채팅"),
    PAY("결제");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
