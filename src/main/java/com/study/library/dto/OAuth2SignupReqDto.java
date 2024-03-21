package com.study.library.dto;

import com.study.library.entity.OAuth2;
import com.study.library.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OAuth2SignupReqDto {

    @Pattern(regexp = "^[A-Za-z0-9]{4,14}$", message = "대소문자, 숫자 5 ~ 14자리 형식이어야 합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{7,128}$", message = "하나 이상의 영문자, 숫자, 특수문자를 포함한 8 ~ 128자리 형식이어야 합니다.")
    private String password;
    @Pattern(regexp = "^[가-힣]{1,}$", message = "2글자 이상 한글로만 입력하여야 합니다")
    private String name;
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String oauth2Name;
    @NotBlank
    private String providerName;

    // dto는 bean이 아니기 때문에 service에서 매개변수로 받는다.
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .build();
    }

    public OAuth2 toOAuth2Entity(int userId) {
        return OAuth2.builder()
                .oAuth2Name(oauth2Name)
                .userId(userId)
                .providerName(providerName)
                .build();
    }
}
