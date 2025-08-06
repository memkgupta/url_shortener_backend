package com.url_shortner.user_service.controllers;

import com.url_shortner.user_service.dtos.AuthResponse;
import com.url_shortner.user_service.dtos.LoginRequest;
import com.url_shortner.user_service.services.auth.AuthService;
import com.url_shortner.user_service.services.auth.GoogleOAuthStrategy;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/oauth/callback")
@RequiredArgsConstructor
public class OAuthController {
    private final AuthService authService;
    private final GoogleOAuthStrategy googleOAuthStrategy;


    @GetMapping("/google")
    public void googleOAuthCallback(@RequestParam String code, HttpServletResponse response) throws IOException {

        AuthResponse authResponse = authService.login(LoginRequest.builder().code(code).build(), googleOAuthStrategy);
        Cookie cookie = new Cookie("accessToken", authResponse.getTokens().getAccess_token());
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setSecure(true);
        response.addCookie(cookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.getTokens().getRefresh_token());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(864000000);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);
        response.sendRedirect("http://localhost:3000/dashboard");

    }
}
