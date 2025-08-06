package com.url_shortner.user_service.exceptions;

public class UserAlreadyExists extends APIException{
    public UserAlreadyExists(String email) {
        super("User already exists with email "+email);
        status=409;
    }
}