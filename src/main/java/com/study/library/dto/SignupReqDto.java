package com.study.library.dto;

import com.study.library.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupReqDto {

    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{6,14}+$", message = "영문 숫자 조합 7~14자리여야 합니다")
    private String username;
    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}+$", message = "대소문자, 숫자, 특수문자 조합으로 8 ~ 128자리여야 합니다")
    private String password;
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣]{2,}$", message = "2글자 이상 한글로만 입력하여야 합니다")
    private String name;
    @NotBlank
    @Email
    private String email;

    // dto는 bean이 아니기 때문에 service에서 매개변수로 받는다.
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .build();
    }
}
