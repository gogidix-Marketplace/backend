package com.gogidix.aiservices.multimodalprocessingservice.interfaces.rest;

import com.gogidix.aiservices.multimodalprocessingservice.application.dto.request.ProcessMultimodalRequest;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.request.SearchSimilarRequest;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.response.MultimodalProcessingResponse;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.response.SimilarContentResponse;
import com.gogidix.aiservices.multimodalprocessingservice.application.service.MultimodalProcessingService;
import com.gogidix.aiservices.multimodalprocessingservice.shared.exception.MultimodalProcessingException;
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
@RequestMapping("/api/v1/multimodal")
@RequiredArgsConstructor
@Validated
public class MultimodalProcessingController {

    private final MultimodalProcessingService service;

    @PostMapping("/process")
    public ResponseEntity<MultimodalProcessingResponse> processMultimodal(
            @Valid @RequestBody ProcessMultimodalRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {

        MultimodalProcessingResponse response = service.processMultimodal(request, userId);
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<SimilarContentResponse> searchSimilar(
            @Valid @RequestBody SearchSimilarRequest request) {

        SimilarContentResponse response = service.searchSimilar(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MultimodalProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleProcessingException(MultimodalProcessingException ex) {
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
