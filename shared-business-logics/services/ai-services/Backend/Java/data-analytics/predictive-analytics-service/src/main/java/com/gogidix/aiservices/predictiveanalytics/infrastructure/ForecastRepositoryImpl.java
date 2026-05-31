package com.gogidix.aiservices.predictiveanalytics.infrastructure;

import com.gogidix.aiservices.predictiveanalytics.application.port.out.ForecastRepository;
import com.gogidix.aiservices.predictiveanalytics.domain.Forecast;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ForecastRepository.
 */
@Repository
public class ForecastRepositoryImpl implements ForecastRepository {

    private final Map<String, Forecast> forecasts = new ConcurrentHashMap<>();

    @Override
    public Forecast save(Forecast forecast) {
        forecasts.put(forecast.getForecastId(), forecast);
        return forecast;
    }

    @Override
    public Optional<Forecast> findById(String forecastId) {
        return Optional.ofNullable(forecasts.get(forecastId));
    }

    @Override
    public void deleteById(String forecastId) {
        forecasts.remove(forecastId);
    }
}
