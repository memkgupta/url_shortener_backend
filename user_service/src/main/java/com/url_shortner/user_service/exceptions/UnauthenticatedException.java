package com.url_shortner.user_service.exceptions;

public class UnauthenticatedException extends APIException {
    public UnauthenticatedException(String message) {
        super(message);
        status=401;
    }
}
