package com.gogidix.globalbusinessmanagement.countryingestion.shared.exception;

public class IngestionException extends RuntimeException {

    private String code;

    public IngestionException(String message) {
        super(message);
    }

    public IngestionException(String message, String code) {
        super(message);
        this.code = code;
    }

    public IngestionException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
