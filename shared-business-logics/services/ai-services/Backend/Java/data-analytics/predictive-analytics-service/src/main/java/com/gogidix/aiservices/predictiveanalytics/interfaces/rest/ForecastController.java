package com.gogidix.aiservices.predictiveanalytics.interfaces.rest;

import com.gogidix.aiservices.predictiveanalytics.application.dto.request.GenerateForecastRequest;
import com.gogidix.aiservices.predictiveanalytics.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.predictiveanalytics.application.service.ForecastService;
import com.gogidix.aiservices.predictiveanalytics.shared.exception.ForecastNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/predictive")
public class ForecastController {

    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @PostMapping("/forecasts")
    public ResponseEntity<ForecastResponse> generateForecast(
            @RequestBody GenerateForecastRequest request) {
        ForecastResponse response = forecastService.generateForecast(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/forecasts/{id}")
    public ResponseEntity<ForecastResponse> getForecast(@PathVariable String id) {
        ForecastResponse response = forecastService.getForecast(id);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ForecastNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ForecastNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
