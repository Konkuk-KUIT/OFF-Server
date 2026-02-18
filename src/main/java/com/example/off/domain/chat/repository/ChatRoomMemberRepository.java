package com.example.off.domain.chat.repository;

import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    @Query("SELECT crm FROM ChatRoomMember crm " +
            "JOIN FETCH crm.chatRoom cr " +
            "LEFT JOIN FETCH cr.project " +
            "JOIN FETCH crm.member " +
            "WHERE crm.member.id = :memberId AND cr.chatType = :chatType")
    List<ChatRoomMember> findAllByMember_IdAndChatRoom_ChatType(
            @Param("memberId") Long memberId,
            @Param("chatType") ChatType chatType
    );

    @Query("SELECT crm FROM ChatRoomMember crm " +
            "JOIN FETCH crm.member " +
            "JOIN FETCH crm.chatRoom cr " +
            "WHERE cr.id = :roomId AND crm.member.id != :myId")
    List<ChatRoomMember> findOpponentByRoomIdAndMyId(
            @Param("roomId") Long roomId,
            @Param("myId") Long myId
    );

    boolean existsByChatRoom_IdAndMember_Id(Long chatRoomId, Long memberId);
}
