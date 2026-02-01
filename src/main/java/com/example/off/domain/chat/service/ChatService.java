package com.example.off.domain.chat.service;

import com.example.off.common.exception.OffException;
import com.example.off.common.response.ResponseCode;
import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.ChatType;
import com.example.off.domain.chat.Message;
import com.example.off.domain.chat.dto.*;
import com.example.off.domain.chat.repository.ChatRoomMemberRepository;
import com.example.off.domain.chat.repository.ChatRoomRepository;
import com.example.off.domain.chat.repository.MessageRepository;
import com.example.off.domain.member.Member;
import com.example.off.domain.member.repository.MemberRepository;
import com.example.off.domain.project.Project;
import com.example.off.domain.projectMember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final SimpMessageSendingOperations messagingTemplate;

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

    @Transactional
    public ChatMessageDetailResponse getChatMessages(Long memberId, Long roomId, Long cursor, int size) {
        messageRepository.markAsReadByRoomId(roomId, memberId);

        // 2. 다른 방에 안 읽은 게 더 있는지 확인 (레드닷 업데이트용)
        boolean hasUnread = messageRepository.existsUnreadMessages(memberId);
        messagingTemplate.convertAndSend("/sub/user/" + memberId + "/unread-status", Map.of("hasUnread", hasUnread));

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
                        .ChatMessageResponse.of(m, m.getMember().getId().equals(memberId)))
                .toList();

        return new ChatMessageDetailResponse(roomId, OpponentResponse.from(opponentMember), messageList, hasNext);
    }

    @Transactional
    public SendMessageResponse sendMessage(Long memberId, Long roomId, String content) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new OffException(ResponseCode.CHATROOM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        ChatRoomMember opponent = chatRoomMemberRepository.findOpponentByRoomIdAndMyId(room.getId(), memberId)
                .orElseThrow(() -> new OffException(ResponseCode.OPPONENT_NOT_FOUND));

        Message message = new Message(content, false, member, room);
        Message savedMessage = messageRepository.save(message);
        SendMessageResponse response = SendMessageResponse.of(savedMessage, true);
        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, response);

        String opponentUnreadChannel = "/sub/user/" + opponent.getId() + "/unread-status";
        messagingTemplate.convertAndSend(opponentUnreadChannel, Map.of("hasUnread", true));

        return response;
    }

    @Transactional
    public ChatInitialSendResponse createRoomAndSendMessage(Long memberId, ChatInitialSendRequest request) {
        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new OffException(ResponseCode.MEMBER_NOT_FOUND));
        Member opponent = memberRepository.findById(request.opponentId())
                .orElseThrow(() -> new OffException(ResponseCode.OPPONENT_NOT_FOUND));

        List<Long> myProjectIds = projectMemberRepository.findAllByMember_Id(memberId).stream()
                .map(pm -> pm.getProject().getId())
                .toList();

        boolean isSameProject = projectMemberRepository.findAllByMember_Id(request.opponentId()).stream()
                .anyMatch(pm -> myProjectIds.contains(pm.getProject().getId()));

        ChatType determinedType = isSameProject ? ChatType.PROJECT : ChatType.CONTACT;

        ChatRoom newRoom = new ChatRoom(determinedType);
        chatRoomRepository.save(newRoom);

        ChatRoomMember myParticipation = new ChatRoomMember(newRoom, me);
        ChatRoomMember opponentParticipation = new ChatRoomMember(newRoom, opponent);
        chatRoomMemberRepository.saveAll(List.of(myParticipation, opponentParticipation));

        SendMessageResponse response = this.sendMessage(memberId, newRoom.getId(), request.content());

        return ChatInitialSendResponse.of(newRoom, response);
    }
}
