package com.gogidix.finance.revenue.shared.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String resource, String id) {
        super(resource + " with id '" + id + "' not found");
    }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}