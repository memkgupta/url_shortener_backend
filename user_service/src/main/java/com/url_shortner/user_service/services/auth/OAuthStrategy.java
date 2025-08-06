package com.url_shortner.user_service.services.auth;

import com.url_shortner.user_service.dtos.AuthResponse;
import com.url_shortner.user_service.dtos.LoginRequest;
import com.url_shortner.user_service.dtos.SignUpRequest;
import com.url_shortner.user_service.dtos.TokenDTO;
import com.url_shortner.user_service.entities.*;
import com.url_shortner.user_service.repositories.AuthRepository;
import com.url_shortner.user_service.repositories.TokenRepository;
import com.url_shortner.user_service.repositories.UserRepository;
import com.url_shortner.user_service.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Map;

@RequiredArgsConstructor
public abstract class OAuthStrategy implements AuthStrategy {
    protected final RestTemplate restTemplate;
    protected final AuthRepository authRepository;
    protected final JWTService jwtService;
    protected final TokenRepository tokenRepository;
    protected final UserRepository userRepository;
    protected String tokenEndPoint;
    protected String clientId;
    protected String clientSecret;
    protected String userInfoEndpoint;
    protected String redirectUri;
    protected String provider;
    protected String grantType;

    private Map<String,String> exchangeToken(String code)
    {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("grant_type", grantType!=null?grantType:"authorization_code");
        params.add("client_id", clientId);

        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> tokenRequest = new HttpEntity<>(params,headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndPoint, tokenRequest, Map.class);

        return tokenResponse.getBody();

    }
    private ResponseEntity<Map> fetchUserInfo(String token)
    {

        return restTemplate.getForEntity(userInfoEndpoint+token, Map.class);
    }

    abstract User extractUser(ResponseEntity<Map> userInfo); // this will extract and save user if not exist
    abstract String extractProviderId(ResponseEntity<Map> userInfo);
    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        String token = exchangeToken(loginRequest.getCode()).get("access_token").toString();
        ResponseEntity<Map> userInfo = fetchUserInfo(token);

        User user = extractUser(userInfo);
        Auth auth = new Auth();
        auth.setAuthType(AuthTypeEnum.OAUTH);
        auth.setOAuthProvider(provider);
        auth.setProvider_id(extractProviderId(userInfo));
        auth.setEmail(user.getEmail());
        auth.setCreated_at(new Timestamp(System.currentTimeMillis()));
        auth.setUser(user);
        authRepository.save(auth);
        String access_token = jwtService.generateToken(user.getEmail());
        String refresh_token = jwtService.generateToken(user.getEmail());
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
    public AuthResponse signup(SignUpRequest signUpRequest) {
        return null;
    }
}
