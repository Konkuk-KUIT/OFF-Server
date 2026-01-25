package com.example.off.domain.notification.repository;

import com.example.off.domain.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n " + "WHERE n.member.id = :memberId " +
            "AND (:cursor IS NULL OR n.id < :cursor) " + "ORDER BY n.id DESC")
    List<Notification> findAllByMemberIdAndCursor(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT COUNT(n) FROM Notification n " + "WHERE n.member.id = :memberId AND n.isRead = false")
    int countUnreadByMemberId(@Param("memberId") Long memberId);
}
