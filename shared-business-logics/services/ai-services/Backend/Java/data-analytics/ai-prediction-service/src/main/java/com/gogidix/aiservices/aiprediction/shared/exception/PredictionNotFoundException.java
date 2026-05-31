package com.gogidix.aiservices.aiprediction.shared.exception;

public class PredictionNotFoundException extends RuntimeException {
    public PredictionNotFoundException(String id) {
        super("Prediction not found: " + id);
    }
}
