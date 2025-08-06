package com.url_shortner.user_service.exceptions;

public class InternalServerError extends APIException{
    public InternalServerError(String message) {
        super(message);
        status=500;
    }
}