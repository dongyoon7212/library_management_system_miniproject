package com.study.library.controller;

import com.study.library.aop.annotation.ParamsPrintAspect;
import com.study.library.dto.SignupReqDto;
import com.study.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth") // 공통 상위 주소
public class AuthController {

    @Autowired
    private AuthService authService;

    @ParamsPrintAspect
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) { // valid체크 후 오류를 bindingResult로 받아옴
        if(authService.isDuplicatedByUsername(signupReqDto.getUsername())) {
            ObjectError objectError = new FieldError("username", "username", "이미 존재하는 사용자 이름입니다");
            bindingResult.addError(objectError);
        }

        if(bindingResult.hasErrors()) { // 에러가 있으면
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러가 있는것들만 가져옴
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField(); // DTO 변수명
                String message = fieldError.getDefaultMessage(); // 에러 메세지
                errorMap.put(fieldName, message);
            }
            return ResponseEntity.badRequest().body(errorMap); // 에러가 있으면 응답으로 errorMap을 반환
        }

        return ResponseEntity.ok(null);
    }
}
