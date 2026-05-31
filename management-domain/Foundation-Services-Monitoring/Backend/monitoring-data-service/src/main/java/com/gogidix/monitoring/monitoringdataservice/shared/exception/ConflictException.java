package com.gogidix.monitoring.monitoringdataservice.shared.exception;

public class ConflictException extends RuntimeException {

    private String code;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }
}
