package com.study.library.security.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class PermitAllFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<String> antMatchers = List.of("/error", "/server", "/auth");

        String uri = request.getRequestURI();
        request.setAttribute("isPermitAll", false);

        for(String antMatcher : antMatchers) {
            if(uri.startsWith(antMatcher)) { // 특정 문자열로 시작하는
                request.setAttribute("isPermitAll", true);
            }
        }

        // 전처리
        filterChain.doFilter(request, response);
        // 후처리
    }
}
