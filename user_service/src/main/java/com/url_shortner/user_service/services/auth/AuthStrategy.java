package com.url_shortner.user_service.services.auth;

import com.url_shortner.user_service.dtos.AuthResponse;
import com.url_shortner.user_service.dtos.LoginRequest;
import com.url_shortner.user_service.dtos.SignUpRequest;

public interface AuthStrategy {
     AuthResponse login(LoginRequest loginRequest);
     AuthResponse signup(SignUpRequest signUpRequest);
}
