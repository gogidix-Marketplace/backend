package com.gogidix.aiservices.timeseriesforecasting.application;

import com.gogidix.aiservices.timeseriesforecasting.domain.*;
import com.gogidix.aiservices.timeseriesforecasting.application.port.out.ForecastRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application service for time series forecasting operations.
 */
@Service
public class TimeSeriesForecastingService {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesForecastingService.class);

    private final ForecastRepository forecastRepository;
    private final ForecastingEngine forecastingEngine;
    private final AnomalyDetectionService anomalyDetectionService;

    public TimeSeriesForecastingService(ForecastRepository forecastRepository,
                                        ForecastingEngine forecastingEngine,
                                        AnomalyDetectionService anomalyDetectionService) {
        this.forecastRepository = forecastRepository;
        this.forecastingEngine = forecastingEngine;
        this.anomalyDetectionService = anomalyDetectionService;
    }

    public TimeSeriesForecast createForecast(
            List<TimeSeriesDataPoint> timeSeriesData,
            Integer forecastHorizon,
            TimeSeriesForecast.Frequency frequency,
            Boolean includeSeasonality) {
        log.info("Creating forecast: horizon={}, frequency={}", forecastHorizon, frequency);

        // Convert to inner TimeSeriesDataPoint for the forecast entity
        List<TimeSeriesForecast.TimeSeriesDataPoint> innerTimeSeriesData = timeSeriesData.stream()
            .map(dp -> new TimeSeriesForecast.TimeSeriesDataPoint(dp.getTimestamp(), dp.getValue()))
            .toList();

        return createForecastFromInner(innerTimeSeriesData, forecastHorizon, frequency, includeSeasonality);
    }

    public TimeSeriesForecast createForecastFromInner(
            List<TimeSeriesForecast.TimeSeriesDataPoint> timeSeriesData,
            Integer forecastHorizon,
            TimeSeriesForecast.Frequency frequency,
            Boolean includeSeasonality) {
        log.info("Creating forecast: horizon={}, frequency={}", forecastHorizon, frequency);

        // Generate forecast
        List<TimeSeriesForecast.ForecastPoint> forecasts =
            forecastingEngine.generateForecast(timeSeriesData, forecastHorizon, frequency, includeSeasonality);

        // Calculate accuracy metrics
        TimeSeriesForecast.AccuracyMetrics metrics =
            forecastingEngine.calculateAccuracyMetrics(timeSeriesData);

        TimeSeriesForecast forecast = new TimeSeriesForecast(
            UUID.randomUUID().toString(),
            timeSeriesData,
            forecastHorizon,
            frequency,
            includeSeasonality,
            forecasts,
            metrics,
            LocalDateTime.now()
        );

        forecastRepository.save(forecast);
        return forecast;
    }

    public List<AnomalyDetectionService.Anomaly> detectAnomalies(
            List<TimeSeriesDataPoint> timeSeriesData) {
        log.info("Detecting anomalies in {} data points", timeSeriesData.size());
        return anomalyDetectionService.detectAnomalies(timeSeriesData);
    }

    public Optional<TimeSeriesForecast> getForecast(String forecastId) {
        return forecastRepository.findById(forecastId);
    }
}
