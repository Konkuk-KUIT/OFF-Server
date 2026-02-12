package com.example.off.domain.chat.dto;

import com.example.off.domain.chat.ChatRoom;
import com.example.off.domain.chat.ChatRoomMember;
import com.example.off.domain.chat.ChatType;
import com.example.off.domain.chat.Message;
import com.example.off.domain.project.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomListResponse {
    List<ChatRoomResponse> chatRoomResponses;

    public static class ChatRoomResponse{

        @Schema(description = "채팅방 아이디", example = "105")
        private Long id;
        private ChatType chatType;
        private OpponentResponse opponentResponse;
        private ChatProjectInfo chatProjectInfo;
        private LastMessageInfo lastMessageInfo;
        private int unReadCount;

        public static class ChatProjectInfo {
            private Long id;
            private String name;

            public ChatProjectInfo(Long id, String name) {
                this.id = id;
                this.name = name;
            }
            public static ChatProjectInfo from(Project project){
                return new ChatProjectInfo(project.getId(), project.getName());
            }
        }
        public static class LastMessageInfo {
            private String content;
            private LocalDateTime createdAt;

            public LastMessageInfo(String content, LocalDateTime createdAt) {
                this.content = content;
                this.createdAt = createdAt;
            }

            public static LastMessageInfo of(Message message){
                return new LastMessageInfo(message.getContent(), message.getCreatedAt());
            }
        }

        public ChatRoomResponse(Long id, ChatType chatType, OpponentResponse opponentResponse, ChatProjectInfo chatProjectInfo, LastMessageInfo lastMessageInfo, int unReadCount) {
            this.id = id;
            this.chatType = chatType;
            this.opponentResponse = opponentResponse;
            this.chatProjectInfo = chatProjectInfo;
            this.lastMessageInfo = lastMessageInfo;
            this.unReadCount = unReadCount;
        }

        public static ChatRoomResponse of(ChatRoom room, ChatRoomMember opponent, Project project,
                                          Message lastMessage, int unReadCount){
            return new ChatRoomResponse(room.getId(), room.getChatType(), OpponentResponse.from(opponent),
                    project == null ? null : ChatProjectInfo.from(project),
                    lastMessage == null ? null : LastMessageInfo.of(lastMessage),
                    unReadCount);
        }
    }


}
