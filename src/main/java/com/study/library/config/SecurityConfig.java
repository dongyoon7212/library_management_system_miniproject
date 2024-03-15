package com.study.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 5. 밑의 설정값을 적용
@Configuration // 1. ioc에 등록하기
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 2. WebSecurityConfigurerAdapter 상속을 받아온다.

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { //bean 등록
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 3. HttpSecurity 오버라이드 한다.
        http.csrf().disable(); // csrf(토큰인증방식) 하지만 SSR방식이기때문에 CSR인 프로젝트는 쓰지 않는다. => 기본설정
        http.authorizeRequests()
                .antMatchers("/server/**", "/auth/**") // 경로 설정
                .permitAll() // 모두 허용
                .anyRequest() // 설정한 경로를 제외하고
                .authenticated(); // 인증 해야함

        // 4. http에 해당 설정을 함
    }
}
