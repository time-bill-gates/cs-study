package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Request 조작
        request.setAttribute("interceptorAttribute", "This was set in the interceptor");

        // Response 헤더 추가
        response.setHeader("X-Interceptor-Header", "Interceptor was here");
        return true; // 계속 진행
    }
}
