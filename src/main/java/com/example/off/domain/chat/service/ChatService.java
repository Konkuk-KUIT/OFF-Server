package com.example.off.domain.chat.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.ChatType;
import com.example.off.domain.chat.Message;
import com.example.off.domain.chat.dto.ChatRoomListResponse;
import com.example.off.domain.chat.repository.ChatRoomMemberRepository;
import com.example.off.domain.chat.repository.MessageRepository;
import com.example.off.domain.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;

    public ChatRoomListResponse getChatRoomList(Long memberId, ChatType chatType) {
        List<ChatRoomMember> myParticipations = chatRoomMemberRepository.findAllByMember_IdAndChatRoom_ChatType(memberId, chatType);
        List<ChatRoomListResponse.ChatRoomResponse> responses = myParticipations.stream()
                .map(participation -> {
                    ChatRoom room = participation.getChatRoom();
                    Project project = room.getProject();

                    ChatRoomMember opponent = chatRoomMemberRepository.findOpponentByRoomIdAndMyId(room.getId(), memberId)
                            .orElseThrow(() -> new OffException(ResponseCode.OPPONENT_NOT_FOUND));

                    Message lastMessage = messageRepository.findFirstByChatRoom_IdOrderByCreatedAtDesc(room.getId())
                            .orElse(null);

                    int unReadCount = messageRepository.countUnreadMessages(room.getId(), memberId);

                    return ChatRoomListResponse.ChatRoomResponse.of(room, opponent, project, lastMessage, unReadCount);
                })
                .toList();

        return new ChatRoomListResponse(responses);
    }
}
