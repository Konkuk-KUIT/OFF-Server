package com.example.off.domain.notification.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.notification.dto.NotificationListResponse;
import com.example.off.domain.notification.dto.NotificationReadResponse;
import com.example.off.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notification", description = "알림 관련 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회", description = "커서 기반 페이징 및 자동 읽음 처리가 포함된 알림 목록을 조회합니다.")
    @GetMapping
    @CustomExceptionDescription(SwaggerResponseDescription.GET_NOTIFICATIONS)
    public BaseResponse<NotificationListResponse> getNotifications(
            @RequestParam(name = "memberId", defaultValue = "1") Long memberId,
            @Parameter(description = "마지막으로 조회된 알림 ID (첫 페이지 조회 시 null)")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "한 번에 조회할 알림 개수 (기본값 10)")
            @RequestParam(defaultValue = "10") int size
    ) {
        NotificationListResponse data = notificationService.getNotifications(memberId, cursor, size);
        return BaseResponse.ok(data);
    }

    @Operation(summary = "URL 알림 읽음 처리", description = "URL이 포함된 알림을 클릭했을 때 읽음 상태로 변경합니다.")
    @PatchMapping("/{notificationId}/read")
    @CustomExceptionDescription(SwaggerResponseDescription.READ_NOTIFICATIONS)
    public BaseResponse<NotificationReadResponse> readNotification(
            @RequestParam(name = "memberId", defaultValue = "1") Long memberId,
            @PathVariable Long notificationId
    ) {
        NotificationReadResponse data = notificationService.readUrlNotification(memberId, notificationId);
        return BaseResponse.ok(data);
    }
}
