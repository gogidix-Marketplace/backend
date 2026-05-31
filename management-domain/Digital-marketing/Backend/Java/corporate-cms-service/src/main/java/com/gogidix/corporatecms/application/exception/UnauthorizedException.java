package com.gogidix.corporatecms.application.exception;

/**
 * Exception thrown when a user is not authorized to perform an action.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("You are not authorized to perform this action");
    }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}