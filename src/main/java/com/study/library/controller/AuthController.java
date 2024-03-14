package com.study.library.controller;

import com.study.library.aop.annotation.ParamsPrintAspect;
import com.study.library.dto.SignupReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth") // 공통 상위 주소
public class AuthController {

    @ParamsPrintAspect
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) { // valid체크 후 오류를 bindingResult로 받아옴

        if(bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                System.out.println(fieldName);
                System.out.println(message);
            }
        }

        return ResponseEntity.ok(null);
    }
}
