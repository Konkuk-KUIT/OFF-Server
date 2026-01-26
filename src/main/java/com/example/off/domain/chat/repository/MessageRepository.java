package com.example.off.domain.chat.repository;

import com.example.off.domain.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findFirstByChatRoom_IdOrderByCreatedAtDesc(Long id);

    @Query("SELECT COUNT(m) FROM Message m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.isRead = false " +
            "AND m.member.id != :memberId")
    int countUnreadMessages(Long id, Long memberId);
}
