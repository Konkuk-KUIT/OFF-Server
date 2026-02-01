package com.example.off.domain.chat.repository;

import com.example.off.domain.chat.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findFirstByChatRoom_IdOrderByCreatedAtDesc(Long id);

    @Modifying(clearAutomatically = true) // 벌크 연산 후 영속성 컨텍스트를 비워 정합성을 유지합니다
    @Query("UPDATE Message m SET m.isRead = true " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.member.id != :myId " + // 수신 메시지만
            "AND m.isRead = false")
    void markAsReadByRoomId(@Param("roomId") Long roomId, @Param("myId") Long myId);

    @Query("SELECT COUNT(m) FROM Message m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.isRead = false " +
            "AND m.member.id != :memberId")
    int countUnreadMessages(Long id, Long memberId);

    @Query("SELECT m FROM Message m " +
            "JOIN FETCH m.member " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND (:cursor IS NULL OR m.id < :cursor) " +
            "ORDER BY m.id DESC")
    List<Message> findOlderMessages(@Param("roomId") Long roomId, @Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "JOIN FETCH m.member " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.id > :cursor " +
            "ORDER BY m.id ASC")
    List<Message> findNewerMessages(@Param("roomId") Long roomId, @Param("cursor") Long cursor, Pageable pageable);
}
