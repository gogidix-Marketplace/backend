package com.gogidix.finance.exchangerate.shared.exception;

public class ExchangeRateNotFoundException extends RuntimeException {

    private String code;

    public ExchangeRateNotFoundException(String message) {
        super(message);
    }

    public ExchangeRateNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ExchangeRateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
