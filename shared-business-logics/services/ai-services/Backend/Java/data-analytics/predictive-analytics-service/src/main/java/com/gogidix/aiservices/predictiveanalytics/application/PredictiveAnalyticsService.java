package com.gogidix.aiservices.predictiveanalytics.application;

import com.gogidix.aiservices.predictiveanalytics.domain.*;
import com.gogidix.aiservices.predictiveanalytics.application.port.out.ForecastRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Application service for predictive analytics operations.
 */
@Service
public class PredictiveAnalyticsService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PredictiveAnalyticsService.class);

    private final ForecastRepository forecastRepository;
    private final ForecastingEngine forecastingEngine;

    public PredictiveAnalyticsService(ForecastRepository forecastRepository, ForecastingEngine forecastingEngine) {
        this.forecastRepository = forecastRepository;
        this.forecastingEngine = forecastingEngine;
    }

    public Forecast generateForecast(String dataSource, String targetField,
                                    Integer horizon, Forecast.ForecastMethod method) {
        log.info("Generating forecast: dataSource={}, field={}, horizon={}, method={}",
            dataSource, targetField, horizon, method);

        // Generate forecast data
        List<Forecast.ForecastDataPoint> forecastData = forecastingEngine.generateForecast(
            dataSource, targetField, horizon, method
        );

        // Generate confidence intervals
        List<Forecast.ConfidenceInterval> intervals = forecastingEngine.generateConfidenceIntervals(
            forecastData, 0.95
        );

        // Calculate metrics
        Forecast.ForecastMetrics metrics = forecastingEngine.calculateMetrics();

        Forecast forecast = new Forecast(
            UUID.randomUUID().toString(),
            dataSource,
            targetField,
            method,
            horizon,
            forecastData,
            intervals,
            metrics,
            LocalDateTime.now()
        );

        forecastRepository.save(forecast);
        return forecast;
    }

    public TrendAnalysisService.TrendResult analyzeTrends(String dataSource, String targetField) {
        log.info("Analyzing trends: dataSource={}, field={}", dataSource, targetField);

        Map<String, Object> data = forecastingEngine.fetchHistoricalData(dataSource);
        return forecastingEngine.analyzeTrends(data, targetField);
    }

    public Optional<Forecast> getForecast(String forecastId) {
        return forecastRepository.findById(forecastId);
    }
}
