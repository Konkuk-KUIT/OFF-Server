package com.example.off.domain.chat.controller;

import com.example.off.common.response.BaseResponse;
import com.example.off.domain.chat.ChatType;
import com.example.off.domain.chat.dto.ChatMessageDetailResponse;
import com.example.off.domain.chat.dto.ChatRoomListResponse;
import com.example.off.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "채팅 관련 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 목록 조회", description = "사용자가 참여 중인 채팅방 목록을 필터링하여 조회합니다.")
    @GetMapping("/rooms")
    public BaseResponse<ChatRoomListResponse> getChatRoomList(
            @Parameter(hidden = true) @RequestParam(defaultValue = "1") Long memberId,
            @Parameter(description = "채팅방 타입 (개인/팀 등)")
            @RequestParam ChatType type
    ) {
        ChatRoomListResponse data = chatService.getChatRoomList(memberId, type);
        return BaseResponse.ok(data);
    }

    @GetMapping("/rooms/{roomId}")
    public BaseResponse<ChatMessageDetailResponse> getMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size,
            @Parameter(hidden = true) @RequestParam(defaultValue = "1") Long memberId // 임시 인증 ID
    ) {
        ChatMessageDetailResponse data = chatService.getChatMessages(memberId, roomId, cursor, size);
        return BaseResponse.ok(data);
    }
}
