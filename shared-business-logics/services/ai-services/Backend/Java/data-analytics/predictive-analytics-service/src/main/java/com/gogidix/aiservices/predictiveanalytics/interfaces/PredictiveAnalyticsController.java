package com.gogidix.aiservices.predictiveanalytics.interfaces;

import com.gogidix.aiservices.predictiveanalytics.application.*;
import com.gogidix.aiservices.predictiveanalytics.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller for predictive analytics operations.
 */
@RestController
@RequestMapping("/api/v1/predictive")
@CrossOrigin(origins = "*")
public class PredictiveAnalyticsController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PredictiveAnalyticsController.class);

    private final PredictiveAnalyticsService analyticsService;

    public PredictiveAnalyticsController(PredictiveAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/forecasts")
    public ResponseEntity<Map<String, Object>> generateForecast(
            @Valid @RequestBody GenerateForecastRequestRecord request) {
        log.info("Generating forecast: dataSource={}, horizon={}", request.dataSource(), request.horizon());

        Forecast forecast = analyticsService.generateForecast(
            request.dataSource(),
            request.targetField(),
            request.horizon(),
            request.method()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toForecastJson(forecast));
    }

    @PostMapping("/trends")
    public ResponseEntity<Map<String, Object>> analyzeTrends(
            @Valid @RequestBody AnalyzeTrendsRequestRecord request) {
        log.info("Analyzing trends: dataSource={}", request.dataSource());

        TrendAnalysisService.TrendResult result = analyticsService.analyzeTrends(
            request.dataSource(),
            request.targetField()
        );

        return ResponseEntity.ok(Map.of(
            "targetField", result.getField(),
            "direction", result.getDirection().toString(),
            "strength", result.getStrength(),
            "seasonality", result.getSeasonality(),
            "slope", result.getSlope(),
            "confidenceLevel", result.getConfidenceLevel()
        ));
    }

    @GetMapping("/forecasts/{forecastId}")
    public ResponseEntity<Map<String, Object>> getForecast(@PathVariable String forecastId) {
        log.info("Fetching forecast: {}", forecastId);

        return analyticsService.getForecast(forecastId)
            .map(forecast -> ResponseEntity.ok(toForecastJson(forecast)))
            .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(InsufficientDataException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientData(
            InsufficientDataException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    private Map<String, Object> toForecastJson(Forecast forecast) {
        return Map.of(
            "forecastId", forecast.getForecastId(),
            "dataSource", forecast.getDataSource(),
            "targetField", forecast.getTargetField(),
            "method", forecast.getMethod().toString(),
            "horizon", forecast.getHorizon(),
            "forecastData", forecast.getForecastData().stream()
                .map(dp -> Map.of(
                    "timestamp", dp.getTimestamp().toString(),
                    "value", dp.getValue()
                ))
                .toList(),
            "confidenceIntervals", forecast.getConfidenceIntervals().stream()
                .map(ci -> Map.of(
                    "timestamp", ci.getTimestamp().toString(),
                    "lowerBound", ci.getLowerBound(),
                    "upperBound", ci.getUpperBound()
                ))
                .toList(),
            "metrics", Map.of(
                "mae", forecast.getMetrics().getMae(),
                "rmse", forecast.getMetrics().getRmse(),
                "mape", forecast.getMetrics().getMape(),
                "confidenceLevel", forecast.getMetrics().getConfidenceLevel()
            ),
            "generatedAt", forecast.getGeneratedAt().toString()
        );
    }

    public record GenerateForecastRequestRecord(
        String dataSource,
        String targetField,
        Integer horizon,
        Forecast.ForecastMethod method
    ) {}

    public record AnalyzeTrendsRequestRecord(
        String dataSource,
        String targetField
    ) {}
}
