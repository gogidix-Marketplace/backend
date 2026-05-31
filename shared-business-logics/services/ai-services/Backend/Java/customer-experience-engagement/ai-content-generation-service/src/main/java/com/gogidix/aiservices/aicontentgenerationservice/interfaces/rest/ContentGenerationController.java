package com.gogidix.aiservices.aicontentgenerationservice.interfaces.rest;

import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.GenerateContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.OptimizeContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.ContentResponse;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.OptimizationResponse;
import com.gogidix.aiservices.aicontentgenerationservice.application.service.ContentGenerationService;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentGenerationException;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/content-generation")
@RequiredArgsConstructor
@Validated
public class ContentGenerationController {

    private final ContentGenerationService contentGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<ContentResponse> generateContent(@Valid @RequestBody GenerateContentRequest request) {
        ContentResponse response = contentGenerationService.generateContent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/generate-variations")
    public ResponseEntity<ContentResponse> generateVariations(@Valid @RequestBody GenerateContentRequest request) {
        ContentResponse response = contentGenerationService.generateVariations(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<ContentResponse> getContent(@PathVariable String contentId) {
        ContentResponse response = contentGenerationService.getContent(contentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/content/user/{userId}")
    public ResponseEntity<List<ContentResponse>> getUserContent(@PathVariable String userId) {
        List<ContentResponse> responses = contentGenerationService.getUserContent(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizationResponse> optimizeContent(@Valid @RequestBody OptimizeContentRequest request) {
        OptimizationResponse response = contentGenerationService.optimizeContent(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<Void> deleteContent(@PathVariable String contentId) {
        contentGenerationService.deleteContent(contentId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleContentNotFound(ContentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ContentGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleContentGenerationException(ContentGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("Invalid request format: " + ex.getMostSpecificCause().getMessage()));
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
