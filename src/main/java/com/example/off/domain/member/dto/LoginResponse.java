package com.example.off.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType;

    public static LoginResponse of(String accessToken, String tokenType){
        return new LoginResponse(accessToken, tokenType);
    }
}
