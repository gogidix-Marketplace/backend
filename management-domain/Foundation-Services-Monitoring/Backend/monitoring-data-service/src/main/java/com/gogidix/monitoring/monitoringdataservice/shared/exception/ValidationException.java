package com.gogidix.monitoring.monitoringdataservice.shared.exception;

public class ValidationException extends RuntimeException {

    private String code;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }
}
