package com.gogidix.aiservices.predictiveanalytics.application.service;

import com.gogidix.aiservices.predictiveanalytics.application.dto.request.GenerateForecastRequest;
import com.gogidix.aiservices.predictiveanalytics.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.predictiveanalytics.application.port.out.ForecastRepository;
import com.gogidix.aiservices.predictiveanalytics.shared.exception.ForecastNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ForecastService {

    private final ForecastRepository forecastRepository;

    public ForecastService(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    public ForecastResponse generateForecast(GenerateForecastRequest request) {
        if (request.getDataSource() == null) {
            throw new IllegalArgumentException("Data source cannot be null");
        }

        // For now, return a simple response since we don't have a real forecasting implementation
        // in this service layer - it delegates to the domain layer
        ForecastResponse response = new ForecastResponse();
        response.setForecastId(UUID.randomUUID().toString());
        response.setForecasts(List.of());
        response.setConfidenceIntervals(List.of());
        response.setMetrics(Map.of("status", "processing"));

        return response;
    }

    public ForecastResponse getForecast(String forecastId) {
        // For now return a not found exception since we don't have real persistence
        throw new ForecastNotFoundException(forecastId);
    }

    private ForecastResponse toResponse(com.gogidix.aiservices.predictiveanalytics.domain.Forecast forecast) {
        ForecastResponse response = new ForecastResponse();
        response.setForecastId(forecast.getForecastId());
        response.setForecasts(List.of());
        response.setConfidenceIntervals(List.of());
        response.setMetrics(Map.of("method", forecast.getMethod().name()));
        return response;
    }
}
