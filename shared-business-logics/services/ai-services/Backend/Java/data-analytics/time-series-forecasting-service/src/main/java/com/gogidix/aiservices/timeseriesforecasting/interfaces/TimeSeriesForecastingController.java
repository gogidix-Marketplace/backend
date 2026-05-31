package com.gogidix.aiservices.timeseriesforecasting.interfaces;

import com.gogidix.aiservices.timeseriesforecasting.application.*;
import com.gogidix.aiservices.timeseriesforecasting.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for time series forecasting operations.
 */
@RestController
@RequestMapping("/api/v1/forecasting")
@CrossOrigin(origins = "*")
public class TimeSeriesForecastingController {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesForecastingController.class);

    private final TimeSeriesForecastingService forecastingService;

    public TimeSeriesForecastingController(TimeSeriesForecastingService forecastingService) {
        this.forecastingService = forecastingService;
    }

    @PostMapping("/forecasts")
    public ResponseEntity<Map<String, Object>> createForecast(
            @Valid @RequestBody CreateForecastRequest request) {
        log.info("Creating forecast: horizon={}, frequency={}", request.forecastHorizon(), request.frequency());

        // Convert DTO to domain TimeSeriesDataPoint
        List<TimeSeriesForecast.TimeSeriesDataPoint> timeSeriesData = request.timeSeriesData().stream()
                .map(dto -> new TimeSeriesForecast.TimeSeriesDataPoint(dto.timestamp(), dto.value()))
                .collect(Collectors.toList());

        TimeSeriesForecast forecast = forecastingService.createForecastFromInner(
            timeSeriesData,
            request.forecastHorizon(),
            request.frequency(),
            request.includeSeasonality()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toForecastJson(forecast));
    }

    @PostMapping("/anomalies")
    public ResponseEntity<Map<String, Object>> detectAnomalies(
            @Valid @RequestBody DetectAnomaliesRequest request) {
        log.info("Detecting anomalies in {} data points", request.timeSeriesData().size());

        // Convert DTO to domain TimeSeriesDataPoint
        List<TimeSeriesDataPoint> timeSeriesData = request.timeSeriesData().stream()
                .map(dto -> new TimeSeriesDataPoint(dto.timestamp(), dto.value()))
                .collect(Collectors.toList());

        List<AnomalyDetectionService.Anomaly> anomalies =
            forecastingService.detectAnomalies(timeSeriesData);

        return ResponseEntity.ok(Map.of(
            "anomalies", anomalies.stream()
                .map(a -> Map.of(
                    "timestamp", a.getTimestamp().toString(),
                    "value", a.getValue(),
                    "zScore", a.getZScore(),
                    "deviationPercentage", a.getDeviationPercentage(),
                    "severity", a.getSeverity().toString()
                ))
                .toList(),
            "count", anomalies.size()
        ));
    }

    @GetMapping("/forecasts/{forecastId}")
    public ResponseEntity<Map<String, Object>> getForecast(@PathVariable String forecastId) {
        log.info("Fetching forecast: {}", forecastId);

        return forecastingService.getForecast(forecastId)
            .map(forecast -> ResponseEntity.ok(toForecastJson(forecast)))
            .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    private Map<String, Object> toForecastJson(TimeSeriesForecast forecast) {
        return Map.of(
            "forecastId", forecast.getForecastId(),
            "forecasts", forecast.getForecasts().stream()
                .map(fp -> Map.of(
                    "timestamp", fp.getTimestamp().toString(),
                    "value", fp.getValue(),
                    "lowerBound", fp.getLowerBound(),
                    "upperBound", fp.getUpperBound()
                ))
                .toList(),
            "accuracyMetrics", Map.of(
                "mae", forecast.getAccuracyMetrics().getMae(),
                "rmse", forecast.getAccuracyMetrics().getRmse(),
                "mape", forecast.getAccuracyMetrics().getMape(),
                "directionAccuracy", forecast.getAccuracyMetrics().getDirectionAccuracy()
            ),
            "generatedAt", forecast.getGeneratedAt().toString()
        );
    }

    public record CreateForecastRequest(
        List<TimeSeriesDataPointDto> timeSeriesData,
        Integer forecastHorizon,
        TimeSeriesForecast.Frequency frequency,
        Boolean includeSeasonality
    ) {
        public record TimeSeriesDataPointDto(LocalDateTime timestamp, Double value) {}
    }

    public record DetectAnomaliesRequest(
        List<TimeSeriesDataPointDto> timeSeriesData
    ) {
        public record TimeSeriesDataPointDto(LocalDateTime timestamp, Double value) {}
    }
}
