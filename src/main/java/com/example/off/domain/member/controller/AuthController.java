package com.example.off.domain.member.controller;


import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.member.dto.SignupRequest;
import com.example.off.domain.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호 등을 이용해 회원가입을 진행합니다.")
    @PostMapping("/signup")
    @CustomExceptionDescription(SwaggerResponseDescription.SIGNUP)
    public BaseResponse<Void> signup(
            @RequestBody @Valid SignupRequest signupRequest
    ){
        authService.signup(signupRequest);
        return BaseResponse.ok(null);
    }

//    @PostMapping("/login")
//    public SignupRequest login(){}
}
