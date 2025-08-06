package com.url_shortner.user_service.services.auth;

import com.url_shortner.user_service.config.UserDetailsImpl;
import com.url_shortner.user_service.dtos.AuthResponse;
import com.url_shortner.user_service.dtos.LoginRequest;
import com.url_shortner.user_service.dtos.SignUpRequest;
import com.url_shortner.user_service.entities.User;
import com.url_shortner.user_service.exceptions.UnauthenticatedException;
import com.url_shortner.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
 private final UserRepository userRepository;

    public AuthResponse login(LoginRequest loginRequest, AuthStrategy strategy) {
        return strategy.login(loginRequest);
    }

    public AuthResponse signup(SignUpRequest signUpRequest, AuthStrategy authStrategy) {
       return authStrategy.signup(signUpRequest);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedException("Unauthenticated");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername());
    }
}
