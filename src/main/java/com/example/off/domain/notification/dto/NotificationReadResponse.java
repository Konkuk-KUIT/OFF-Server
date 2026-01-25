package com.example.off.domain.notification.dto;

public record NotificationReadResponse(
    Long notificationId,
    boolean isRead
) {
}
