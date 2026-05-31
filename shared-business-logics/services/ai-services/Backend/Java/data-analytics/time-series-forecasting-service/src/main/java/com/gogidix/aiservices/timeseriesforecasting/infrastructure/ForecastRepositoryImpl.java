package com.gogidix.aiservices.timeseriesforecasting.infrastructure;

import com.gogidix.aiservices.timeseriesforecasting.application.port.out.ForecastRepository;
import com.gogidix.aiservices.timeseriesforecasting.domain.TimeSeriesForecast;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ForecastRepository.
 */
@Repository
public class ForecastRepositoryImpl implements ForecastRepository {

    private final Map<String, TimeSeriesForecast> forecasts = new ConcurrentHashMap<>();

    @Override
    public TimeSeriesForecast save(TimeSeriesForecast forecast) {
        forecasts.put(forecast.getForecastId(), forecast);
        return forecast;
    }

    @Override
    public Optional<TimeSeriesForecast> findById(String forecastId) {
        return Optional.ofNullable(forecasts.get(forecastId));
    }

    @Override
    public void deleteById(String forecastId) {
        forecasts.remove(forecastId);
    }
}
