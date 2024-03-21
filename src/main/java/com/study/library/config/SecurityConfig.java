package com.study.library.config;

import com.study.library.security.exception.AuthEntryPoint;
import com.study.library.security.filter.JwtAuthenticationFilter;
import com.study.library.security.filter.PermitAllFilter;
import com.study.library.security.handler.OAuth2SuccessHandler;
import com.study.library.service.OAuth2PrincipalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity // 5. 밑의 설정값을 적용
@Configuration // 1. ioc에 등록하기
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 2. WebSecurityConfigurerAdapter 상속을 받아온다.

    @Autowired
    private PermitAllFilter permitAllFilter;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private OAuth2PrincipalUserService oAuth2PrincipalUserService;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { //bean 등록
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 3. HttpSecurity 오버라이드 한다.
        http.cors(); // cross origin 세팅 => WebMvcConfig에 설정한 값을 따라감
        http.csrf().disable(); // csrf(토큰인증방식) 하지만 SSR방식이기때문에 CSR인 프로젝트는 쓰지 않는다. => 기본설정
        http.authorizeRequests()
                .antMatchers("/server/**", "/auth/**") // 경로 설정
                .permitAll() // 모두 허용
                .antMatchers("/mail/authenticate")
                .permitAll()
                .anyRequest() // 설정한 경로를 제외하고
                .authenticated() // 인증 해야함
                .and()
                .addFilterAfter(permitAllFilter, LogoutFilter.class) // LogoutFilter후에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter 전에 추가
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint) //entrypoint가 없으면 403 있으면 401 설정을 해둬야함
                .and()
                .oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint() // OAuth2로그인 토큰 검사
                .userService(oAuth2PrincipalUserService);
        // 4. http에 해당 설정을 함
    }
}
