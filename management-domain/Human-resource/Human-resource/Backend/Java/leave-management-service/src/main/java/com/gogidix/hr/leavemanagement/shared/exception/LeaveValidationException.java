package com.gogidix.hr.leavemanagement.shared.exception;

public class LeaveValidationException extends RuntimeException {

    private String code;

    public LeaveValidationException(String message) {
        super(message);
    }

    public LeaveValidationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public LeaveValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
