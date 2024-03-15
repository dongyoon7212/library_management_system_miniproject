package com.study.library.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    public String generateToken(Authentication authentication) {

        String accessToken = Jwts.builder() // 토큰을 생성해줌
                .compact();

        return accessToken;
    }
}
