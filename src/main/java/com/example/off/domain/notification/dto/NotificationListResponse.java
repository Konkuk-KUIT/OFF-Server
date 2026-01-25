package com.example.off.domain.notification.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NotificationListResponse {
    private int unReadCount;
    private List<NotificationResponse> notifications;
    private boolean hasNext;

    public static NotificationListResponse of(int unReadCount, List<NotificationResponse> notifications, boolean hasNext){
        return new NotificationListResponse(unReadCount, notifications, hasNext);
    }
}
