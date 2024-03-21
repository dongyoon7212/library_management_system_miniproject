package com.study.library.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String name = authentication.getName();

        // OAuth2 로그인을 통해 회원가입을 진행한 기록이 있는지 (연동이 된 경우)

        // OAuth2 로그인을 통해 회원가입이 되어있지 않은 상태 (연동이 된적이 없는 경우)

        // OAuth2 동기화
    }
}
