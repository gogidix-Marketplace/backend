package com.gogidix.marketing.campaign.shared.exception;

public class AccessDeniedException extends RuntimeException {
    private String code;

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }
}
