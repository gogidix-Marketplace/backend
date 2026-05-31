package com.gogidix.aiservices.timeseriesforecasting.application.service;

import com.gogidix.aiservices.timeseriesforecasting.application.dto.request.CreateForecastRequest;
import com.gogidix.aiservices.timeseriesforecasting.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.timeseriesforecasting.domain.model.Frequency;
import com.gogidix.aiservices.timeseriesforecasting.domain.model.TimeSeriesForecast;
import com.gogidix.aiservices.timeseriesforecasting.domain.port.out.TimeSeriesForecastRepository;
import com.gogidix.aiservices.timeseriesforecasting.shared.exception.ForecastNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForecastingService {

    private final TimeSeriesForecastRepository forecastRepository;

    public ForecastingService(TimeSeriesForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    public ForecastResponse createForecast(CreateForecastRequest request) {
        if (request.getTimeSeriesData() == null || request.getTimeSeriesData().isEmpty()) {
            throw new IllegalArgumentException("Time series data cannot be null or empty");
        }

        List<TimeSeriesForecast.TimeSeriesPoint> dataPoints = request.getTimeSeriesData().stream()
                .map(p -> new TimeSeriesForecast.TimeSeriesPoint(p.getTimestamp(), p.getValue()))
                .collect(Collectors.toList());

        Frequency frequency = request.getFrequency() != null ? request.getFrequency() : Frequency.DAILY;
        int horizon = request.getForecastHorizon() != null ? request.getForecastHorizon() : 30;

        TimeSeriesForecast forecast;
        if (Boolean.TRUE.equals(request.getIncludeSeasonality())) {
            forecast = TimeSeriesForecast.create(dataPoints, horizon, frequency).withSeasonality(true);
        } else {
            forecast = TimeSeriesForecast.create(dataPoints, horizon, frequency);
        }

        forecast.complete(List.of());

        TimeSeriesForecast saved = forecastRepository.save(forecast);
        return toResponse(saved);
    }

    public ForecastResponse getForecast(String forecastId) {
        TimeSeriesForecast forecast = forecastRepository.findById(forecastId)
                .orElseThrow(() -> new ForecastNotFoundException(forecastId));
        return toResponse(forecast);
    }

    private ForecastResponse toResponse(TimeSeriesForecast forecast) {
        return ForecastResponse.builder()
                .forecastId(forecast.getForecastId())
                .forecasts(List.of())
                .accuracyMetrics(Map.of("mae", 0.0, "rmse", 0.0))
                .build();
    }
}
