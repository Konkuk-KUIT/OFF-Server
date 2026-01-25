package com.example.off.domain.notification.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.notification.Notification;
import com.example.off.domain.notification.dto.NotificationListResponse;
import com.example.off.domain.notification.dto.NotificationReadResponse;
import com.example.off.domain.notification.dto.NotificationResponse;
import com.example.off.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationListResponse getNotifications(Long memberId, Long cursor, int size) {
        int totalUnread = notificationRepository.countUnreadByMemberId(memberId);
        List<Notification> notifications = notificationRepository.findAllByMemberIdAndCursor(memberId, cursor,
                PageRequest.of(0, size + 1));
        boolean hasNext = notifications.size() > size;

        List<Notification> autoReadList = notifications.stream().limit(size)
                .filter(n -> n.getUrl() == null && !n.getIsRead()).peek(Notification::read).toList();
        long autoReadCount = autoReadList.size();
        int finalUnreadCount = Math.max(0, totalUnread - (int)autoReadCount);

        List<NotificationResponse> notificationResponses = notifications.stream().limit(size)
                .map(NotificationResponse::from).toList();

        return NotificationListResponse.of(finalUnreadCount, notificationResponses, hasNext);
    }

    public NotificationReadResponse readUrlNotification(Long memberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .filter(n -> n.getMember().getId().equals(memberId))
                .orElseThrow(() -> new OffException(ResponseCode.NOTIFICATION_NOT_FOUND));

        if (notification.getUrl() == null) {
            throw new OffException(ResponseCode.BAD_NOTIFICATION_REQUEST);
        }

        if (!notification.getIsRead()) {
            notification.read();
        }
        return new NotificationReadResponse(notification.getId(), notification.getIsRead());
    }
}
