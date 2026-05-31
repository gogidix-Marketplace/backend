package com.gogidix.aiservices.aidocumentprocessingservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request.ProcessDocumentRequest;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.DocumentProcessingResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.ProcessingStatusResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.application.service.DocumentProcessingService;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.DocumentNotFoundException;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.DocumentProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Validated
public class DocumentProcessingController {

    private final DocumentProcessingService documentProcessingService;

    @PostMapping("/process")
    public ResponseEntity<DocumentProcessingResponse> processDocument(
            @Valid @RequestBody ProcessDocumentRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {

        DocumentProcessingResponse response = documentProcessingService.processDocument(request, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/processing/{processingId}")
    public ResponseEntity<ProcessingStatusResponse> getProcessingStatus(@PathVariable String processingId) {
        try {
            UUID.fromString(processingId); // Validate UUID format
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        ProcessingStatusResponse response = documentProcessingService.getProcessingStatus(processingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/processing/{processingId}")
    public ResponseEntity<Void> cancelProcessing(@PathVariable String processingId) {
        documentProcessingService.cancelProcessing(processingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/processing/{processingId}/retry")
    public ResponseEntity<Void> retryProcessing(@PathVariable String processingId) {
        documentProcessingService.retryProcessing(processingId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchProcessingResponse> processBatch(
            @RequestBody Map<String, Object> batchRequest,
            @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> documents = (List<Map<String, Object>>) batchRequest.get("documents");

        if (documents == null || documents.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ObjectMapper mapper = new ObjectMapper();
        List<ProcessDocumentRequest> requests = documents.stream()
                .map(doc -> mapper.convertValue(doc, ProcessDocumentRequest.class))
                .toList();

        List<DocumentProcessingResponse> responses = documentProcessingService.processBatch(requests, userId);

        BatchProcessingResponse response = new BatchProcessingResponse(
                "Batch processing initiated",
                responses.size(),
                responses
        );

        return ResponseEntity.accepted().body(response);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(DocumentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DocumentProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleProcessingException(DocumentProcessingException ex) {
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
