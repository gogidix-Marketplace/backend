package com.gogidix.aiservices.timeseriesforecasting.domain.port.out;

import com.gogidix.aiservices.timeseriesforecasting.domain.model.TimeSeriesForecast;

import java.util.Optional;

public interface TimeSeriesForecastRepository {
    TimeSeriesForecast save(TimeSeriesForecast forecast);
    Optional<TimeSeriesForecast> findById(String id);
    void delete(String id);
}
