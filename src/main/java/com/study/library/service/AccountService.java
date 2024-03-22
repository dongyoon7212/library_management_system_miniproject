package com.study.library.service;

import com.study.library.dto.EditPasswordReqDto;
import com.study.library.entity.User;
import com.study.library.exception.ValidException;
import com.study.library.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void editPassword(EditPasswordReqDto editPasswordReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userMapper.findUserByUsername(authentication.getName());
        if(!passwordEncoder.matches(editPasswordReqDto.getOldPassword(), user.getPassword())) { // 비밀번호가 틀린 경우
            throw new ValidException(Map.of("oldPassword", "비밀번호 인증에 실패하였습니다.\n다시 입력하세요."));
        }
        if(!editPasswordReqDto.getNewPassword().equals(editPasswordReqDto.getNewPasswordCheck())) {
            throw new ValidException(Map.of("newPasswordCheck", "새로운 비밀번호가 서로 일치 하지않습니다.\n다시 입력하세요."));
        }


    }
}
