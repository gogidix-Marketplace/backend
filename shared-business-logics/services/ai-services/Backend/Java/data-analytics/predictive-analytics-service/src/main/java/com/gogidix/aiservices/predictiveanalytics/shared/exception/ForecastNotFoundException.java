package com.gogidix.aiservices.predictiveanalytics.shared.exception;

public class ForecastNotFoundException extends RuntimeException {
    public ForecastNotFoundException(String id) {
        super("Forecast not found: " + id);
    }
}
