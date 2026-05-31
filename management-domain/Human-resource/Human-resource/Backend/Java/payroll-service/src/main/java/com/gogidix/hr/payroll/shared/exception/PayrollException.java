package com.gogidix.hr.payroll.shared.exception;

public class PayrollException extends RuntimeException {

    private String code;

    public PayrollException(String message) {
        super(message);
    }

    public PayrollException(String message, String code) {
        super(message);
        this.code = code;
    }

    public PayrollException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
