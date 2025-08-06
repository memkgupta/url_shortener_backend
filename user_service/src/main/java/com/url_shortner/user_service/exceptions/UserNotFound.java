package com.url_shortner.user_service.exceptions;

public class UserNotFound extends APIException {
    public UserNotFound(String field,String value) {
        super("User with " + field+" "+value+ " not found");
        status=404;
    }
}