package com.gogidix.monitoring.monitoringdataservice.shared.exception;

public class BaseDomainException extends RuntimeException {

    private String code;

    public BaseDomainException(String message) {
        super(message);
    }

    public BaseDomainException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BaseDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }
}
