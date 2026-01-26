package com.example.off.domain.chat.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.ChatType;
import com.example.off.domain.chat.Message;
import com.example.off.domain.chat.dto.ChatMessageDetailResponse;
import com.example.off.domain.chat.dto.ChatRoomListResponse;
import com.example.off.domain.chat.dto.OpponentResponse;
import com.example.off.domain.chat.repository.ChatRoomMemberRepository;
import com.example.off.domain.chat.repository.MessageRepository;
import com.example.off.domain.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public ChatMessageDetailResponse getChatMessages(Long memberId, Long roomId, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Message> messages = messageRepository.findOlderMessages(roomId, cursor, pageable);

        boolean hasNext = messages.size() > size;
        if (hasNext) {
            messages = messages.subList(0, size);
        }

        ChatRoomMember opponentMember = chatRoomMemberRepository.findOpponentByRoomIdAndMyId(roomId, memberId)
                .orElseThrow(() -> new OffException(ResponseCode.OPPONENT_NOT_FOUND));

        List<ChatMessageDetailResponse.ChatMessageResponse> messageList = messages.stream()
                .map(m -> ChatMessageDetailResponse
                        .ChatMessageResponse.of(m,m.getMember().getId().equals(memberId)))
                .toList();

        return new ChatMessageDetailResponse(roomId, OpponentResponse.from(opponentMember),messageList, hasNext);
    }
}
