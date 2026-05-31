package com.gogidix.monitoring.servicehealthservice.shared.exception;

public class NotFoundException extends RuntimeException {

    private String code;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }
}
