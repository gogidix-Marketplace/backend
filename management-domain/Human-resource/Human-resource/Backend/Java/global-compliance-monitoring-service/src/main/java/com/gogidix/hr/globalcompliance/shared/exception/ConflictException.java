package com.gogidix.hr.globalcompliance.shared.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
    public ConflictException(String message, Throwable cause) { super(message, cause); }
    public ConflictException(String resource, String identifier) { super(resource + " already exists: " + identifier); }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}