package com.gogidix.aiservices.aireporting.shared.exception;

public class ReportException extends RuntimeException {
    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
