package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.ProcessingStatus;
import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Generic result object for utility operations
 * Represents the outcome of any utility processing operation
 */
@Data
@Builder(toBuilder = true)
public class UtilityResult<T> {
    
    private final String operationId;
    private final UtilityType utilityType;
    private final ProcessingStatus status;
    private final T result;
    private final String errorMessage;
    private final List<String> warnings;
    private final Map<String, Object> metadata;
    private final Instant processedAt;
    private final long processingTimeMs;
    
    /**
     * Creates a successful utility result
     */
    public static <T> UtilityResult<T> success(String operationId, UtilityType utilityType, T result) {
        return UtilityResult.<T>builder()
                .operationId(operationId)
                .utilityType(utilityType)
                .status(ProcessingStatus.SUCCESS)
                .result(result)
                .processedAt(Instant.now())
                .build();
    }
    
    /**
     * Creates a failed utility result
     */
    public static <T> UtilityResult<T> failure(String operationId, UtilityType utilityType, String errorMessage) {
        return UtilityResult.<T>builder()
                .operationId(operationId)
                .utilityType(utilityType)
                .status(ProcessingStatus.FAILED)
                .errorMessage(errorMessage)
                .processedAt(Instant.now())
                .build();
    }
    
    /**
     * Creates a result with warnings
     */
    public static <T> UtilityResult<T> withWarnings(String operationId, UtilityType utilityType, T result, List<String> warnings) {
        return UtilityResult.<T>builder()
                .operationId(operationId)
                .utilityType(utilityType)
                .status(ProcessingStatus.SUCCESS_WITH_WARNINGS)
                .result(result)
                .warnings(warnings)
                .processedAt(Instant.now())
                .build();
    }
    
    /**
     * Checks if the operation was successful
     */
    public boolean isSuccessful() {
        return status == ProcessingStatus.SUCCESS || status == ProcessingStatus.SUCCESS_WITH_WARNINGS;
    }
    
    /**
     * Checks if the operation failed
     */
    public boolean isFailed() {
        return status == ProcessingStatus.FAILED;
    }
    
    /**
     * Checks if the operation has warnings
     */
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
}