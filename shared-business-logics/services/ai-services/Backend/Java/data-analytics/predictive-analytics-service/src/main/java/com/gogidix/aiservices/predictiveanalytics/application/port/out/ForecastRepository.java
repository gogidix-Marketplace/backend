package com.gogidix.aiservices.predictiveanalytics.application.port.out;

import com.gogidix.aiservices.predictiveanalytics.domain.Forecast;

import java.util.Optional;

/**
 * Repository port for forecast results.
 */
public interface ForecastRepository {

    /**
     * Saves a forecast.
     */
    Forecast save(Forecast forecast);

    /**
     * Finds a forecast by ID.
     */
    Optional<Forecast> findById(String forecastId);

    /**
     * Deletes a forecast by ID.
     */
    void deleteById(String forecastId);
}
