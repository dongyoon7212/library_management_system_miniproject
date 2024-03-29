package com.study.library.jwt;

import com.study.library.entity.User;
import com.study.library.repository.UserMapper;
import com.study.library.security.PrincipalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;

    // 여기에 autowired를 하면 final인 key를 들고오지 못함
    private UserMapper userMapper;

    // 생성자 매개변수에 autowired
    public JwtProvider(@Value("${jwt.secret}") String secret, @Autowired UserMapper userMapper) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.userMapper = userMapper;
    }

    public String generateToken(User user) {

        int userId = user.getUserId();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        Date expireDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 20)); // 만료기간 (현재시간 + 하루)

        String accessToken = Jwts.builder() // 토큰을 생성해줌
                .claim("userId", userId) // json형식으로 k,v로 들어감
                .claim("username", username)
                .claim("authorities", authorities)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256) // 암호화(key, 알고리즘) => hs256 64bytes
                .compact();

        return accessToken;
    }

    public String removeBearer(String token) {
        if(!StringUtils.hasText(token)) {
            return null;
        }

        return token.substring("Bearer ".length());
    }

    public Claims getClaims(String token) {
        Claims claims = null;

        claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build() // key를 통해 토큰 해석
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public Authentication getAuthentication(Claims claims) {
        String username = claims.get("username").toString();
        User user = userMapper.findUserByUsername(username);

        if(user == null) {
            // 토큰은 유효하지만 DB에서 user정보가 삭제되었을 경우
            return null;
        }
        PrincipalUser principalUser = user.toPrincipalUser();
        return new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities()); //password는 앞에서 검증이 되었기 때문에 비어있어도 된다.
        // Authentication으로 업케스팅 되서 리턴
    }

    public String generateAuthMailToken(int userId, String toMailAddress) {
        Date expireDate = new Date(new Date().getTime() + (1000 * 60 * 5));

        return Jwts.builder()
                .claim("userId", userId)
                .claim("toMailAddress", toMailAddress)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
