package com.study.library.controller;

import com.study.library.aop.annotation.ParamsPrintAspect;
import com.study.library.dto.SignupReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth") // 공통 상위 주소
public class AuthController {

    @ParamsPrintAspect
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) { // valid체크 후 오류를 bindingResult로 받아옴

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
