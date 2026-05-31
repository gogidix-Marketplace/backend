package com.gogidix.aiservices.aidatavalidation.application.port.out;

public class DataSourceAdapterException extends RuntimeException {
    public DataSourceAdapterException(String message) {
        super(message);
    }

    public DataSourceAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
