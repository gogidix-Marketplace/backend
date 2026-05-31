package com.gogidix.hr.leavemanagement.shared.exception;

public class LeaveNotFoundException extends RuntimeException {

    private String code;

    public LeaveNotFoundException(String message) {
        super(message);
    }

    public LeaveNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public LeaveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
