package com.gogidix.aiservices.nlpprocessingservice.interfaces.rest;

import com.gogidix.aiservices.nlpprocessingservice.application.dto.request.AnalyzeTextRequest;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.request.SummarizeTextRequest;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.response.TextAnalysisResponse;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.response.TextSummaryResponse;
import com.gogidix.aiservices.nlpprocessingservice.application.service.NlpProcessingService;
import com.gogidix.aiservices.nlpprocessingservice.shared.exception.NlpProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/nlp")
@RequiredArgsConstructor
@Validated
public class NlpProcessingController {

    private final NlpProcessingService service;

    @PostMapping("/analyze")
    public ResponseEntity<TextAnalysisResponse> analyzeText(
            @Valid @RequestBody AnalyzeTextRequest request) {

        TextAnalysisResponse response = service.analyzeText(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/summarize")
    public ResponseEntity<TextSummaryResponse> summarizeText(
            @Valid @RequestBody SummarizeTextRequest request) {

        TextSummaryResponse response = service.summarizeText(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(NlpProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleNlpException(NlpProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
