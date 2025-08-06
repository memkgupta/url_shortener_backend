package com.url_shortner.user_service.services.auth;

import com.url_shortner.user_service.entities.Auth;
import com.url_shortner.user_service.entities.User;
import com.url_shortner.user_service.repositories.AuthRepository;
import com.url_shortner.user_service.repositories.TokenRepository;
import com.url_shortner.user_service.repositories.UserRepository;
import com.url_shortner.user_service.services.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Component
public class GoogleOAuthStrategy extends OAuthStrategy {



    public GoogleOAuthStrategy(
            RestTemplate restTemplate,
            AuthRepository authRepository,
            JWTService jwtService,
            TokenRepository tokenRepository,
            UserRepository userRepository,
            @Value("${oauth.client.id}") String clientId,
            @Value("${oauth.client.secret}") String clientSecret
    ){
        super(restTemplate, authRepository, jwtService, tokenRepository, userRepository);
        this.tokenEndPoint = "https://oauth2.googleapis.com/token";
        this.userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=";

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = "http://localhost:8080/v1/oauth/callback/google";
        this.grantType = "authorization_code";
    }

    @Override
    User extractUser(ResponseEntity<Map> userInfo) {
        Map<String, Object> body = userInfo.getBody();
        Auth auth = authRepository.findByEmail((String) body.get("email"));
        if (auth == null) {
            User user = new User();
            user.setName((String) body.get("name"));
            user.setEmail((String) body.get("email"));
            user.setProfile((String) body.get("picture"));
            user.setRole("USER");
            userRepository.save(user);
            return user;
        }
        else{
            return auth.getUser();
        }

    }

    @Override
    String extractProviderId(ResponseEntity<Map> userInfo) {
        return (String) userInfo.getBody().get("sub");
    }


}
