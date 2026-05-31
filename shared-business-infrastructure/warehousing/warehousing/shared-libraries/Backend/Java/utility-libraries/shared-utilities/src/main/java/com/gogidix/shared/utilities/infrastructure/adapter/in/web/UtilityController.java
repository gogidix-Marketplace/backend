package com.gogidix.shared.utilities.infrastructure.adapter.in.web;

import com.gogidix.shared.utilities.application.port.in.ProcessUtilityPort;
import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityResult;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for utility operations
 * Provides HTTP endpoints for various utility functions
 */
@RestController
@RequestMapping("/api/v1/utilities")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Utilities", description = "Comprehensive utility operations API")
public class UtilityController {

    private final ProcessUtilityPort processUtilityPort;

    @Operation(
        summary = "Process DateTime formatting operation", 
        description = "Format LocalDateTime to ISO string format"
    )
    @ApiResponse(responseCode = "200", description = "DateTime formatted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid datetime provided")
    @PostMapping("/datetime/format")
    public ResponseEntity<UtilityResult<String>> formatDateTime(
            @Parameter(description = "DateTime to format") 
            @RequestBody Map<String, Object> request) {
        
        try {
            LocalDateTime dateTime = LocalDateTime.parse(request.get("dateTime").toString());
            
            ProcessingRequest processingRequest = ProcessingRequest.create(
                UtilityType.DATETIME_FORMATTING, 
                dateTime
            );
            
            UtilityResult<?> result = processUtilityPort.processUtilityRequest(processingRequest);
            
            @SuppressWarnings("unchecked")
            UtilityResult<String> typedResult = (UtilityResult<String>) result;
            
            return ResponseEntity.ok(typedResult);
        } catch (Exception e) {
            log.error("Error formatting datetime", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Process DateTime parsing operation", 
        description = "Parse ISO string to LocalDateTime"
    )
    @ApiResponse(responseCode = "200", description = "DateTime parsed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ISO string provided")
    @PostMapping("/datetime/parse")
    public ResponseEntity<UtilityResult<LocalDateTime>> parseDateTime(
            @Parameter(description = "ISO string to parse") 
            @RequestBody Map<String, Object> request) {
        
        try {
            String isoString = request.get("isoString").toString();
            
            ProcessingRequest processingRequest = ProcessingRequest.create(
                UtilityType.DATETIME_PARSING, 
                isoString
            );
            
            UtilityResult<?> result = processUtilityPort.processUtilityRequest(processingRequest);
            
            @SuppressWarnings("unchecked")
            UtilityResult<LocalDateTime> typedResult = (UtilityResult<LocalDateTime>) result;
            
            return ResponseEntity.ok(typedResult);
        } catch (Exception e) {
            log.error("Error parsing datetime", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Check if DateTime is expired", 
        description = "Validate if given datetime is before current time"
    )
    @ApiResponse(responseCode = "200", description = "DateTime validation completed")
    @PostMapping("/datetime/validate-expired")
    public ResponseEntity<UtilityResult<Boolean>> isDateTimeExpired(
            @Parameter(description = "DateTime to validate") 
            @RequestBody Map<String, Object> request) {
        
        try {
            LocalDateTime dateTime = LocalDateTime.parse(request.get("dateTime").toString());
            
            ProcessingRequest processingRequest = ProcessingRequest.create(
                UtilityType.DATETIME_VALIDATION, 
                dateTime
            );
            
            UtilityResult<?> result = processUtilityPort.processUtilityRequest(processingRequest);
            
            @SuppressWarnings("unchecked")
            UtilityResult<Boolean> typedResult = (UtilityResult<Boolean>) result;
            
            return ResponseEntity.ok(typedResult);
        } catch (Exception e) {
            log.error("Error validating datetime", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Serialize object to JSON", 
        description = "Convert Java object to JSON string representation"
    )
    @ApiResponse(responseCode = "200", description = "Object serialized to JSON successfully")
    @ApiResponse(responseCode = "400", description = "Invalid object for serialization")
    @PostMapping("/json/serialize")
    public ResponseEntity<UtilityResult<String>> serializeToJson(
            @Parameter(description = "Object to serialize") 
            @RequestBody Object data) {
        
        try {
            ProcessingRequest processingRequest = ProcessingRequest.create(
                UtilityType.JSON_SERIALIZATION, 
                data
            );
            
            UtilityResult<?> result = processUtilityPort.processUtilityRequest(processingRequest);
            
            @SuppressWarnings("unchecked")
            UtilityResult<String> typedResult = (UtilityResult<String>) result;
            
            return ResponseEntity.ok(typedResult);
        } catch (Exception e) {
            log.error("Error serializing to JSON", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Validate JSON string", 
        description = "Check if provided string is valid JSON format"
    )
    @ApiResponse(responseCode = "200", description = "JSON validation completed")
    @PostMapping("/json/validate")
    public ResponseEntity<UtilityResult<Boolean>> validateJson(
            @Parameter(description = "JSON string to validate") 
            @RequestBody Map<String, Object> request) {
        
        try {
            String jsonString = request.get("json").toString();
            
            ProcessingRequest processingRequest = ProcessingRequest.create(
                UtilityType.JSON_VALIDATION, 
                jsonString
            );
            
            UtilityResult<?> result = processUtilityPort.processUtilityRequest(processingRequest);
            
            @SuppressWarnings("unchecked")
            UtilityResult<Boolean> typedResult = (UtilityResult<Boolean>) result;
            
            return ResponseEntity.ok(typedResult);
        } catch (Exception e) {
            log.error("Error validating JSON", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Pretty print JSON", 
        description = "Format JSON string with proper indentation and formatting"
    )
    @ApiResponse(responseCode = "200", description = "JSON formatted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid JSON provided")
    @PostMapping("/json/pretty-print")
    public ResponseEntity<UtilityResult<String>> prettyPrintJson(
            @Parameter(description = "JSON string to format") 
            @RequestBody Map<String, Object> request) {
        
        try {
            String jsonString = request.get("json").toString();
            
            ProcessingRequest processingRequest = ProcessingRequest.create(
                UtilityType.JSON_TRANSFORMATION, 
                jsonString
            );
            
            UtilityResult<?> result = processUtilityPort.processUtilityRequest(processingRequest);
            
            @SuppressWarnings("unchecked")
            UtilityResult<String> typedResult = (UtilityResult<String>) result;
            
            return ResponseEntity.ok(typedResult);
        } catch (Exception e) {
            log.error("Error pretty printing JSON", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Process utility operation asynchronously", 
        description = "Submit utility operation for asynchronous processing"
    )
    @ApiResponse(responseCode = "202", description = "Operation submitted for processing")
    @PostMapping("/async")
    public ResponseEntity<CompletableFuture<UtilityResult<?>>> processAsync(
            @Parameter(description = "Async processing request") 
            @RequestBody Map<String, Object> request) {
        
        try {
            UtilityType utilityType = UtilityType.valueOf(request.get("utilityType").toString());
            Object data = request.get("data");
            
            ProcessingRequest processingRequest = ProcessingRequest.create(utilityType, data);
            
            CompletableFuture<UtilityResult<?>> result = processUtilityPort.processUtilityRequestAsync(processingRequest);
            
            return ResponseEntity.accepted().body(result);
        } catch (Exception e) {
            log.error("Error processing async request", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Get utility service health", 
        description = "Check health status of utility service"
    )
    @ApiResponse(responseCode = "200", description = "Service health retrieved successfully")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "service", "shared-utilities",
            "timestamp", LocalDateTime.now(),
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(health);
    }

    @Operation(
        summary = "Get supported utility types", 
        description = "List all supported utility operation types"
    )
    @ApiResponse(responseCode = "200", description = "Utility types retrieved successfully")
    @GetMapping("/types")
    public ResponseEntity<UtilityType[]> getUtilityTypes() {
        return ResponseEntity.ok(UtilityType.values());
    }
}