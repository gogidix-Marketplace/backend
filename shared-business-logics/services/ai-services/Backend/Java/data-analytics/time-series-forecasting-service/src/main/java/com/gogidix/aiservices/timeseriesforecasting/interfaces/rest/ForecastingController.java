package com.gogidix.aiservices.timeseriesforecasting.interfaces.rest;

import com.gogidix.aiservices.timeseriesforecasting.application.dto.request.CreateForecastRequest;
import com.gogidix.aiservices.timeseriesforecasting.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.timeseriesforecasting.application.service.ForecastingService;
import com.gogidix.aiservices.timeseriesforecasting.shared.exception.ForecastNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/forecasting")
public class ForecastingController {

    private final ForecastingService forecastingService;

    public ForecastingController(ForecastingService forecastingService) {
        this.forecastingService = forecastingService;
    }

    @PostMapping("/forecasts")
    public ResponseEntity<ForecastResponse> createForecast(
            @RequestBody CreateForecastRequest request) {
        ForecastResponse response = forecastingService.createForecast(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/forecasts/{id}")
    public ResponseEntity<ForecastResponse> getForecast(@PathVariable String id) {
        ForecastResponse response = forecastingService.getForecast(id);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ForecastNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ForecastNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
