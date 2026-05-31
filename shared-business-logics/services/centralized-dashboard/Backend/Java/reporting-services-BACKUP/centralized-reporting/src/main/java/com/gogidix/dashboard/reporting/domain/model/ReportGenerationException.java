package com.gogidix.dashboard.reporting.domain.model;

/**
 * Report Generation Exception
 */
public class ReportGenerationException extends RuntimeException {
    public ReportGenerationException(String message) {
        super(message);
    }

    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
