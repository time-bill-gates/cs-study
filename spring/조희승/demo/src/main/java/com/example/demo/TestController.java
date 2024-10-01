package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        String interceptorAttribute = (String) request.getAttribute("interceptorAttribute");
        String interceptorHeader = response.getHeader("X-Interceptor-Header");

        return "Interceptor attribute: " + interceptorAttribute +
                ", Interceptor header: " + interceptorHeader;
    }
}
