package com.example.off.domain.chat.repository;

import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findAllByMember_IdAndChatRoom_ChatType(Long memberId, ChatType chatRoomChatType);
    @Query("SELECT cp FROM ChatRoomMember cp " +
            "JOIN FETCH cp.member " +
            "WHERE cp.chatRoom.id = :roomId AND cp.member.id != :myId")
    Optional<ChatRoomMember> findOpponentByRoomIdAndMyId(
            @Param("roomId") Long roomId,
            @Param("myId") Long myId
    );

    boolean existsByChatRoom_IdAndMember_Id(Long chatRoomId, Long memberId);
}
