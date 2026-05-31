package com.gogidix.shared.warehousing.quality.domain.exception;

public class QualityCheckNotFoundException extends RuntimeException {

    public QualityCheckNotFoundException(String id) {
        super("Quality check not found: " + id);
    }
}
