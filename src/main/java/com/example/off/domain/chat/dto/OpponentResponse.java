package com.example.off.domain.chat.dto;

import com.example.off.domain.chat.ChatRoomMember;

public record OpponentResponse(
        String nickname,
        String profileImage) {

    public static OpponentResponse from(ChatRoomMember opponent) {
        return new OpponentResponse(opponent.getMember().getNickname(),
                opponent.getMember().getProfileImage());
    }
}
