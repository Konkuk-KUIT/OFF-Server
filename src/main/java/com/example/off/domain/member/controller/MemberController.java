package com.example.off.domain.member.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.member.dto.MyProjectsResponse;
import com.example.off.domain.member.dto.ProfileResponse;
import com.example.off.domain.member.dto.UpdateProfileRequest;
import com.example.off.domain.member.dto.UpdateProfileResponse;
import com.example.off.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "내 프로필 조회 및 수정",
            description = "accessToken 을 기반으로 현재 로그인한 회원의 정보를 불러옵니다.")
    @GetMapping("/me")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_MY_PROFILE)
    public BaseResponse<ProfileResponse> getMyProfile(HttpServletRequest request) {
        Long memberId = getMemberId(request);

        ProfileResponse data = memberService.getMyProfile(memberId);
        return BaseResponse.ok(data);
    }

    @Operation(summary = "내 프로필 수정하기", description = "현재 록인한 회원의 프로필을 수정합니다.")
    @PatchMapping("/me")
    @CustomExceptionDescription(SwaggerResponseDescription.UPDATE_PROFILE)
    //header 와 body 모두 필요함
    public BaseResponse<UpdateProfileResponse> updateMyProfile(
            HttpServletRequest request,
            @Valid @RequestBody UpdateProfileRequest updateRequest
            ) {
        Long memberId = getMemberId(request);
        UpdateProfileResponse data = memberService.updateProfile(memberId, updateRequest);
        return BaseResponse.ok(data);
    }

    @Operation(summary = "참여한 프로젝트 조회",
            description = "현재 로그인한 회원이 참여했던 프로젝트 정보를 불러옵니다.")
    @GetMapping("/me/projects")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_MY_PROJECTS)
    public BaseResponse<MyProjectsResponse> getMyProjects(HttpServletRequest request) {
        Long memberId = getMemberId(request);

        MyProjectsResponse data = memberService.getMyProjects(memberId);
        return BaseResponse.ok(data);
    }

    private Long getMemberId (HttpServletRequest req){
        Object memberId = req.getAttribute("memberId");
        if (!(memberId instanceof Long id)) {
            throw new OffException(ResponseCode.INVALID_TOKEN);
        }
        return id;
    }
}
