package com.gogidix.aiservices.aidatavalidation.application.port.out;

public class DataSourceNotFoundException extends RuntimeException {
    public DataSourceNotFoundException(String message) {
        super(message);
    }
}
