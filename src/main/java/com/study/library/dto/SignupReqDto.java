package com.study.library.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class SignupReqDto {

    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{3,10}+$", message = "영문 숫자 조합 6~10자리여야 합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}+$", message = "대소문자, 숫자, 특수문자 조합으로 8 ~ 128자리여야 합니다.")
    private String password;
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣]*$", message = "한글로만 입력하여야 합니다.")
    private String name;
    @Email
    private String email;
}
