package com.example.off.domain.notification.dto;

import com.example.off.domain.notification.Notification;

public record NotificationResponse(
        Long notificationId,
        String title,
        String type,
        String content,
        String redirectUrl,
        String createdAt,
        boolean isRead
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getNotificationType().getDescription(),
                notification.getNotificationType().name(),
                notification.getContent(),
                notification.getUrl(),
                notification.getCreatedAt().toString(),
                notification.getIsRead()
        );
    }
}
