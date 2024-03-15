package com.study.library.controller;

import com.study.library.aop.annotation.ValidAspect;
import com.study.library.dto.SigninReqDto;
import com.study.library.dto.SignupReqDto;
import com.study.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth") // 공통 상위 주소
public class AuthController {

    @Autowired
    private AuthService authService;

    @ValidAspect
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) { // valid체크 후 오류를 bindingResult로 받아옴

        authService.signup(signupReqDto);

        return ResponseEntity.created(null).body(true);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto) {

        return ResponseEntity.ok(authService.signin(signinReqDto)); // jwt토큰을 응답으로 줌
    }
}


/*
회원가입                                                                                          -service
react -(axios)-> security ------ (json) ->authController(signup)-------|--------@valid체크 후-()-->중복체크 ---(비밀번호 암호화)---------------> service(매개변수passwordEncoder)--(entity)-->DB
                -permitAll(통과)          -DTO => 정규식 --> @valid(여러곳에서 다쓰임) 그래서 aop           -Bcrypt.encode                        -sql 실패시 롤백(user,role)후 예외처리     -user
                                                                -->bindingresult                            -sercurityconfig bean 등록(ioc)       -toentity로 객체변환 후 sql                -ROLE
                                                                -->hasError(t/f)                            -passwordEncoder()
                                                                -->ErrorMap
      <------------------------------------------------------------리턴(errorMap)
      <-------------------------------------------------------------------------------------------중복되면 리턴
 */