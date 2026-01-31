package com.example.off.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenService {
    //secret key 생성
    private static final String SECRET_KEY = "very-secret-key-please-change";
    private static final long EXPIRE_MS = 1000 * 60 * 60; // 1시간

    private static final SecretKey key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    //JWT 생성
    public String createToken(String userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRE_MS);

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
