package com.example.off.domain.notification.dto;

import com.example.off.domain.notification.Notification;

import java.time.format.DateTimeFormatter;

public record NotificationResponse(
        Long notificationId,
        String title,
        String type,
        String content,
        String redirectUrl,
        String createdAt,
        boolean isRead
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getNotificationType().getDescription(),
                notification.getNotificationType().name(),
                notification.getContent(),
                notification.getUrl(),
                notification.getCreatedAt().format(FORMATTER),
                notification.getIsRead()
        );
    }
}
