package com.gogidix.aiservices.aiprediction.domain;

public class PredictionTimeoutException extends RuntimeException {
    public PredictionTimeoutException(String message) {
        super(message);
    }

    public PredictionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
