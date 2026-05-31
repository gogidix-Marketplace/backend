package com.gogidix.marketing.campaign.shared.exception;

public class TenantNotActiveException extends RuntimeException {
    private String code;

    public TenantNotActiveException(String message) {
        super(message);
    }

    public TenantNotActiveException(String message, String code) {
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
