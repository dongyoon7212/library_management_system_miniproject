package com.study.library.dto;

import lombok.Data;

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
}
