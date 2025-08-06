package com.url_shortner.user_service.services.auth;

import com.url_shortner.user_service.dtos.AuthResponse;
import com.url_shortner.user_service.dtos.LoginRequest;
import com.url_shortner.user_service.dtos.SignUpRequest;
import com.url_shortner.user_service.dtos.TokenDTO;
import com.url_shortner.user_service.entities.*;
import com.url_shortner.user_service.exceptions.APIExceptionFactory;
import com.url_shortner.user_service.exceptions.ExceptionTypeEnum;
import com.url_shortner.user_service.repositories.AuthRepository;
import com.url_shortner.user_service.repositories.TokenRepository;
import com.url_shortner.user_service.repositories.UserRepository;
import com.url_shortner.user_service.services.JWTService;
import com.url_shortner.user_service.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
@Component
@RequiredArgsConstructor
public class EmailAuthStrategy implements AuthStrategy {
private UserRepository userRepository;
private final UserService userService;
private final APIExceptionFactory exceptionFactory;
private final AuthRepository authRepository;
private final PasswordEncoder passwordEncoder;
private final TokenRepository tokenRepository;
private final JWTService jwtService;
    @Override
    public AuthResponse signup(SignUpRequest signUpRequest) {
        User previousUser = userService.getUserByEmail(signUpRequest.getEmail());
        if(previousUser != null) {
            throw exceptionFactory.throwException(ExceptionTypeEnum.USER_ALREADY_EXISTS,"email", previousUser.getEmail());

        }
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setName(signUpRequest.getName());

        userService.create(user);
        Auth auth = new Auth();
        auth.setAuthType(AuthTypeEnum.EMAIL);
        auth.setEmail(user.getEmail());
        auth.setCreated_at(new Timestamp(System.currentTimeMillis()));
        auth.setUser(user);
        authRepository.save(auth);
        String access_token = jwtService.generateToken(user.getEmail());
        String refresh_token = jwtService.generateToken(user.getEmail(),new Date(System.currentTimeMillis() + 1000L *60*60*24*30));
        Token refreshTokenEntity = new Token();
        refreshTokenEntity.setAuth(auth);
        refreshTokenEntity.setToken(refresh_token);
        refreshTokenEntity.setType(TokenType.REFRESH_TOKEN);
        refreshTokenEntity.setExpires(new Timestamp(System.currentTimeMillis() + 1000L *60*60*24*30));
        refreshTokenEntity.setCreated(new Timestamp(System.currentTimeMillis()));
        tokenRepository.save(refreshTokenEntity);

        return AuthResponse.builder()
                .tokens(TokenDTO.builder().access_token(access_token).refresh_token(refresh_token).build())
                .name(user.getName())
                .email(user.getEmail())
                .profile(user.getProfile())
                .build();

    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {




        Auth auth = authRepository.findByEmail(loginRequest.getEmail());
        if(auth == null) {
            throw exceptionFactory.throwException(ExceptionTypeEnum.BAD_CREDENTIALS,null,null);
        }
        User user = auth.getUser();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw exceptionFactory.throwException(ExceptionTypeEnum.BAD_CREDENTIALS, "email", loginRequest.getEmail());
        }
        String access_token = jwtService.generateToken(user.getEmail());
        String refresh_token = jwtService.generateToken(user.getEmail());

        Token refreshTokenEntity = new Token();
        refreshTokenEntity.setAuth(auth);
        refreshTokenEntity.setToken(refresh_token);
        refreshTokenEntity.setType(TokenType.REFRESH_TOKEN);
        refreshTokenEntity.setExpires(new Timestamp(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30));
        refreshTokenEntity.setCreated(new Timestamp(System.currentTimeMillis()));
        tokenRepository.save(refreshTokenEntity);

        return AuthResponse.builder()
                .tokens(TokenDTO.builder().access_token(access_token).refresh_token(refresh_token).build())
                .name(user.getName())
                .email(user.getEmail())
                .profile(user.getProfile())
                .build();
    }
}
