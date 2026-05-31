package com.gogidix.aiservices.timeseriesforecasting.shared.exception;

public class ForecastNotFoundException extends RuntimeException {
    public ForecastNotFoundException(String id) {
        super("Forecast not found: " + id);
    }
}
