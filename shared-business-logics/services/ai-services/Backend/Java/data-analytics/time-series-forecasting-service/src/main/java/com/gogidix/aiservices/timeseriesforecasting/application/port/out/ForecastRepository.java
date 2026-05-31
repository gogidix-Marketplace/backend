package com.gogidix.aiservices.timeseriesforecasting.application.port.out;

import com.gogidix.aiservices.timeseriesforecasting.domain.TimeSeriesForecast;

import java.util.Optional;

/**
 * Repository port for time series forecast results.
 */
public interface ForecastRepository {

    /**
     * Saves a forecast.
     */
    TimeSeriesForecast save(TimeSeriesForecast forecast);

    /**
     * Finds a forecast by ID.
     */
    Optional<TimeSeriesForecast> findById(String forecastId);

    /**
     * Deletes a forecast by ID.
     */
    void deleteById(String forecastId);
}
