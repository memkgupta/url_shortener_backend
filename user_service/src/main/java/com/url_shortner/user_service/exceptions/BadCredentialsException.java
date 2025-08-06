package com.url_shortner.user_service.exceptions;

public class BadCredentialsException extends APIException{
    public BadCredentialsException() {
        super("Invalid username or password");
        status = 401;
    }
}
