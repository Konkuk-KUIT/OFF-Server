package com.example.off.domain.partnerRecruit.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.partnerRecruit.dto.*;
import com.example.off.domain.partnerRecruit.service.PartnerMatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Partner Matching", description = "파트너 매칭 API")
@RestController
@RequiredArgsConstructor
public class PartnerMatchingController {
    private final PartnerMatchingService partnerMatchingService;

    @Operation(summary = "파트너 제안", description = "기획자가 파트너에게 프로젝트 참여를 제안합니다.")
    @PostMapping("/projects/{projectId}/invitations")
    @CustomExceptionDescription(SwaggerResponseDescription.INVITE_PARTNER)
    public BaseResponse<InvitePartnerResponse> invite(
            @PathVariable Long projectId,
            @Valid @RequestBody InvitePartnerRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(partnerMatchingService.invite(memberId, projectId, request));
    }

    @Operation(summary = "파트너 제안 수락", description = "파트너가 기획자의 제안을 수락합니다.")
    @PostMapping("/invitations/{invitationId}/accept")
    @CustomExceptionDescription(SwaggerResponseDescription.ACCEPT_INVITATION)
    public BaseResponse<AcceptInvitationResponse> acceptInvitation(
            @PathVariable Long invitationId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(partnerMatchingService.acceptInvitation(memberId, invitationId));
    }

    @Operation(summary = "프로젝트 지원", description = "파트너가 프로젝트에 지원합니다.")
    @PostMapping("/projects/{projectId}/applications")
    @CustomExceptionDescription(SwaggerResponseDescription.APPLY_PROJECT)
    public BaseResponse<ApplyProjectResponse> apply(
            @PathVariable Long projectId,
            @Valid @RequestBody ApplyProjectRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(partnerMatchingService.apply(memberId, projectId, request));
    }

    @Operation(summary = "지원 수락", description = "기획자가 파트너의 지원을 수락합니다. PayLog가 자동 생성됩니다.")
    @PostMapping("/projects/{projectId}/applications/{applicationId}/accept")
    @CustomExceptionDescription(SwaggerResponseDescription.ACCEPT_APPLICATION)
    public BaseResponse<AcceptApplicationResponse> acceptApplication(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(partnerMatchingService.acceptApplication(memberId, projectId, applicationId));
    }

    @Operation(summary = "파트너 프로필 조회", description = "파트너의 프로필 및 포트폴리오를 조회합니다.")
    @GetMapping("/partners/{partnerId}")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_PARTNER_PROFILE)
    public BaseResponse<PartnerProfileResponse> getPartnerProfile(
            @PathVariable Long partnerId
    ) {
        return BaseResponse.ok(partnerMatchingService.getPartnerProfile(partnerId));
    }

    private Long getMemberId(HttpServletRequest req) {
        Object attr = req.getAttribute("memberId");
        if (attr instanceof Long id) return id;
        throw new OffException(ResponseCode.INVALID_TOKEN);
    }
}
