package com.example.off.common.jwt.service;

import com.example.off.common.exception.OffException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import static com.example.off.common.response.ResponseCode.INVALID_TOKEN;

@Slf4j
@Service
public class JwtTokenService {

    // 보통 설정파일(@Value)에서 가져오거나 상수로 정의되어 있을 거예요.
    private final String secretKeyString = "여기에_엄청_긴_비밀키가_들어있을_거예요_최소_32자_이상";

    /**
     * 토큰에서 MemberId(Long) 추출하기
     */
    public Long getMemberIdFromToken(String token) {
        try {
            // 1. 비밀키 세팅
            SecretKey key = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

            // 2. 토큰을 열어서 그 안의 정보(Claims)를 가져옴
            Claims claims = Jwts.parser()             // parserBuilder() 아님!
                    .verifyWith(key)                 // setSigningKey() 대신 verifyWith()
                    .build()
                    .parseSignedClaims(token)        // parseClaimsJws() 대신 parseSignedClaims()
                    .getPayload();                   // getBody() 대신 getPayload()

            // 3. 'subject' 칸에 저장된 ID를 꺼내서 Long으로 변환
            // (보통 subject에 memberId를 넣어둡니다. 만약 다른 키값에 넣었다면 claims.get("키이름")으로 꺼내야 해요!)
            return Long.parseLong(claims.getSubject());

        } catch (Exception e) {
            // 토큰이 가짜거나, 만료됐거나, 형식이 이상하면 여기서 에러가 납니다.
            log.error("JWT 토큰에서 memberId 추출 실패: {}", e.getMessage());
            throw new OffException(INVALID_TOKEN); // 사용자님이 쓰시는 예외 처리
        }
    }
}