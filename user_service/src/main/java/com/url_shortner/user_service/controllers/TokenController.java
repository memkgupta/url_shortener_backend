package com.url_shortner.user_service.controllers;

import com.url_shortner.user_service.dtos.AuthResponse;
import com.url_shortner.user_service.dtos.TokenDTO;
import com.url_shortner.user_service.entities.Token;
import com.url_shortner.user_service.entities.TokenType;
import com.url_shortner.user_service.entities.User;
import com.url_shortner.user_service.repositories.TokenRepository;
import com.url_shortner.user_service.services.JWTService;
import com.url_shortner.user_service.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final UserService userService;

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        refreshToken = refreshToken.substring(7); // remove "Bearer "

        Token token = tokenRepository.findByTokenAndType(refreshToken, TokenType.REFRESH_TOKEN);
        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        User user = token.getAuth().getUser();
        String newAccessToken = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(AuthResponse.builder()
                        .tokens(TokenDTO.builder().access_token(newAccessToken).refresh_token(refreshToken).build())
                .email(user.getEmail())
                .name(user.getName())
                .profile(user.getProfile())
                .build());
    }
}