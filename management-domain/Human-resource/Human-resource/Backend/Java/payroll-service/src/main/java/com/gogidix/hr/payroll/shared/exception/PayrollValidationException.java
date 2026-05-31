package com.gogidix.hr.payroll.shared.exception;

public class PayrollValidationException extends RuntimeException {

    private String code;

    public PayrollValidationException(String message) {
        super(message);
    }

    public PayrollValidationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public PayrollValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
