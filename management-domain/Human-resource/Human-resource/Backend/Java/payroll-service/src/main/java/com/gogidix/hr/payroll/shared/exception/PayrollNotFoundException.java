package com.gogidix.hr.payroll.shared.exception;

public class PayrollNotFoundException extends RuntimeException {

    private String code;

    public PayrollNotFoundException(String message) {
        super(message);
    }

    public PayrollNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public PayrollNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
