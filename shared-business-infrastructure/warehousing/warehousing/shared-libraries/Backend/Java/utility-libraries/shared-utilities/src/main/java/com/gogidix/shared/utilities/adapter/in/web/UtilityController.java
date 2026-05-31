package com.gogidix.shared.utilities.adapter.in.web;

import com.gogidix.shared.utilities.application.port.in.UtilityOperationUseCase;
import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.UtilityType;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.OperationPriority;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for utility operations.
 * Provides HTTP endpoints for utility processing operations.
 */
@RestController
@RequestMapping("/api/v1/utilities")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Utility Operations", description = "Utility processing and management operations")
public class UtilityController {
    
    private final UtilityOperationUseCase utilityOperationUseCase;
    
    public UtilityController(UtilityOperationUseCase utilityOperationUseCase) {
        this.utilityOperationUseCase = utilityOperationUseCase;
    }
    
    @PostMapping("/execute")
    @Operation(summary = "Execute utility operation", description = "Executes a utility operation synchronously")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation executed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "422", description = "Operation validation failed")
    })
    public ResponseEntity<OperationResponse> executeOperation(@Valid @RequestBody ExecuteOperationDto request) {
        
        UtilityOperationUseCase.ExecuteOperationRequest executeRequest = new UtilityOperationUseCase.ExecuteOperationRequest(
            request.getType(),
            request.getName(),
            request.getDescription(),
            request.getParameters(),
            request.getPriority(),
            request.getUserId(),
            request.getSessionId(),
            request.isCached(),
            request.getCacheKey(),
            request.getCacheTtlSeconds(),
            request.getTimeoutSeconds(),
            request.getMetadata()
        );
        
        UtilityOperationUseCase.OperationResult result = utilityOperationUseCase.executeOperation(executeRequest);
        
        return ResponseEntity.ok(OperationResponse.from(result));
    }
    
    @PostMapping("/execute-async")
    @Operation(summary = "Execute utility operation asynchronously", description = "Executes a utility operation asynchronously")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Operation queued for processing"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<AsyncOperationResponse> executeOperationAsync(@Valid @RequestBody ExecuteOperationDto request) {
        
        UtilityOperationUseCase.ExecuteOperationRequest executeRequest = new UtilityOperationUseCase.ExecuteOperationRequest(
            request.getType(),
            request.getName(),
            request.getDescription(),
            request.getParameters(),
            request.getPriority(),
            request.getUserId(),
            request.getSessionId(),
            request.isCached(),
            request.getCacheKey(),
            request.getCacheTtlSeconds(),
            request.getTimeoutSeconds(),
            request.getMetadata()
        );
        
        CompletableFuture<UtilityOperationUseCase.OperationResult> future = 
            utilityOperationUseCase.executeOperationAsync(executeRequest);
        
        // Get the operation ID from the immediate result (should be available)
        String operationId = "async-" + System.currentTimeMillis(); // Mock ID for response
        
        return ResponseEntity.accepted().body(new AsyncOperationResponse(operationId, 
            "Operation queued for processing", "/api/v1/utilities/operations/" + operationId));
    }
    
    @PostMapping("/execute-batch")
    @Operation(summary = "Execute batch utility operations", description = "Executes multiple utility operations in batch")
    @PreAuthorize("hasAuthority('UTILITY_BATCH_EXECUTE')")
    public ResponseEntity<BatchOperationResponse> executeBatchOperations(@Valid @RequestBody BatchExecuteDto request) {
        
        List<UtilityOperationUseCase.ExecuteOperationRequest> executeRequests = request.getOperations().stream()
            .map(dto -> new UtilityOperationUseCase.ExecuteOperationRequest(
                dto.getType(), dto.getName(), dto.getDescription(), dto.getParameters(),
                dto.getPriority(), dto.getUserId(), dto.getSessionId(), dto.isCached(),
                dto.getCacheKey(), dto.getCacheTtlSeconds(), dto.getTimeoutSeconds(), dto.getMetadata()))
            .toList();
        
        UtilityOperationUseCase.BatchOperationResult result = utilityOperationUseCase.executeBatchOperations(executeRequests);
        
        return ResponseEntity.ok(BatchOperationResponse.from(result));
    }
    
    @GetMapping("/operations/{operationId}")
    @Operation(summary = "Get operation details", description = "Retrieves details of a specific utility operation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation found"),
        @ApiResponse(responseCode = "404", description = "Operation not found")
    })
    public ResponseEntity<OperationDetailsResponse> getOperation(@PathVariable String operationId) {
        
        return utilityOperationUseCase.getOperation(operationId)
            .map(operation -> ResponseEntity.ok(OperationDetailsResponse.from(operation)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/users/{userId}/operations")
    @Operation(summary = "Get user operations", description = "Retrieves operations for a specific user")
    @PreAuthorize("hasAuthority('UTILITY_READ') or #userId == authentication.name")
    public ResponseEntity<List<OperationDetailsResponse>> getUserOperations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "50") int limit) {
        
        List<UtilityOperation> operations = utilityOperationUseCase.getUserOperations(userId, limit);
        
        List<OperationDetailsResponse> response = operations.stream()
            .map(OperationDetailsResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/operations")
    @Operation(summary = "Get operations by criteria", description = "Retrieves operations filtered by status or type")
    @PreAuthorize("hasAuthority('UTILITY_READ_ALL')")
    public ResponseEntity<List<OperationDetailsResponse>> getOperations(
            @RequestParam(required = false) OperationStatus status,
            @RequestParam(required = false) UtilityType type,
            @RequestParam(defaultValue = "50") int limit) {
        
        List<UtilityOperation> operations;
        
        if (status != null) {
            operations = utilityOperationUseCase.getOperationsByStatus(status, limit);
        } else if (type != null) {
            operations = utilityOperationUseCase.getOperationsByType(type, limit);
        } else {
            // Return recent operations if no filter specified
            operations = utilityOperationUseCase.getOperationsByStatus(OperationStatus.COMPLETED, limit);
        }
        
        List<OperationDetailsResponse> response = operations.stream()
            .map(OperationDetailsResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/operations/{operationId}/cancel")
    @Operation(summary = "Cancel operation", description = "Cancels a running utility operation")
    @PreAuthorize("hasAuthority('UTILITY_CANCEL')")
    public ResponseEntity<OperationResponse> cancelOperation(
            @PathVariable String operationId,
            @Valid @RequestBody CancelOperationDto request) {
        
        UtilityOperationUseCase.OperationResult result = utilityOperationUseCase.cancelOperation(operationId, request.getReason());
        
        return ResponseEntity.ok(OperationResponse.from(result));
    }
    
    @PostMapping("/operations/{operationId}/retry")
    @Operation(summary = "Retry failed operation", description = "Retries a failed utility operation")
    @PreAuthorize("hasAuthority('UTILITY_RETRY')")
    public ResponseEntity<OperationResponse> retryOperation(@PathVariable String operationId) {
        
        UtilityOperationUseCase.OperationResult result = utilityOperationUseCase.retryOperation(operationId);
        
        return ResponseEntity.ok(OperationResponse.from(result));
    }
    
    @PostMapping("/operations/{operationId}/pause")
    @Operation(summary = "Pause operation", description = "Pauses a running utility operation")
    @PreAuthorize("hasAuthority('UTILITY_PAUSE')")
    public ResponseEntity<OperationResponse> pauseOperation(@PathVariable String operationId) {
        
        UtilityOperationUseCase.OperationResult result = utilityOperationUseCase.pauseOperation(operationId);
        
        return ResponseEntity.ok(OperationResponse.from(result));
    }
    
    @PostMapping("/operations/{operationId}/resume")
    @Operation(summary = "Resume paused operation", description = "Resumes a paused utility operation")
    @PreAuthorize("hasAuthority('UTILITY_RESUME')")
    public ResponseEntity<OperationResponse> resumeOperation(@PathVariable String operationId) {
        
        UtilityOperationUseCase.OperationResult result = utilityOperationUseCase.resumeOperation(operationId);
        
        return ResponseEntity.ok(OperationResponse.from(result));
    }
    
    @GetMapping("/users/{userId}/statistics")
    @Operation(summary = "Get user operation statistics", description = "Gets operation statistics for a user")
    @PreAuthorize("hasAuthority('UTILITY_STATS') or #userId == authentication.name")
    public ResponseEntity<OperationStatisticsResponse> getOperationStatistics(@PathVariable String userId) {
        
        UtilityOperationUseCase.OperationStatistics statistics = utilityOperationUseCase.getOperationStatistics(userId);
        
        return ResponseEntity.ok(OperationStatisticsResponse.from(statistics));
    }
    
    @GetMapping("/statistics/global")
    @Operation(summary = "Get global operation statistics", description = "Gets global operation statistics")
    @PreAuthorize("hasAuthority('UTILITY_GLOBAL_STATS')")
    public ResponseEntity<OperationStatisticsResponse> getGlobalStatistics() {
        
        UtilityOperationUseCase.OperationStatistics statistics = utilityOperationUseCase.getGlobalStatistics();
        
        return ResponseEntity.ok(OperationStatisticsResponse.from(statistics));
    }
    
    @PostMapping("/admin/cleanup")
    @Operation(summary = "Cleanup completed operations", description = "Removes old completed operations")
    @PreAuthorize("hasAuthority('UTILITY_CLEANUP')")
    public ResponseEntity<CleanupResponse> cleanupCompletedOperations(@RequestParam(defaultValue = "30") int maxAge) {
        
        UtilityOperationUseCase.CleanupResult result = utilityOperationUseCase.cleanupCompletedOperations(maxAge);
        
        return ResponseEntity.ok(CleanupResponse.from(result));
    }
    
    @GetMapping("/types")
    @Operation(summary = "Get available utility types", description = "Gets list of available utility operation types")
    public ResponseEntity<List<UtilityTypeInfoResponse>> getAvailableUtilityTypes() {
        
        List<UtilityOperationUseCase.UtilityTypeInfo> types = utilityOperationUseCase.getAvailableUtilityTypes();
        
        List<UtilityTypeInfoResponse> response = types.stream()
            .map(UtilityTypeInfoResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate operation parameters", description = "Validates parameters for a utility operation")
    public ResponseEntity<ValidationResponse> validateOperationParameters(@Valid @RequestBody ValidateParametersDto request) {
        
        UtilityOperationUseCase.ValidationResult result = utilityOperationUseCase.validateOperationParameters(
            request.getType(), request.getParameters());
        
        return ResponseEntity.ok(ValidationResponse.from(result));
    }
    
    @GetMapping("/queue/status")
    @Operation(summary = "Get queue status", description = "Gets current operation queue status")
    @PreAuthorize("hasAuthority('UTILITY_QUEUE_STATUS')")
    public ResponseEntity<QueueStatusResponse> getQueueStatus() {
        
        UtilityOperationUseCase.QueueStatus status = utilityOperationUseCase.getQueueStatus();
        
        return ResponseEntity.ok(QueueStatusResponse.from(status));
    }
    
    @PostMapping("/queue/process")
    @Operation(summary = "Process queue", description = "Triggers processing of queued operations")
    @PreAuthorize("hasAuthority('UTILITY_PROCESS_QUEUE')")
    public ResponseEntity<ProcessingResponse> processQueue() {
        
        UtilityOperationUseCase.ProcessingResult result = utilityOperationUseCase.processQueue();
        
        return ResponseEntity.ok(ProcessingResponse.from(result));
    }
    
    // DTO Classes
    
    public static class ExecuteOperationDto {
        @NotNull private UtilityType type;
        @NotBlank private String name;
        private String description;
        @NotNull private Map<String, Object> parameters;
        private OperationPriority priority = OperationPriority.NORMAL;
        private String userId;
        private String sessionId;
        private boolean cached = false;
        private String cacheKey;
        private Long cacheTtlSeconds;
        private Long timeoutSeconds;
        private Map<String, String> metadata;
        
        // Getters and setters
        public UtilityType getType() { return type; }
        public void setType(UtilityType type) { this.type = type; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public OperationPriority getPriority() { return priority; }
        public void setPriority(OperationPriority priority) { this.priority = priority; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public boolean isCached() { return cached; }
        public void setCached(boolean cached) { this.cached = cached; }
        public String getCacheKey() { return cacheKey; }
        public void setCacheKey(String cacheKey) { this.cacheKey = cacheKey; }
        public Long getCacheTtlSeconds() { return cacheTtlSeconds; }
        public void setCacheTtlSeconds(Long cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }
        public Long getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(Long timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
        public Map<String, String> getMetadata() { return metadata; }
        public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    }
    
    public static class BatchExecuteDto {
        @NotNull private List<ExecuteOperationDto> operations;
        
        public List<ExecuteOperationDto> getOperations() { return operations; }
        public void setOperations(List<ExecuteOperationDto> operations) { this.operations = operations; }
    }
    
    public static class CancelOperationDto {
        @NotBlank private String reason;
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    public static class ValidateParametersDto {
        @NotNull private UtilityType type;
        @NotNull private Map<String, Object> parameters;
        
        public UtilityType getType() { return type; }
        public void setType(UtilityType type) { this.type = type; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }
    
    // Response Classes
    
    public static class OperationResponse {
        private boolean success;
        private String operationId;
        private String status;
        private Map<String, Object> result;
        private String message;
        private String errorCode;
        
        public static OperationResponse from(UtilityOperationUseCase.OperationResult result) {
            OperationResponse response = new OperationResponse();
            response.success = result.isSuccess();
            response.operationId = result.getOperation() != null ? result.getOperation().getOperationId() : null;
            response.status = result.getOperation() != null ? result.getOperation().getStatus().name() : null;
            response.result = result.getResult();
            response.message = result.getMessage();
            response.errorCode = result.getErrorCode();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public String getOperationId() { return operationId; }
        public String getStatus() { return status; }
        public Map<String, Object> getResult() { return result; }
        public String getMessage() { return message; }
        public String getErrorCode() { return errorCode; }
    }
    
    public static class AsyncOperationResponse {
        private String operationId;
        private String message;
        private String statusUrl;
        
        public AsyncOperationResponse(String operationId, String message, String statusUrl) {
            this.operationId = operationId;
            this.message = message;
            this.statusUrl = statusUrl;
        }
        
        public String getOperationId() { return operationId; }
        public String getMessage() { return message; }
        public String getStatusUrl() { return statusUrl; }
    }
    
    public static class BatchOperationResponse {
        private int totalOperations;
        private int successfulOperations;
        private int failedOperations;
        private double successRate;
        private long totalExecutionTimeMs;
        private List<OperationResponse> results;
        
        public static BatchOperationResponse from(UtilityOperationUseCase.BatchOperationResult result) {
            BatchOperationResponse response = new BatchOperationResponse();
            response.totalOperations = result.getTotalOperations();
            response.successfulOperations = result.getSuccessfulOperations();
            response.failedOperations = result.getFailedOperations();
            response.successRate = result.getSuccessRate();
            response.totalExecutionTimeMs = result.getTotalExecutionTimeMs();
            response.results = result.getResults().stream()
                .map(OperationResponse::from)
                .toList();
            return response;
        }
        
        public int getTotalOperations() { return totalOperations; }
        public int getSuccessfulOperations() { return successfulOperations; }
        public int getFailedOperations() { return failedOperations; }
        public double getSuccessRate() { return successRate; }
        public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
        public List<OperationResponse> getResults() { return results; }
    }
    
    public static class OperationDetailsResponse {
        private String operationId;
        private String name;
        private String description;
        private UtilityType type;
        private String status;
        private String priority;
        private java.time.LocalDateTime startTime;
        private java.time.LocalDateTime endTime;
        private Long executionTimeMs;
        private String userId;
        private Map<String, Object> inputData;
        private Map<String, Object> outputData;
        private int retryCount;
        private String errorMessage;
        private String errorCode;
        
        public static OperationDetailsResponse from(UtilityOperation operation) {
            OperationDetailsResponse response = new OperationDetailsResponse();
            response.operationId = operation.getOperationId();
            response.name = operation.getOperationName();
            response.description = operation.getOperationDescription();
            response.type = operation.getOperationType();
            response.status = operation.getStatus().name();
            response.priority = operation.getPriority().name();
            response.startTime = operation.getOperationStartTime();
            response.endTime = operation.getOperationEndTime();
            response.executionTimeMs = operation.getExecutionTimeMs();
            response.userId = operation.getUserId();
            response.inputData = operation.getInputData();
            response.outputData = operation.getOutputData();
            response.retryCount = operation.getRetryCount();
            response.errorMessage = operation.getErrorMessage();
            response.errorCode = operation.getErrorCode();
            return response;
        }
        
        // Getters
        public String getOperationId() { return operationId; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public UtilityType getType() { return type; }
        public String getStatus() { return status; }
        public String getPriority() { return priority; }
        public java.time.LocalDateTime getStartTime() { return startTime; }
        public java.time.LocalDateTime getEndTime() { return endTime; }
        public Long getExecutionTimeMs() { return executionTimeMs; }
        public String getUserId() { return userId; }
        public Map<String, Object> getInputData() { return inputData; }
        public Map<String, Object> getOutputData() { return outputData; }
        public int getRetryCount() { return retryCount; }
        public String getErrorMessage() { return errorMessage; }
        public String getErrorCode() { return errorCode; }
    }
    
    public static class OperationStatisticsResponse {
        private String userId;
        private long totalOperations;
        private long successfulOperations;
        private long failedOperations;
        private double successRate;
        private long averageExecutionTimeMs;
        private Map<UtilityType, Long> operationsByType;
        private Map<OperationStatus, Long> operationsByStatus;
        private java.time.LocalDateTime lastOperationTime;
        
        public static OperationStatisticsResponse from(UtilityOperationUseCase.OperationStatistics statistics) {
            OperationStatisticsResponse response = new OperationStatisticsResponse();
            response.userId = statistics.getUserId();
            response.totalOperations = statistics.getTotalOperations();
            response.successfulOperations = statistics.getSuccessfulOperations();
            response.failedOperations = statistics.getFailedOperations();
            response.successRate = statistics.getSuccessRate();
            response.averageExecutionTimeMs = statistics.getAverageExecutionTimeMs();
            response.operationsByType = statistics.getOperationsByType();
            response.operationsByStatus = statistics.getOperationsByStatus();
            response.lastOperationTime = statistics.getLastOperationTime();
            return response;
        }
        
        // Getters
        public String getUserId() { return userId; }
        public long getTotalOperations() { return totalOperations; }
        public long getSuccessfulOperations() { return successfulOperations; }
        public long getFailedOperations() { return failedOperations; }
        public double getSuccessRate() { return successRate; }
        public long getAverageExecutionTimeMs() { return averageExecutionTimeMs; }
        public Map<UtilityType, Long> getOperationsByType() { return operationsByType; }
        public Map<OperationStatus, Long> getOperationsByStatus() { return operationsByStatus; }
        public java.time.LocalDateTime getLastOperationTime() { return lastOperationTime; }
    }
    
    public static class CleanupResponse {
        private int operationsFound;
        private int operationsRemoved;
        private long cleanupTimeMs;
        private String summary;
        
        public static CleanupResponse from(UtilityOperationUseCase.CleanupResult result) {
            CleanupResponse response = new CleanupResponse();
            response.operationsFound = result.getOperationsFound();
            response.operationsRemoved = result.getOperationsRemoved();
            response.cleanupTimeMs = result.getCleanupTimeMs();
            response.summary = result.getSummary();
            return response;
        }
        
        public int getOperationsFound() { return operationsFound; }
        public int getOperationsRemoved() { return operationsRemoved; }
        public long getCleanupTimeMs() { return cleanupTimeMs; }
        public String getSummary() { return summary; }
    }
    
    public static class UtilityTypeInfoResponse {
        private UtilityType type;
        private String displayName;
        private boolean cacheable;
        private boolean retryable;
        private long defaultTimeoutSeconds;
        private long maxInputSizeBytes;
        private boolean requiresAuthentication;
        private List<String> requiredParameters;
        
        public static UtilityTypeInfoResponse from(UtilityOperationUseCase.UtilityTypeInfo info) {
            UtilityTypeInfoResponse response = new UtilityTypeInfoResponse();
            response.type = info.getType();
            response.displayName = info.getType().getDisplayName();
            response.cacheable = info.isCacheable();
            response.retryable = info.isRetryable();
            response.defaultTimeoutSeconds = info.getDefaultTimeoutSeconds();
            response.maxInputSizeBytes = info.getMaxInputSizeBytes();
            response.requiresAuthentication = info.isRequiresAuthentication();
            response.requiredParameters = info.getRequiredParameters();
            return response;
        }
        
        // Getters
        public UtilityType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public boolean isCacheable() { return cacheable; }
        public boolean isRetryable() { return retryable; }
        public long getDefaultTimeoutSeconds() { return defaultTimeoutSeconds; }
        public long getMaxInputSizeBytes() { return maxInputSizeBytes; }
        public boolean isRequiresAuthentication() { return requiresAuthentication; }
        public List<String> getRequiredParameters() { return requiredParameters; }
    }
    
    public static class ValidationResponse {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private String message;
        
        public static ValidationResponse from(UtilityOperationUseCase.ValidationResult result) {
            ValidationResponse response = new ValidationResponse();
            response.valid = result.isValid();
            response.errors = result.getErrors();
            response.warnings = result.getWarnings();
            response.message = result.getMessage();
            return response;
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public String getMessage() { return message; }
    }
    
    public static class QueueStatusResponse {
        private int queuedOperations;
        private int runningOperations;
        private int completedOperations;
        private int failedOperations;
        private double averageWaitTimeMs;
        private int availableWorkers;
        private int busyWorkers;
        private int totalWorkers;
        private boolean overloaded;
        
        public static QueueStatusResponse from(UtilityOperationUseCase.QueueStatus status) {
            QueueStatusResponse response = new QueueStatusResponse();
            response.queuedOperations = status.getQueuedOperations();
            response.runningOperations = status.getRunningOperations();
            response.completedOperations = status.getCompletedOperations();
            response.failedOperations = status.getFailedOperations();
            response.averageWaitTimeMs = status.getAverageWaitTimeMs();
            response.availableWorkers = status.getAvailableWorkers();
            response.busyWorkers = status.getBusyWorkers();
            response.totalWorkers = status.getTotalWorkers();
            response.overloaded = status.isOverloaded();
            return response;
        }
        
        // Getters
        public int getQueuedOperations() { return queuedOperations; }
        public int getRunningOperations() { return runningOperations; }
        public int getCompletedOperations() { return completedOperations; }
        public int getFailedOperations() { return failedOperations; }
        public double getAverageWaitTimeMs() { return averageWaitTimeMs; }
        public int getAvailableWorkers() { return availableWorkers; }
        public int getBusyWorkers() { return busyWorkers; }
        public int getTotalWorkers() { return totalWorkers; }
        public boolean isOverloaded() { return overloaded; }
    }
    
    public static class ProcessingResponse {
        private int processedOperations;
        private int skippedOperations;
        private long processingTimeMs;
        private String summary;
        
        public static ProcessingResponse from(UtilityOperationUseCase.ProcessingResult result) {
            ProcessingResponse response = new ProcessingResponse();
            response.processedOperations = result.getProcessedOperations();
            response.skippedOperations = result.getSkippedOperations();
            response.processingTimeMs = result.getProcessingTimeMs();
            response.summary = result.getSummary();
            return response;
        }
        
        public int getProcessedOperations() { return processedOperations; }
        public int getSkippedOperations() { return skippedOperations; }
        public long getProcessingTimeMs() { return processingTimeMs; }
        public String getSummary() { return summary; }
    }
}