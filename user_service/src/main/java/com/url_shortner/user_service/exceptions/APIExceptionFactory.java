package com.url_shortner.user_service.exceptions;

import org.springframework.stereotype.Component;
@Component
public class APIExceptionFactory {

    public APIException throwException(ExceptionTypeEnum type , String field , String value)
    {
        return switch (type){
            case USER_NOT_FOUND ->new UserNotFound(field,value);

            case INTERNAL_SERVER_ERROR -> new InternalServerError(value);
            case BAD_REQUEST -> new BadRequestException(value);
            case BAD_CREDENTIALS -> new BadCredentialsException();
            case UNAUTHORIZED -> new UnAuthorisedAccess(field,value);
            case USER_ALREADY_EXISTS -> new UserAlreadyExists(value);
        };
    }
}
