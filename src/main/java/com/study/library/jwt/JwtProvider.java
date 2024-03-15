package com.study.library.jwt;

import com.study.library.security.PrincipalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(Authentication authentication) {

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        int userId = principalUser.getUserId();
        String username = principalUser.getUsername();
        Collection<? extends GrantedAuthority> authorities = principalUser.getAuthorities();
        Date expireDate = new Date(new Date().getTime() + (1000 * 60 * 60 *24)); // 만료기간 (현재시간 + 하루)

        String accessToken = Jwts.builder() // 토큰을 생성해줌
                .claim("userId", userId) // json형식으로 k,v로 들어감
                .claim("username", username)
                .claim("authorities", authorities)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256) // 암호화(key, 알고리즘) => hs256 64bytes
                .compact();

        return accessToken;
    }
}
