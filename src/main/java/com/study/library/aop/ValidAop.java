package com.study.library.aop;

import com.study.library.dto.OAuth2SignupReqDto;
import com.study.library.dto.SignupReqDto;
import com.study.library.exception.ValidException;
import com.study.library.repository.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class ValidAop {

    @Autowired
    private UserMapper userMapper;

    @Pointcut("@annotation(com.study.library.aop.annotation.ValidAspect)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();

        Object[] args = proceedingJoinPoint.getArgs();

        BeanPropertyBindingResult bindingResult = null;

        for (Object arg : args) {
            if(arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg;
                System.out.println(bindingResult);
            }
        }

        if(methodName.equals("signup")) {
            SignupReqDto signupReqDto = null;

            for (Object arg : args) {
                if(arg.getClass() == SignupReqDto.class) {
                    signupReqDto = (SignupReqDto) arg;
                }
            }

            if(userMapper.findUserByUsername(signupReqDto.getUsername()) != null){
                ObjectError objectError = new FieldError("username", "username", "이미 존재하는 사용자이름 입니다.");
                bindingResult.addError(objectError);
            }
        }

        if(methodName.equals("oAuth2Signup")) {
            OAuth2SignupReqDto oAuth2SignupReqDto = null;

            for (Object arg : args) {
                if(arg.getClass() == OAuth2SignupReqDto.class) {
                    oAuth2SignupReqDto = (OAuth2SignupReqDto) arg;
                }
            }

            if(userMapper.findUserByUsername(oAuth2SignupReqDto.getUsername()) != null){
                ObjectError objectError = new FieldError("username", "username", "이미 존재하는 사용자이름 입니다.");
                bindingResult.addError(objectError);
            }
        }

        if(bindingResult.hasErrors()) { // 에러가 있으면
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러가 있는것들만 가져옴
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField(); // DTO 변수명
                String message = fieldError.getDefaultMessage(); // 에러 메세지
                errorMap.put(fieldName, message);
            }
            throw new ValidException(errorMap);
        }

        return proceedingJoinPoint.proceed();

    }
}
