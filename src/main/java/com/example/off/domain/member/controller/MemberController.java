package com.example.off.domain.member.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.member.dto.ProfileResponse;
import com.example.off.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "내 프로필 조회", description = "accessToken 을 기반으로 현재 로그인한 회원의 정보를 불러옵니다.")
    @GetMapping("/me")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_MY_PROFILE)
    public BaseResponse<ProfileResponse> getMyProfile(HttpServletRequest request){
        Long memberId = (Long) request.getAttribute("memberId");
        if (memberId == null ) {
            throw new OffException(ResponseCode.INVALID_TOKEN);
        }

        ProfileResponse data = memberService.getMyProfile(memberId);
        return BaseResponse.ok(data);
    }

    @Operation(summary = "내 프로필 수정하기")
    @GetMapping("/me")
    public String updateMyProfile(){
        return "string";
    }

    public String getJoinedProjects() {
        return "string";
    }
}
