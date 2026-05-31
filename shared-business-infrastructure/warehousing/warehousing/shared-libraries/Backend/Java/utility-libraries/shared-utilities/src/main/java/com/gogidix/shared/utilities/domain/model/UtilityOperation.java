package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.model.domain.model.BaseEntity;
import com.gogidix.shared.model.domain.model.EntityStatus;
import com.gogidix.shared.model.domain.model.ValidationResult;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

/**
 * Rich Domain Model for Utility Operation - HEXAGONAL ARCHITECTURE TEMPLATE
 * 
 * This serves as the comprehensive utility operation domain model for Agent B services.
 * Demonstrates:
 * - Rich utility business logic and operation management
 * - Zero infrastructure dependencies (NO Lombok, JPA, etc.)
 * - Immutable design with builder pattern
 * - Utility processing, caching, and retry mechanisms
 * - Performance monitoring and resource management
 * 
 * NOTE: @Getter added to generate accessors for final fields
 * 
 * @author Agent A - Foundation Lead
 * @template-for Agent B Utility Services
 * @version 1.0.0
 */
@Getter
public class UtilityOperation extends BaseEntity {
    
    // Operation Identity (immutable)
    private final String operationId;
    private final UtilityType operationType;
    private final OperationStatus operationStatus;
    private final String operationName;
    private final String operationDescription;
    
    // Operation Data (immutable)
    private final Map<String, Object> inputData;
    private final Map<String, Object> outputData;
    private final Map<String, Object> processingContext;
    private final Map<String, String> operationMetadata;
    
    // Timing Information (immutable)
    private final LocalDateTime operationStartTime;
    private final LocalDateTime operationEndTime;
    private final LocalDateTime scheduledTime;
    private final Long executionTimeMs;
    private final Long queueTimeMs;
    private final Long totalProcessingTimeMs;
    
    // User and Session Context (immutable)
    private final String userId;
    private final String sessionId;
    private final String correlationId;
    private final String requestId;
    private final String clientId;
    
    // Priority and Processing (immutable)
    private final OperationPriority priority;
    private final int retryCount;
    private final int maxRetries;
    private final double retryBackoffMultiplier;
    private final boolean allowPreemption;
    
    // Error Handling (immutable)
    private final String errorMessage;
    private final String errorCode;
    private final String errorStackTrace;
    private final List<String> errorHistory;
    private final String recoverySuggestion;
    
    // Caching Configuration (immutable)
    private final boolean cached;
    private final String cacheKey;
    private final Long cacheTtlSeconds;
    private final String cacheRegion;
    private final boolean cacheWriteThrough;
    
    // Resource Management (immutable)
    private final long maxMemoryBytes;
    private final long actualMemoryUsedBytes;
    private final int maxCpuCores;
    private final double cpuUsagePercent;
    private final long maxExecutionTimeMs;
    
    // Monitoring and Metrics (immutable)
    private final int progressPercentage;
    private final String currentPhase;
    private final long itemsProcessed;
    private final long totalItemsToProcess;
    private final String performanceProfile;
    
    // Constructor for UtilityOperation creation
    public UtilityOperation(UUID id, Long version, LocalDateTime createdAt, String createdBy,
                           LocalDateTime updatedAt, String updatedBy, boolean deleted,
                           LocalDateTime deletedAt, String deletedBy, String deletionReason,
                           String operationId, UtilityType operationType, OperationStatus operationStatus,
                           String operationName, String operationDescription,
                           Map<String, Object> inputData, Map<String, Object> outputData, 
                           Map<String, Object> processingContext, Map<String, String> operationMetadata,
                           LocalDateTime operationStartTime, LocalDateTime operationEndTime, 
                           LocalDateTime scheduledTime, Long executionTimeMs, Long queueTimeMs, 
                           Long totalProcessingTimeMs,
                           String userId, String sessionId, String correlationId, String requestId, 
                           String clientId,
                           OperationPriority priority, int retryCount, int maxRetries, 
                           double retryBackoffMultiplier, boolean allowPreemption,
                           String errorMessage, String errorCode, String errorStackTrace, 
                           List<String> errorHistory, String recoverySuggestion,
                           boolean cached, String cacheKey, Long cacheTtlSeconds, String cacheRegion, 
                           boolean cacheWriteThrough,
                           long maxMemoryBytes, long actualMemoryUsedBytes, int maxCpuCores, 
                           double cpuUsagePercent, long maxExecutionTimeMs,
                           int progressPercentage, String currentPhase, long itemsProcessed, 
                           long totalItemsToProcess, String performanceProfile) {
        
        super(id, version, "UTILITY_OPERATION", createdAt, createdBy, updatedAt, updatedBy,
              deleted, deletedAt, deletedBy, deletionReason,
              mapOperationStatusToEntityStatus(operationStatus), null, null, null);
              
        this.operationId = operationId;
        this.operationType = operationType;
        this.operationStatus = operationStatus != null ? operationStatus : OperationStatus.QUEUED;
        this.operationName = operationName;
        this.operationDescription = operationDescription;
        this.inputData = inputData != null ? new HashMap<>(inputData) : new HashMap<>();
        this.outputData = outputData != null ? new HashMap<>(outputData) : new HashMap<>();
        this.processingContext = processingContext != null ? new HashMap<>(processingContext) : new HashMap<>();
        this.operationMetadata = operationMetadata != null ? new HashMap<>(operationMetadata) : new HashMap<>();
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;
        this.scheduledTime = scheduledTime;
        this.executionTimeMs = executionTimeMs;
        this.queueTimeMs = queueTimeMs;
        this.totalProcessingTimeMs = totalProcessingTimeMs;
        this.userId = userId;
        this.sessionId = sessionId;
        this.correlationId = correlationId;
        this.requestId = requestId;
        this.clientId = clientId;
        this.priority = priority != null ? priority : OperationPriority.NORMAL;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries > 0 ? maxRetries : 3;
        this.retryBackoffMultiplier = retryBackoffMultiplier > 0 ? retryBackoffMultiplier : 1.5;
        this.allowPreemption = allowPreemption;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorStackTrace = errorStackTrace;
        this.errorHistory = errorHistory != null ? new ArrayList<>(errorHistory) : new ArrayList<>();
        this.recoverySuggestion = recoverySuggestion;
        this.cached = cached;
        this.cacheKey = cacheKey;
        this.cacheTtlSeconds = cacheTtlSeconds;
        this.cacheRegion = cacheRegion;
        this.cacheWriteThrough = cacheWriteThrough;
        this.maxMemoryBytes = maxMemoryBytes;
        this.actualMemoryUsedBytes = actualMemoryUsedBytes;
        this.maxCpuCores = maxCpuCores;
        this.cpuUsagePercent = cpuUsagePercent;
        this.maxExecutionTimeMs = maxExecutionTimeMs;
        this.progressPercentage = Math.max(0, Math.min(100, progressPercentage));
        this.currentPhase = currentPhase;
        this.itemsProcessed = itemsProcessed;
        this.totalItemsToProcess = totalItemsToProcess;
        this.performanceProfile = performanceProfile;
    }
    
    // ==============================================
    // UTILITY OPERATION BUSINESS LOGIC METHODS
    // ==============================================
    
    /**
     * Business Rule: Validate if the operation is properly configured
     */
    @Override
    public boolean isValid() {
        return operationId != null && !operationId.trim().isEmpty() &&
               operationType != null &&
               operationName != null && !operationName.trim().isEmpty() &&
               inputData != null &&
               operationStatus != null &&
               priority != null;
    }
    
    /**
     * Business Rule: Check if operation is in progress
     */
    public boolean isInProgress() {
        return operationStatus.isActive() || operationStatus.isPending();
    }
    
    /**
     * Business Rule: Check if operation completed successfully
     */
    public boolean isCompleted() {
        return operationStatus == OperationStatus.COMPLETED;
    }
    
    /**
     * Business Rule: Check if operation has failed
     */
    public boolean hasFailed() {
        return operationStatus.isFailure();
    }
    
    /**
     * Business Rule: Check if operation can be retried
     */
    public boolean canRetry() {
        return hasFailed() && retryCount < maxRetries && operationType.isRetryable();
    }
    
    /**
     * Business Rule: Check if operation is high priority
     */
    public boolean isHighPriority() {
        return priority.requiresImmediateProcessing();
    }
    
    /**
     * Business Rule: Check if operation is resource intensive
     */
    public boolean isResourceIntensive() {
        return operationType.isResourceIntensive() || 
               actualMemoryUsedBytes > (1024 * 1024 * 100) || // > 100MB
               cpuUsagePercent > 80.0;
    }
    
    /**
     * Business Calculation: Get total execution time including queue time
     */
    public long getTotalExecutionTimeMs() {
        if (totalProcessingTimeMs != null) return totalProcessingTimeMs;
        if (operationStartTime == null || operationEndTime == null) return 0L;
        return Duration.between(operationStartTime, operationEndTime).toMillis();
    }
    
    /**
     * Business Calculation: Get queue time in milliseconds
     */
    public long getQueueTimeMs() {
        if (queueTimeMs != null) return queueTimeMs;
        if (scheduledTime == null || operationStartTime == null) return 0L;
        return Duration.between(scheduledTime, operationStartTime).toMillis();
    }
    
    /**
     * Business Logic: Start the operation
     */
    public UtilityOperation startOperation(String startedBy) {
        if (!operationStatus.canTransitionTo(OperationStatus.RUNNING)) {
            throw new IllegalStateException("Cannot start operation in status: " + operationStatus);
        }
        
        LocalDateTime now = LocalDateTime.now();
        return new UtilityOperation(
            getId(), getVersion(), getCreatedAt(), getCreatedBy(), now, startedBy,
            isDeleted(), getDeletedAt(), getDeletedBy(), getDeletionReason(),
            operationId, operationType, OperationStatus.RUNNING, operationName, operationDescription,
            inputData, outputData, processingContext, operationMetadata,
            now, operationEndTime, scheduledTime, executionTimeMs, getQueueTimeMs(), totalProcessingTimeMs,
            userId, sessionId, correlationId, requestId, clientId,
            priority, retryCount, maxRetries, retryBackoffMultiplier, allowPreemption,
            null, null, null, errorHistory, recoverySuggestion,
            cached, cacheKey, cacheTtlSeconds, cacheRegion, cacheWriteThrough,
            maxMemoryBytes, actualMemoryUsedBytes, maxCpuCores, cpuUsagePercent, maxExecutionTimeMs,
            0, "STARTING", itemsProcessed, totalItemsToProcess, performanceProfile
        );
    }
    
    /**
     * Business Logic: Complete the operation successfully
     */
    public UtilityOperation completeOperation(Map<String, Object> result, String completedBy) {
        if (!operationStatus.canTransitionTo(OperationStatus.COMPLETED)) {
            throw new IllegalStateException("Cannot complete operation in status: " + operationStatus);
        }
        
        LocalDateTime now = LocalDateTime.now();
        long executionTime = operationStartTime != null ? 
            Duration.between(operationStartTime, now).toMillis() : 0L;
        
        return new UtilityOperation(
            getId(), getVersion(), getCreatedAt(), getCreatedBy(), now, completedBy,
            isDeleted(), getDeletedAt(), getDeletedBy(), getDeletionReason(),
            operationId, operationType, OperationStatus.COMPLETED, operationName, operationDescription,
            inputData, result != null ? result : outputData, processingContext, operationMetadata,
            operationStartTime, now, scheduledTime, executionTime, queueTimeMs, executionTime,
            userId, sessionId, correlationId, requestId, clientId,
            priority, retryCount, maxRetries, retryBackoffMultiplier, allowPreemption,
            null, null, null, errorHistory, recoverySuggestion,
            cached, cacheKey, cacheTtlSeconds, cacheRegion, cacheWriteThrough,
            maxMemoryBytes, actualMemoryUsedBytes, maxCpuCores, cpuUsagePercent, maxExecutionTimeMs,
            100, "COMPLETED", totalItemsToProcess, totalItemsToProcess, performanceProfile
        );
    }
    
    /**
     * Business Logic: Fail the operation with error details
     */
    public UtilityOperation failOperation(String errorMsg, String errorCd, String stackTrace, String failedBy) {
        LocalDateTime now = LocalDateTime.now();
        long executionTime = operationStartTime != null ? 
            Duration.between(operationStartTime, now).toMillis() : 0L;
        
        List<String> newErrorHistory = new ArrayList<>(errorHistory);
        newErrorHistory.add(now + ": " + errorMsg);
        
        return new UtilityOperation(
            getId(), getVersion(), getCreatedAt(), getCreatedBy(), now, failedBy,
            isDeleted(), getDeletedAt(), getDeletedBy(), getDeletionReason(),
            operationId, operationType, OperationStatus.FAILED, operationName, operationDescription,
            inputData, outputData, processingContext, operationMetadata,
            operationStartTime, now, scheduledTime, executionTime, queueTimeMs, executionTime,
            userId, sessionId, correlationId, requestId, clientId,
            priority, retryCount, maxRetries, retryBackoffMultiplier, allowPreemption,
            errorMsg, errorCd, stackTrace, newErrorHistory, generateRecoverySuggestion(errorCd),
            cached, cacheKey, cacheTtlSeconds, cacheRegion, cacheWriteThrough,
            maxMemoryBytes, actualMemoryUsedBytes, maxCpuCores, cpuUsagePercent, maxExecutionTimeMs,
            progressPercentage, "FAILED", itemsProcessed, totalItemsToProcess, performanceProfile
        );
    }
    
    /**
     * Business Logic: Retry the operation with backoff
     */
    public UtilityOperation retryOperation(String retryBy) {
        if (!canRetry()) {
            throw new IllegalStateException("Operation cannot be retried");
        }
        
        long backoffMs = calculateRetryBackoff();
        LocalDateTime retryTime = LocalDateTime.now().plusNanos(backoffMs * 1_000_000);
        
        return new UtilityOperation(
            getId(), getVersion(), getCreatedAt(), getCreatedBy(), LocalDateTime.now(), retryBy,
            isDeleted(), getDeletedAt(), getDeletedBy(), getDeletionReason(),
            operationId, operationType, OperationStatus.RETRYING, operationName, operationDescription,
            inputData, outputData, processingContext, operationMetadata,
            null, null, retryTime, null, null, null,
            userId, sessionId, correlationId, requestId, clientId,
            priority, retryCount + 1, maxRetries, retryBackoffMultiplier, allowPreemption,
            null, null, null, errorHistory, recoverySuggestion,
            cached, cacheKey, cacheTtlSeconds, cacheRegion, cacheWriteThrough,
            maxMemoryBytes, actualMemoryUsedBytes, maxCpuCores, cpuUsagePercent, maxExecutionTimeMs,
            0, "RETRYING", itemsProcessed, totalItemsToProcess, performanceProfile
        );
    }
    
    /**
     * Business Logic: Cancel the operation
     */
    public UtilityOperation cancelOperation(String reason, String cancelledBy) {
        if (!operationStatus.canCancel()) {
            throw new IllegalStateException("Cannot cancel operation in status: " + operationStatus);
        }
        
        return new UtilityOperation(
            getId(), getVersion(), getCreatedAt(), getCreatedBy(), LocalDateTime.now(), cancelledBy,
            isDeleted(), getDeletedAt(), getDeletedBy(), getDeletionReason(),
            operationId, operationType, OperationStatus.CANCELLED, operationName, operationDescription,
            inputData, outputData, processingContext, operationMetadata,
            operationStartTime, LocalDateTime.now(), scheduledTime, executionTimeMs, queueTimeMs, totalProcessingTimeMs,
            userId, sessionId, correlationId, requestId, clientId,
            priority, retryCount, maxRetries, retryBackoffMultiplier, allowPreemption,
            "Cancelled: " + reason, "CANCELLED", null, errorHistory, recoverySuggestion,
            cached, cacheKey, cacheTtlSeconds, cacheRegion, cacheWriteThrough,
            maxMemoryBytes, actualMemoryUsedBytes, maxCpuCores, cpuUsagePercent, maxExecutionTimeMs,
            progressPercentage, "CANCELLED", itemsProcessed, totalItemsToProcess, performanceProfile
        );
    }
    
    /**
     * Business Logic: Update operation progress
     */
    public UtilityOperation updateProgress(int progress, String phase, long processed, String updatedBy) {
        return new UtilityOperation(
            getId(), getVersion(), getCreatedAt(), getCreatedBy(), LocalDateTime.now(), updatedBy,
            isDeleted(), getDeletedAt(), getDeletedBy(), getDeletionReason(),
            operationId, operationType, operationStatus, operationName, operationDescription,
            inputData, outputData, processingContext, operationMetadata,
            operationStartTime, operationEndTime, scheduledTime, executionTimeMs, queueTimeMs, totalProcessingTimeMs,
            userId, sessionId, correlationId, requestId, clientId,
            priority, retryCount, maxRetries, retryBackoffMultiplier, allowPreemption,
            errorMessage, errorCode, errorStackTrace, errorHistory, recoverySuggestion,
            cached, cacheKey, cacheTtlSeconds, cacheRegion, cacheWriteThrough,
            maxMemoryBytes, actualMemoryUsedBytes, maxCpuCores, cpuUsagePercent, maxExecutionTimeMs,
            Math.max(0, Math.min(100, progress)), phase, processed, totalItemsToProcess, performanceProfile
        );
    }
    
    /**
     * Business Logic: Get input parameter with type safety
     */
    public Object getInputParameter(String key) {
        return inputData.get(key);
    }
    
    /**
     * Business Logic: Get string parameter with default
     */
    public String getStringParameter(String key, String defaultValue) {
        Object value = getInputParameter(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Business Logic: Get integer parameter with default
     */
    public Integer getIntParameter(String key, Integer defaultValue) {
        Object value = getInputParameter(key);
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Business Logic: Check if operation should use caching
     */
    public boolean shouldUseCache() {
        return cached && cacheKey != null && !cacheKey.trim().isEmpty() && operationType.isCacheable();
    }
    
    /**
     * Business Logic: Generate cache key if not set
     */
    public String generateCacheKey() {
        if (cacheKey != null && !cacheKey.trim().isEmpty()) {
            return cacheKey;
        }
        
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append("utility_").append(operationType.name().toLowerCase());
        keyBuilder.append("_").append(operationName.replaceAll("[^a-zA-Z0-9]", "_"));
        
        if (!inputData.isEmpty()) {
            int hashCode = inputData.toString().hashCode();
            keyBuilder.append("_").append(Math.abs(hashCode));
        }
        
        return keyBuilder.toString();
    }
    
    /**
     * Business Rule: Check if operation requires authentication
     */
    public boolean requiresAuthentication() {
        return operationType.requiresAuthentication() || (userId != null && !userId.trim().isEmpty());
    }
    
    /**
     * Business Calculation: Calculate operation complexity score
     */
    public int getComplexityScore() {
        int score = operationType.getComplexityWeight();
        
        // Input data complexity
        score += Math.min(inputData.size(), 10);
        
        // Priority adjustment
        score += priority.getLevel() * 5;
        
        // Resource usage
        if (actualMemoryUsedBytes > 0) {
            score += Math.min((int)(actualMemoryUsedBytes / (1024 * 1024)), 50); // MB to score
        }
        
        return score;
    }
    
    /**
     * Business Rule: Check if operation is overdue
     */
    public boolean isOverdue() {
        if (scheduledTime == null) return false;
        long expectedDurationMs = operationType.getDefaultTimeoutSeconds() * 1000L;
        return LocalDateTime.now().isAfter(scheduledTime.plusNanos(expectedDurationMs * 1_000_000));
    }
    
    /**
     * Business Calculation: Calculate performance efficiency score
     */
    public double getPerformanceEfficiency() {
        if (totalItemsToProcess == 0) return 100.0;
        
        double completionRate = (double) itemsProcessed / totalItemsToProcess * 100;
        double timeEfficiency = 100.0;
        
        if (executionTimeMs != null && executionTimeMs > 0) {
            long expectedTime = operationType.getDefaultTimeoutSeconds() * 1000L;
            timeEfficiency = Math.min(100.0, (double) expectedTime / executionTimeMs * 100);
        }
        
        return (completionRate + timeEfficiency) / 2.0;
    }
    
    /**
     * Business Logic: Generate operation summary for monitoring
     */
    public OperationSummary getOperationSummary() {
        return new OperationSummary(
            operationId,
            operationType,
            operationStatus,
            priority,
            progressPercentage,
            currentPhase,
            getTotalExecutionTimeMs(),
            getComplexityScore(),
            getPerformanceEfficiency(),
            errorMessage,
            requiresAuthentication(),
            shouldUseCache()
        );
    }
    
    /**
     * Business Logic: Check if operation is eligible for preemption
     */
    public boolean isPreemptible() {
        return !priority.allowsPreemption() && !isResourceIntensive() && operationType.isRetryable();
    }
    
    /**
     * Business Validation: Check operation completeness
     */
    @Override
    public boolean isComplete() {
        return isValid() &&
               operationStatus.isFinished() &&
               (operationStatus.isSuccess() ? outputData != null : errorMessage != null);
    }
    
    /**
     * Business Rule Validation: Validate business rules
     */
    @Override
    public ValidationResult validateBusinessRules() {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Validate operation configuration
        if (maxRetries > 10) {
            warnings.add("High retry count may impact performance");
        }
        
        if (actualMemoryUsedBytes > maxMemoryBytes) {
            errors.add("Memory usage exceeds allocation");
        }
        
        if (operationType.requiresAuthentication() && (userId == null || userId.trim().isEmpty())) {
            errors.add("Authentication required but user ID not provided");
        }
        
        if (totalItemsToProcess > 0 && itemsProcessed > totalItemsToProcess) {
            warnings.add("Items processed exceeds expected total");
        }
        
        if (errors.isEmpty()) {
            return warnings.isEmpty() ? ValidationResult.valid() : ValidationResult.withWarnings(warnings);
        } else {
            return ValidationResult.invalid(errors);
        }
    }
    
    // Private helper methods
    private long calculateRetryBackoff() {
        return (long) (Math.pow(retryBackoffMultiplier, retryCount) * 1000); // Base 1 second
    }
    
    private String generateRecoverySuggestion(String errorCode) {
        return switch (errorCode) {
            case "MEMORY_ERROR" -> "Reduce input data size or increase memory allocation";
            case "TIMEOUT" -> "Optimize operation or increase timeout threshold";
            case "AUTH_ERROR" -> "Verify authentication credentials and permissions";
            case "VALIDATION_ERROR" -> "Check input data format and requirements";
            default -> "Review error details and retry with corrected parameters";
        };
    }
    
    private static EntityStatus mapOperationStatusToEntityStatus(OperationStatus opStatus) {
        return switch (opStatus) {
            case QUEUED -> EntityStatus.DRAFT;
            case RUNNING, RETRYING -> EntityStatus.PROCESSING;
            case COMPLETED -> EntityStatus.ACTIVE;
            case PAUSED -> EntityStatus.SUSPENDED;
            case FAILED, ERROR, TIMEOUT -> EntityStatus.ERROR;
            case CANCELLED -> EntityStatus.ARCHIVED;
        };
    }
    
    // Getters (immutable access)
    public String getOperationId() { return operationId; }
    public UtilityType getOperationType() { return operationType; }
    public OperationStatus getOperationStatus() { return operationStatus; }
    public String getOperationName() { return operationName; }
    public String getOperationDescription() { return operationDescription; }
    public Map<String, Object> getInputData() { return new HashMap<>(inputData); }
    public Map<String, Object> getOutputData() { return new HashMap<>(outputData); }
    public Map<String, Object> getProcessingContext() { return new HashMap<>(processingContext); }
    public Map<String, String> getOperationMetadata() { return new HashMap<>(operationMetadata); }
    public LocalDateTime getOperationStartTime() { return operationStartTime; }
    public LocalDateTime getOperationEndTime() { return operationEndTime; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public Long getTotalProcessingTimeMs() { return totalProcessingTimeMs; }
    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public String getCorrelationId() { return correlationId; }
    public String getRequestId() { return requestId; }
    public String getClientId() { return clientId; }
    public OperationPriority getPriority() { return priority; }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public double getRetryBackoffMultiplier() { return retryBackoffMultiplier; }
    public boolean isAllowPreemption() { return allowPreemption; }
    public String getErrorMessage() { return errorMessage; }
    public String getErrorCode() { return errorCode; }
    public String getErrorStackTrace() { return errorStackTrace; }
    public List<String> getErrorHistory() { return new ArrayList<>(errorHistory); }
    public String getRecoverySuggestion() { return recoverySuggestion; }
    public boolean isCached() { return cached; }
    public String getCacheKey() { return cacheKey; }
    public Long getCacheTtlSeconds() { return cacheTtlSeconds; }
    public String getCacheRegion() { return cacheRegion; }
    public boolean isCacheWriteThrough() { return cacheWriteThrough; }
    public long getMaxMemoryBytes() { return maxMemoryBytes; }
    public long getActualMemoryUsedBytes() { return actualMemoryUsedBytes; }
    public int getMaxCpuCores() { return maxCpuCores; }
    public double getCpuUsagePercent() { return cpuUsagePercent; }
    public long getMaxExecutionTimeMs() { return maxExecutionTimeMs; }
    public int getProgressPercentage() { return progressPercentage; }
    public String getCurrentPhase() { return currentPhase; }
    public long getItemsProcessed() { return itemsProcessed; }
    public long getTotalItemsToProcess() { return totalItemsToProcess; }
    public String getPerformanceProfile() { return performanceProfile; }
    
    @Override
    public String getEntityType() {
        return "UTILITY_OPERATION";
    }
}

/**
 * Operation Summary record for monitoring and reporting
 */
record OperationSummary(
    String operationId,
    UtilityType operationType,
    OperationStatus operationStatus,
    OperationPriority priority,
    int progressPercentage,
    String currentPhase,
    long totalExecutionTimeMs,
    int complexityScore,
    double performanceEfficiency,
    String errorMessage,
    boolean requiresAuthentication,
    boolean usesCaching
) {}