package com.gogidix.monitoring.servicehealthservice.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    private String code;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }
}
