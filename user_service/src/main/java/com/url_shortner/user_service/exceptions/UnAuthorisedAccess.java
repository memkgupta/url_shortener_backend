package com.url_shortner.user_service.exceptions;

public class UnAuthorisedAccess extends APIException {
    public UnAuthorisedAccess(String resourceName , String resourceId) {
        super("Unauthorised access to "+resourceName +"with id "+resourceId);
        status=401;
    }
}