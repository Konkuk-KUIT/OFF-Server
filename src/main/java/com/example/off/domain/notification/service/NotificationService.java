package com.example.off.domain.notification.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.notification.Notification;
import com.example.off.domain.notification.NotificationType;
import com.example.off.domain.notification.dto.NotificationListResponse;
import com.example.off.domain.notification.dto.NotificationReadResponse;
import com.example.off.domain.notification.dto.NotificationResponse;
import com.example.off.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate; // 실시간 배달원 추가

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

    /**
     * 알림 생성 및 실시간 전송 (MVP 핵심 로직)
     */
    public void sendNotification(Long receiverId, String content, String url, NotificationType type) {
        // 1. 수신자 조회
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));

        // 2. 알림 DB 저장
        Notification notification = new Notification(content, url, false, type, receiver);
        notificationRepository.save(notification);

        // 3. 실시간 알림 전송 (STOMP)
        // /user/{receiverId}/queue/notifications 주소로 메시지가 전달됩니다.
        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/notifications",
                NotificationResponse.from(notification) // DTO로 변환해서 전송
        );
    }
}
