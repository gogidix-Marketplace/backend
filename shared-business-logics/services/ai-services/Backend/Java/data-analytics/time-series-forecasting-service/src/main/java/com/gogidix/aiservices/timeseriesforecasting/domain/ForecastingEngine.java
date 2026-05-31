package com.gogidix.aiservices.timeseriesforecasting.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Domain service for time series forecasting.
 */
@Service
public class ForecastingEngine {

    private static final Logger log = LoggerFactory.getLogger(ForecastingEngine.class);
    private final Random random = new Random();

    /**
     * Generates time series forecasts.
     */
    public List<TimeSeriesForecast.ForecastPoint> generateForecast(
            List<TimeSeriesForecast.TimeSeriesDataPoint> timeSeriesData,
            Integer forecastHorizon,
            TimeSeriesForecast.Frequency frequency,
            Boolean includeSeasonality) {

        log.info("Generating forecast: horizon={}, frequency={}, seasonality={}",
            forecastHorizon, frequency, includeSeasonality);

        List<TimeSeriesForecast.ForecastPoint> forecasts = new ArrayList<>();

        // Calculate trend from historical data
        double lastValue = timeSeriesData.get(timeSeriesData.size() - 1).getValue();
        double trend = calculateTrend(timeSeriesData);

        LocalDateTime lastTimestamp = timeSeriesData.get(timeSeriesData.size() - 1).getTimestamp();

        // Generate forecasts
        for (int i = 1; i <= forecastHorizon; i++) {
            LocalDateTime forecastTime = addTime(lastTimestamp, i, frequency);

            double forecastValue = lastValue + (trend * i);
            if (includeSeasonality) {
                forecastValue += calculateSeasonalAdjustment(forecastTime);
            }

            // Add some noise
            forecastValue += (random.nextDouble() - 0.5) * 5;

            // Calculate confidence bounds
            double boundWidth = 10.0 + (i * 2); // Wider bounds for further forecasts
            double lowerBound = forecastValue - boundWidth;
            double upperBound = forecastValue + boundWidth;

            forecasts.add(new TimeSeriesForecast.ForecastPoint(
                forecastTime,
                Math.round(forecastValue * 100.0) / 100.0,
                Math.round(lowerBound * 100.0) / 100.0,
                Math.round(upperBound * 100.0) / 100.0
            ));
        }

        return forecasts;
    }

    /**
     * Calculates accuracy metrics.
     */
    public TimeSeriesForecast.AccuracyMetrics calculateAccuracyMetrics(
            List<TimeSeriesForecast.TimeSeriesDataPoint> timeSeriesData) {

        // Simple mock metrics calculation
        double mae = 2.0 + random.nextDouble() * 5;
        double rmse = 3.0 + random.nextDouble() * 5;
        double mape = 1.0 + random.nextDouble() * 5;
        double directionAccuracy = 0.75 + random.nextDouble() * 0.20;

        return new TimeSeriesForecast.AccuracyMetrics(
            Math.round(mae * 100.0) / 100.0,
            Math.round(rmse * 100.0) / 100.0,
            Math.round(mape * 100.0) / 100.0,
            Math.round(directionAccuracy * 100.0) / 100.0
        );
    }

    private double calculateTrend(List<TimeSeriesForecast.TimeSeriesDataPoint> data) {
        if (data.size() < 2) {
            return 0;
        }

        // Simple linear trend calculation
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = Math.min(data.size(), 50); // Use last 50 points

        for (int i = 0; i < n; i++) {
            int index = data.size() - n + i;
            double y = data.get(index).getValue();
            sumX += i;
            sumY += y;
            sumXY += i * y;
            sumX2 += i * i;
        }

        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }

    private double calculateSeasonalAdjustment(LocalDateTime timestamp) {
        // Simple seasonal pattern based on day of month
        int dayOfMonth = timestamp.getDayOfMonth();
        return Math.sin(dayOfMonth * Math.PI / 15) * 5;
    }

    private LocalDateTime addTime(LocalDateTime base, int amount, TimeSeriesForecast.Frequency frequency) {
        return switch (frequency) {
            case HOURLY -> base.plusHours(amount);
            case DAILY -> base.plusDays(amount);
            case WEEKLY -> base.plusWeeks(amount);
            case MONTHLY -> base.plusMonths(amount);
        };
    }
}
