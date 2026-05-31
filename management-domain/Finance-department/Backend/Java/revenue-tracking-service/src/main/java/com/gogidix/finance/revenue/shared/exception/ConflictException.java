package com.gogidix.finance.revenue.shared.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
    public ConflictException(String resource, String id) {
        super(resource + " with id '" + id + "' already exists");
    }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}