package com.gogidix.aiservices.aireporting.shared.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(String id) {
        super("Report not found: " + id);
    }
}
