package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Pure domain model for utility operation results with zero external dependencies.
 * Represents the outcome of any utility processing operation with comprehensive business logic.
 * NOTE: @Getter added to generate accessors for final fields.
 */
@Getter
public class UtilityResultClean<T> {
    
    private final String operationId;
    private final UtilityType utilityType;
    private final ProcessingStatus status;
    private final T result;
    private final String errorMessage;
    private final List<String> warnings;
    private final Map<String, Object> metadata;
    private final Instant processedAt;
    private final long processingTimeMs;
    private final String processingNode;
    private final int retryCount;
    private final Instant createdAt;
    
    public UtilityResultClean(String operationId, UtilityType utilityType, ProcessingStatus status,
                             T result, String errorMessage, List<String> warnings, 
                             Map<String, Object> metadata, Instant processedAt, long processingTimeMs,
                             String processingNode, int retryCount, Instant createdAt) {
        
        this.operationId = validateOperationId(operationId);
        this.utilityType = Objects.requireNonNull(utilityType, "Utility type cannot be null");
        this.status = Objects.requireNonNull(status, "Processing status cannot be null");
        this.result = result;
        this.errorMessage = errorMessage;
        this.warnings = warnings != null ? new ArrayList<>(warnings) : new ArrayList<>();
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        this.processedAt = Objects.requireNonNull(processedAt, "Processed time cannot be null");
        this.processingTimeMs = Math.max(0, processingTimeMs);
        this.processingNode = processingNode;
        this.retryCount = Math.max(0, retryCount);
        this.createdAt = createdAt != null ? createdAt : processedAt;
        
        validateResultConsistency();
    }
    
    // ==============================================
    // STATIC FACTORY METHODS
    // ==============================================
    
    /**
     * Creates a successful utility result
     */
    public static <T> UtilityResultClean<T> success(String operationId, UtilityType utilityType, T result) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.SUCCESS, result, null, null, null,
            Instant.now(), 0, null, 0, Instant.now()
        );
    }
    
    /**
     * Creates a successful result with processing time
     */
    public static <T> UtilityResultClean<T> success(String operationId, UtilityType utilityType, 
                                                    T result, long processingTimeMs) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.SUCCESS, result, null, null, null,
            Instant.now(), processingTimeMs, null, 0, Instant.now()
        );
    }
    
    /**
     * Creates a failed utility result
     */
    public static <T> UtilityResultClean<T> failure(String operationId, UtilityType utilityType, String errorMessage) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.FAILED, null, errorMessage, null, null,
            Instant.now(), 0, null, 0, Instant.now()
        );
    }
    
    /**
     * Creates a failed result with processing time and retry count
     */
    public static <T> UtilityResultClean<T> failure(String operationId, UtilityType utilityType, 
                                                    String errorMessage, long processingTimeMs, int retryCount) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.FAILED, null, errorMessage, null, null,
            Instant.now(), processingTimeMs, null, retryCount, Instant.now()
        );
    }
    
    /**
     * Creates a result with warnings
     */
    public static <T> UtilityResultClean<T> withWarnings(String operationId, UtilityType utilityType, 
                                                         T result, List<String> warnings) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.SUCCESS_WITH_WARNINGS, result, null, warnings, null,
            Instant.now(), 0, null, 0, Instant.now()
        );
    }
    
    /**
     * Creates a result with metadata
     */
    public static <T> UtilityResultClean<T> withMetadata(String operationId, UtilityType utilityType, 
                                                         T result, Map<String, Object> metadata) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.SUCCESS, result, null, null, metadata,
            Instant.now(), 0, null, 0, Instant.now()
        );
    }
    
    /**
     * Creates a processing result (in progress)
     */
    public static <T> UtilityResultClean<T> processing(String operationId, UtilityType utilityType) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.PROCESSING, null, null, null, null,
            Instant.now(), 0, null, 0, Instant.now()
        );
    }
    
    /**
     * Creates a timeout result
     */
    public static <T> UtilityResultClean<T> timeout(String operationId, UtilityType utilityType, long timeoutMs) {
        return new UtilityResultClean<>(
            operationId, utilityType, ProcessingStatus.TIMEOUT, null, 
            "Operation timed out after " + timeoutMs + "ms", null, 
            Map.of("timeoutMs", timeoutMs), Instant.now(), timeoutMs, null, 0, Instant.now()
        );
    }
    
    // ==============================================
    // BUSINESS LOGIC METHODS
    // ==============================================
    
    /**
     * Business Rule: Checks if the operation was successful
     */
    public boolean isSuccessful() {
        return status == ProcessingStatus.SUCCESS || status == ProcessingStatus.SUCCESS_WITH_WARNINGS;
    }
    
    /**
     * Business Rule: Checks if the operation failed
     */
    public boolean isFailed() {
        return status == ProcessingStatus.FAILED;
    }
    
    /**
     * Business Rule: Checks if the operation is still processing
     */
    public boolean isProcessing() {
        return status == ProcessingStatus.PROCESSING;
    }
    
    /**
     * Business Rule: Checks if the operation timed out
     */
    public boolean isTimeout() {
        return status == ProcessingStatus.TIMEOUT;
    }
    
    /**
     * Business Rule: Checks if the operation was cancelled
     */
    public boolean isCancelled() {
        return status == ProcessingStatus.CANCELLED;
    }
    
    /**
     * Business Rule: Checks if the operation has warnings
     */
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
    
    /**
     * Business Rule: Checks if the operation has a result
     */
    public boolean hasResult() {
        return isSuccessful() && result != null;
    }
    
    /**
     * Business Rule: Checks if the operation has metadata
     */
    public boolean hasMetadata() {
        return metadata != null && !metadata.isEmpty();
    }
    
    /**
     * Business Rule: Checks if this was a retry operation
     */
    public boolean wasRetried() {
        return retryCount > 0;
    }
    
    /**
     * Business Rule: Checks if operation was slow (took longer than threshold)
     */
    public boolean isSlowOperation() {
        return processingTimeMs > getSlowOperationThreshold();
    }
    
    /**
     * Business Rule: Checks if operation was very fast (likely cached)
     */
    public boolean isFastOperation() {
        return processingTimeMs < 10; // Less than 10ms
    }
    
    /**
     * Business Rule: Checks if operation requires attention (failed or has warnings)
     */
    public boolean requiresAttention() {
        return isFailed() || hasWarnings() || isTimeout();
    }
    
    /**
     * Business Rule: Checks if operation is critical based on utility type
     */
    public boolean isCriticalOperation() {
        return utilityType.isCritical();
    }
    
    /**
     * Business Rule: Checks if operation is retryable
     */
    public boolean isRetryable() {
        return isFailed() && 
               !isTimeout() && 
               retryCount < getMaxRetryCount() &&
               hasRetryableError();
    }
    
    // ==============================================
    // UTILITY CALCULATION METHODS
    // ==============================================
    
    /**
     * Business Calculation: Get total processing duration
     */
    public Duration getTotalProcessingDuration() {
        return Duration.between(createdAt, processedAt);
    }
    
    /**
     * Business Calculation: Get processing efficiency score (0-100)
     */
    public int getEfficiencyScore() {
        if (!isSuccessful()) return 0;
        
        int score = 100;
        
        // Penalize for slow operations
        if (isSlowOperation()) {
            score -= Math.min(50, (int) (processingTimeMs / 1000)); // -1 per second
        }
        
        // Penalize for warnings
        if (hasWarnings()) {
            score -= Math.min(20, warnings.size() * 5); // -5 per warning, max -20
        }
        
        // Penalize for retries
        score -= Math.min(30, retryCount * 10); // -10 per retry, max -30
        
        return Math.max(0, score);
    }
    
    /**
     * Business Calculation: Get operation priority score for queuing
     */
    public int getPriorityScore() {
        int score = utilityType.getPriorityScore();
        
        // Boost for retries (indicates importance)
        score += retryCount * 10;
        
        // Boost for critical operations
        if (isCriticalOperation()) {
            score += 25;
        }
        
        return Math.min(100, score);
    }
    
    /**
     * Business Calculation: Get processing throughput (operations per second)
     */
    public double getProcessingThroughput() {
        if (processingTimeMs == 0) return Double.MAX_VALUE;
        return 1000.0 / processingTimeMs; // ops per second
    }
    
    /**
     * Business Calculation: Estimate memory footprint
     */
    public long getEstimatedMemoryFootprint() {
        long footprint = 200; // Base overhead
        
        if (result != null) {
            footprint += estimateObjectSize(result);
        }
        
        footprint += warnings.size() * 50; // Average warning string size
        footprint += metadata.size() * 100; // Average metadata entry size
        
        if (errorMessage != null) {
            footprint += errorMessage.length() * 2; // Unicode chars
        }
        
        return footprint;
    }
    
    // ==============================================
    // TRANSFORMATION METHODS
    // ==============================================
    
    /**
     * Transform result to a different type
     */
    public <U> UtilityResultClean<U> map(java.util.function.Function<T, U> mapper) {
        if (!hasResult()) {
            return new UtilityResultClean<>(
                this.operationId, this.utilityType, this.status, null, this.errorMessage,
                this.warnings, this.metadata, this.processedAt, this.processingTimeMs,
                this.processingNode, this.retryCount, this.createdAt
            );
        }
        
        try {
            U mappedResult = mapper.apply(this.result);
            return new UtilityResultClean<>(
                this.operationId, this.utilityType, this.status, mappedResult, this.errorMessage,
                this.warnings, this.metadata, this.processedAt, this.processingTimeMs,
                this.processingNode, this.retryCount, this.createdAt
            );
        } catch (Exception e) {
            return UtilityResultClean.failure(this.operationId, this.utilityType, 
                "Mapping failed: " + e.getMessage());
        }
    }
    
    /**
     * Add processing time to existing result
     */
    public UtilityResultClean<T> withProcessingTime(long additionalProcessingTimeMs) {
        return new UtilityResultClean<>(
            this.operationId, this.utilityType, this.status, this.result, this.errorMessage,
            this.warnings, this.metadata, this.processedAt, 
            this.processingTimeMs + additionalProcessingTimeMs,
            this.processingNode, this.retryCount, this.createdAt
        );
    }
    
    /**
     * Add warning to existing result
     */
    public UtilityResultClean<T> withAdditionalWarning(String warning) {
        if (warning == null || warning.trim().isEmpty()) {
            return this;
        }
        
        List<String> newWarnings = new ArrayList<>(this.warnings);
        newWarnings.add(warning.trim());
        
        ProcessingStatus newStatus = this.status == ProcessingStatus.SUCCESS ? 
            ProcessingStatus.SUCCESS_WITH_WARNINGS : this.status;
        
        return new UtilityResultClean<>(
            this.operationId, this.utilityType, newStatus, this.result, this.errorMessage,
            newWarnings, this.metadata, this.processedAt, this.processingTimeMs,
            this.processingNode, this.retryCount, this.createdAt
        );
    }
    
    /**
     * Add metadata to existing result
     */
    public UtilityResultClean<T> withAdditionalMetadata(String key, Object value) {
        Map<String, Object> newMetadata = new HashMap<>(this.metadata);
        newMetadata.put(key, value);
        
        return new UtilityResultClean<>(
            this.operationId, this.utilityType, this.status, this.result, this.errorMessage,
            this.warnings, newMetadata, this.processedAt, this.processingTimeMs,
            this.processingNode, this.retryCount, this.createdAt
        );
    }
    
    /**
     * Create retry version of this result
     */
    public UtilityResultClean<T> createRetryVersion() {
        return new UtilityResultClean<>(
            this.operationId, this.utilityType, ProcessingStatus.PROCESSING, null, null,
            new ArrayList<>(), new HashMap<>(this.metadata), Instant.now(), 0,
            this.processingNode, this.retryCount + 1, this.createdAt
        );
    }
    
    /**
     * Mark as completed with result
     */
    public UtilityResultClean<T> markCompleted(T completedResult, long finalProcessingTimeMs) {
        return new UtilityResultClean<>(
            this.operationId, this.utilityType, ProcessingStatus.SUCCESS, completedResult, null,
            this.warnings, this.metadata, Instant.now(), finalProcessingTimeMs,
            this.processingNode, this.retryCount, this.createdAt
        );
    }
    
    // ==============================================
    // HELPER METHODS
    // ==============================================
    
    private long getSlowOperationThreshold() {
        return switch (utilityType) {
            case JSON_SERIALIZATION, JSON_DESERIALIZATION, JSON_VALIDATION -> 100; // 100ms
            case DATETIME_FORMATTING, DATETIME_PARSING -> 50; // 50ms
            case FILE_EXCEL_PROCESSING, FILE_CSV_PROCESSING -> 1000; // 1s
            case HTTP_REQUEST_PROCESSING, HTTP_RESPONSE_PROCESSING -> 2000; // 2s
            case ENCRYPTION, DECRYPTION, HASHING -> 250; // 250ms
            default -> 500; // 500ms default
        };
    }
    
    private int getMaxRetryCount() {
        return utilityType.isCritical() ? 5 : 3;
    }
    
    private boolean hasRetryableError() {
        if (errorMessage == null) return true;
        
        String[] nonRetryableErrors = {
            "INVALID_INPUT", "VALIDATION_ERROR", "PERMISSION_DENIED", 
            "RESOURCE_NOT_FOUND", "MALFORMED_DATA"
        };
        
        String upperError = errorMessage.toUpperCase();
        for (String error : nonRetryableErrors) {
            if (upperError.contains(error)) return false;
        }
        
        return true;
    }
    
    private long estimateObjectSize(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof String) return ((String) obj).length() * 2;
        if (obj instanceof Number) return 8;
        if (obj instanceof Boolean) return 1;
        return 100; // Default estimate
    }
    
    // ==============================================
    // DOMAIN VALIDATION METHODS
    // ==============================================
    
    private String validateOperationId(String operationId) {
        if (operationId == null || operationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Operation ID cannot be null or empty");
        }
        if (operationId.length() > 100) {
            throw new IllegalArgumentException("Operation ID cannot exceed 100 characters");
        }
        return operationId.trim();
    }
    
    private void validateResultConsistency() {
        if (isSuccessful()) {
            if (errorMessage != null && !errorMessage.trim().isEmpty()) {
                throw new IllegalArgumentException("Successful result cannot have error message");
            }
        } else if (isFailed()) {
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                throw new IllegalArgumentException("Failed result must have error message");
            }
        }
        
        if (status == ProcessingStatus.SUCCESS_WITH_WARNINGS && !hasWarnings()) {
            throw new IllegalArgumentException("Result marked as having warnings but no warnings provided");
        }
    }
    
    // ==============================================
    // GETTERS (NO LOMBOK DEPENDENCY)
    // ==============================================
    
    public String getOperationId() { return operationId; }
    public UtilityType getUtilityType() { return utilityType; }
    public ProcessingStatus getStatus() { return status; }
    public T getResult() { return result; }
    public String getErrorMessage() { return errorMessage; }
    public List<String> getWarnings() { return new ArrayList<>(warnings); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public Instant getProcessedAt() { return processedAt; }
    public long getProcessingTimeMs() { return processingTimeMs; }
    public String getProcessingNode() { return processingNode; }
    public int getRetryCount() { return retryCount; }
    public Instant getCreatedAt() { return createdAt; }
    
    public Object getMetadataValue(String key) {
        return metadata.get(key);
    }
    
    public String getStringMetadata(String key, String defaultValue) {
        Object value = getMetadataValue(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    // ==============================================
    // OBJECT METHODS
    // ==============================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilityResultClean<?> that = (UtilityResultClean<?>) o;
        return Objects.equals(operationId, that.operationId) &&
               Objects.equals(utilityType, that.utilityType) &&
               Objects.equals(status, that.status);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(operationId, utilityType, status);
    }
    
    @Override
    public String toString() {
        return String.format("UtilityResult{id='%s', type=%s, status=%s, processingTime=%dms, retries=%d}", 
                           operationId, utilityType, status, processingTimeMs, retryCount);
    }
}