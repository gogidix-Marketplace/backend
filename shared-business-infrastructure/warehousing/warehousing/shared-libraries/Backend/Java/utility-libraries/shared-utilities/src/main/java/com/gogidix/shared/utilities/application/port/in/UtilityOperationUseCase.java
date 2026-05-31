package com.gogidix.shared.utilities.application.port.in;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.UtilityType;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.OperationPriority;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Input port for utility operation use cases.
 * Defines the contract for utility operations from the application layer.
 */
public interface UtilityOperationUseCase {
    
    /**
     * Executes a utility operation synchronously
     */
    OperationResult executeOperation(ExecuteOperationRequest request);
    
    /**
     * Executes a utility operation asynchronously
     */
    CompletableFuture<OperationResult> executeOperationAsync(ExecuteOperationRequest request);
    
    /**
     * Executes multiple operations in batch
     */
    BatchOperationResult executeBatchOperations(List<ExecuteOperationRequest> requests);
    
    /**
     * Gets operation status and result
     */
    Optional<UtilityOperation> getOperation(String operationId);
    
    /**
     * Gets operations by user ID
     */
    List<UtilityOperation> getUserOperations(String userId, int limit);
    
    /**
     * Gets operations by status
     */
    List<UtilityOperation> getOperationsByStatus(OperationStatus status, int limit);
    
    /**
     * Gets operations by type
     */
    List<UtilityOperation> getOperationsByType(UtilityType type, int limit);
    
    /**
     * Cancels a running operation
     */
    OperationResult cancelOperation(String operationId, String reason);
    
    /**
     * Retries a failed operation
     */
    OperationResult retryOperation(String operationId);
    
    /**
     * Pauses a running operation
     */
    OperationResult pauseOperation(String operationId);
    
    /**
     * Resumes a paused operation
     */
    OperationResult resumeOperation(String operationId);
    
    /**
     * Gets operation statistics
     */
    OperationStatistics getOperationStatistics(String userId);
    
    /**
     * Gets global operation statistics
     */
    OperationStatistics getGlobalStatistics();
    
    /**
     * Cleans up completed operations
     */
    CleanupResult cleanupCompletedOperations(int maxAge);
    
    /**
     * Gets available utility types
     */
    List<UtilityTypeInfo> getAvailableUtilityTypes();
    
    /**
     * Validates operation parameters
     */
    ValidationResult validateOperationParameters(UtilityType type, Map<String, Object> parameters);
    
    /**
     * Gets operation queue status
     */
    QueueStatus getQueueStatus();
    
    /**
     * Processes operation queue
     */
    ProcessingResult processQueue();
    
    /**
     * Request to execute an operation
     */
    class ExecuteOperationRequest {
        private final UtilityType type;
        private final String name;
        private final String description;
        private final Map<String, Object> parameters;
        private final OperationPriority priority;
        private final String userId;
        private final String sessionId;
        private final boolean cached;
        private final String cacheKey;
        private final Long cacheTtlSeconds;
        private final Long timeoutSeconds;
        private final Map<String, String> metadata;
        
        public ExecuteOperationRequest(UtilityType type, String name, String description,
                                     Map<String, Object> parameters, OperationPriority priority,
                                     String userId, String sessionId, boolean cached,
                                     String cacheKey, Long cacheTtlSeconds, Long timeoutSeconds,
                                     Map<String, String> metadata) {
            this.type = type;
            this.name = name;
            this.description = description;
            this.parameters = parameters;
            this.priority = priority;
            this.userId = userId;
            this.sessionId = sessionId;
            this.cached = cached;
            this.cacheKey = cacheKey;
            this.cacheTtlSeconds = cacheTtlSeconds;
            this.timeoutSeconds = timeoutSeconds;
            this.metadata = metadata;
        }
        
        // Getters
        public UtilityType getType() { return type; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public Map<String, Object> getParameters() { return parameters; }
        public OperationPriority getPriority() { return priority; }
        public String getUserId() { return userId; }
        public String getSessionId() { return sessionId; }
        public boolean isCached() { return cached; }
        public String getCacheKey() { return cacheKey; }
        public Long getCacheTtlSeconds() { return cacheTtlSeconds; }
        public Long getTimeoutSeconds() { return timeoutSeconds; }
        public Map<String, String> getMetadata() { return metadata; }
    }
    
    /**
     * Operation execution result
     */
    class OperationResult {
        private final boolean success;
        private final UtilityOperation operation;
        private final Map<String, Object> result;
        private final String message;
        private final String errorCode;
        private final Exception error;
        
        public OperationResult(boolean success, UtilityOperation operation, Map<String, Object> result,
                              String message, String errorCode, Exception error) {
            this.success = success;
            this.operation = operation;
            this.result = result;
            this.message = message;
            this.errorCode = errorCode;
            this.error = error;
        }
        
        public static OperationResult success(UtilityOperation operation, Map<String, Object> result, String message) {
            return new OperationResult(true, operation, result, message, null, null);
        }
        
        public static OperationResult failure(UtilityOperation operation, String message, String errorCode, Exception error) {
            return new OperationResult(false, operation, null, message, errorCode, error);
        }
        
        public boolean isSuccess() { return success; }
        public UtilityOperation getOperation() { return operation; }
        public Map<String, Object> getResult() { return result; }
        public String getMessage() { return message; }
        public String getErrorCode() { return errorCode; }
        public Optional<Exception> getError() { return Optional.ofNullable(error); }
    }
    
    /**
     * Batch operation result
     */
    class BatchOperationResult {
        private final int totalOperations;
        private final int successfulOperations;
        private final int failedOperations;
        private final List<OperationResult> results;
        private final long totalExecutionTimeMs;
        
        public BatchOperationResult(int totalOperations, int successfulOperations, int failedOperations,
                                   List<OperationResult> results, long totalExecutionTimeMs) {
            this.totalOperations = totalOperations;
            this.successfulOperations = successfulOperations;
            this.failedOperations = failedOperations;
            this.results = results;
            this.totalExecutionTimeMs = totalExecutionTimeMs;
        }
        
        public int getTotalOperations() { return totalOperations; }
        public int getSuccessfulOperations() { return successfulOperations; }
        public int getFailedOperations() { return failedOperations; }
        public List<OperationResult> getResults() { return results; }
        public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
        
        public double getSuccessRate() {
            return totalOperations > 0 ? (double) successfulOperations / totalOperations : 0.0;
        }
    }
    
    /**
     * Operation statistics
     */
    class OperationStatistics {
        private final String userId;
        private final long totalOperations;
        private final long successfulOperations;
        private final long failedOperations;
        private final long averageExecutionTimeMs;
        private final Map<UtilityType, Long> operationsByType;
        private final Map<OperationStatus, Long> operationsByStatus;
        private final java.time.LocalDateTime lastOperationTime;
        
        public OperationStatistics(String userId, long totalOperations, long successfulOperations,
                                  long failedOperations, long averageExecutionTimeMs,
                                  Map<UtilityType, Long> operationsByType,
                                  Map<OperationStatus, Long> operationsByStatus,
                                  java.time.LocalDateTime lastOperationTime) {
            this.userId = userId;
            this.totalOperations = totalOperations;
            this.successfulOperations = successfulOperations;
            this.failedOperations = failedOperations;
            this.averageExecutionTimeMs = averageExecutionTimeMs;
            this.operationsByType = operationsByType;
            this.operationsByStatus = operationsByStatus;
            this.lastOperationTime = lastOperationTime;
        }
        
        public String getUserId() { return userId; }
        public long getTotalOperations() { return totalOperations; }
        public long getSuccessfulOperations() { return successfulOperations; }
        public long getFailedOperations() { return failedOperations; }
        public long getAverageExecutionTimeMs() { return averageExecutionTimeMs; }
        public Map<UtilityType, Long> getOperationsByType() { return operationsByType; }
        public Map<OperationStatus, Long> getOperationsByStatus() { return operationsByStatus; }
        public java.time.LocalDateTime getLastOperationTime() { return lastOperationTime; }
        
        public double getSuccessRate() {
            return totalOperations > 0 ? (double) successfulOperations / totalOperations : 0.0;
        }
    }
    
    /**
     * Cleanup result
     */
    class CleanupResult {
        private final int operationsFound;
        private final int operationsRemoved;
        private final long cleanupTimeMs;
        private final String summary;
        
        public CleanupResult(int operationsFound, int operationsRemoved, long cleanupTimeMs, String summary) {
            this.operationsFound = operationsFound;
            this.operationsRemoved = operationsRemoved;
            this.cleanupTimeMs = cleanupTimeMs;
            this.summary = summary;
        }
        
        public int getOperationsFound() { return operationsFound; }
        public int getOperationsRemoved() { return operationsRemoved; }
        public long getCleanupTimeMs() { return cleanupTimeMs; }
        public String getSummary() { return summary; }
    }
    
    /**
     * Utility type information
     */
    class UtilityTypeInfo {
        private final UtilityType type;
        private final String displayName;
        private final boolean cacheable;
        private final boolean retryable;
        private final long defaultTimeoutSeconds;
        private final long maxInputSizeBytes;
        private final boolean requiresAuthentication;
        private final List<String> requiredParameters;
        
        public UtilityTypeInfo(UtilityType type, String displayName, boolean cacheable, boolean retryable,
                              long defaultTimeoutSeconds, long maxInputSizeBytes, boolean requiresAuthentication,
                              List<String> requiredParameters) {
            this.type = type;
            this.displayName = displayName;
            this.cacheable = cacheable;
            this.retryable = retryable;
            this.defaultTimeoutSeconds = defaultTimeoutSeconds;
            this.maxInputSizeBytes = maxInputSizeBytes;
            this.requiresAuthentication = requiresAuthentication;
            this.requiredParameters = requiredParameters;
        }
        
        public UtilityType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public boolean isCacheable() { return cacheable; }
        public boolean isRetryable() { return retryable; }
        public long getDefaultTimeoutSeconds() { return defaultTimeoutSeconds; }
        public long getMaxInputSizeBytes() { return maxInputSizeBytes; }
        public boolean isRequiresAuthentication() { return requiresAuthentication; }
        public List<String> getRequiredParameters() { return requiredParameters; }
    }
    
    /**
     * Parameter validation result
     */
    class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;
        private final String message;
        
        public ValidationResult(boolean valid, List<String> errors, List<String> warnings, String message) {
            this.valid = valid;
            this.errors = errors;
            this.warnings = warnings;
            this.message = message;
        }
        
        public static ValidationResult valid(String message) {
            return new ValidationResult(true, List.of(), List.of(), message);
        }
        
        public static ValidationResult invalid(List<String> errors, String message) {
            return new ValidationResult(false, errors, List.of(), message);
        }
        
        public static ValidationResult validWithWarnings(List<String> warnings, String message) {
            return new ValidationResult(true, List.of(), warnings, message);
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public String getMessage() { return message; }
        public boolean hasWarnings() { return !warnings.isEmpty(); }
    }
    
    /**
     * Queue status
     */
    class QueueStatus {
        private final int queuedOperations;
        private final int runningOperations;
        private final int completedOperations;
        private final int failedOperations;
        private final double averageWaitTimeMs;
        private final int availableWorkers;
        private final int busyWorkers;
        
        public QueueStatus(int queuedOperations, int runningOperations, int completedOperations,
                          int failedOperations, double averageWaitTimeMs, int availableWorkers, int busyWorkers) {
            this.queuedOperations = queuedOperations;
            this.runningOperations = runningOperations;
            this.completedOperations = completedOperations;
            this.failedOperations = failedOperations;
            this.averageWaitTimeMs = averageWaitTimeMs;
            this.availableWorkers = availableWorkers;
            this.busyWorkers = busyWorkers;
        }
        
        public int getQueuedOperations() { return queuedOperations; }
        public int getRunningOperations() { return runningOperations; }
        public int getCompletedOperations() { return completedOperations; }
        public int getFailedOperations() { return failedOperations; }
        public double getAverageWaitTimeMs() { return averageWaitTimeMs; }
        public int getAvailableWorkers() { return availableWorkers; }
        public int getBusyWorkers() { return busyWorkers; }
        
        public int getTotalWorkers() { return availableWorkers + busyWorkers; }
        public boolean isOverloaded() { return queuedOperations > availableWorkers * 2; }
    }
    
    /**
     * Queue processing result
     */
    class ProcessingResult {
        private final int processedOperations;
        private final int skippedOperations;
        private final long processingTimeMs;
        private final String summary;
        
        public ProcessingResult(int processedOperations, int skippedOperations, long processingTimeMs, String summary) {
            this.processedOperations = processedOperations;
            this.skippedOperations = skippedOperations;
            this.processingTimeMs = processingTimeMs;
            this.summary = summary;
        }
        
        public int getProcessedOperations() { return processedOperations; }
        public int getSkippedOperations() { return skippedOperations; }
        public long getProcessingTimeMs() { return processingTimeMs; }
        public String getSummary() { return summary; }
    }
}