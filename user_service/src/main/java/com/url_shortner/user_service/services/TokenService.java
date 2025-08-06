package com.url_shortner.user_service.services;

import com.url_shortner.user_service.entities.Auth;
import com.url_shortner.user_service.entities.Token;
import com.url_shortner.user_service.exceptions.APIExceptionFactory;
import com.url_shortner.user_service.exceptions.ExceptionTypeEnum;
import com.url_shortner.user_service.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final APIExceptionFactory exceptionFactory;
    //    public boolean isTokenValid
    public String refreshToken(String token){

        Token t= tokenRepository.findByToken(token);

        if(t==null){
            throw exceptionFactory.throwException(ExceptionTypeEnum.BAD_REQUEST,null,"token is invalid");
        }
        if(t.getExpires().before(new Date(Instant.now().toEpochMilli()))){
            throw exceptionFactory.throwException(ExceptionTypeEnum.BAD_REQUEST,null,"token is expired");
        }

        return jwtService.generateToken(t.getAuth().getEmail());


    }

    public String generateToken(Auth auth) {
        String token = jwtService.generateToken(auth.getEmail());
        Token t= new Token();

        t.setToken(token);
        t.setExpires(new Timestamp(System.currentTimeMillis()+1000*60*60*24));
        t.setCreated(new Timestamp(System.currentTimeMillis()));

        return tokenRepository.save(t).getToken();
    }
}