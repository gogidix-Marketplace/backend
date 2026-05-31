package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

/**
 * Generic processing request for utility operations
 * Represents the input for any utility processing operation
 */
@Data
@Builder
public class ProcessingRequest {
    
    private final String requestId;
    private final UtilityType utilityType;
    private final Object data;
    private final Map<String, Object> parameters;
    private final Map<String, String> context;
    private final String userId;
    private final String sessionId;
    private final Instant createdAt;
    private final Integer priority;
    private final Long timeoutMs;
    
    /**
     * Creates a basic processing request
     */
    public static ProcessingRequest create(UtilityType utilityType, Object data) {
        return ProcessingRequest.builder()
                .requestId(generateRequestId())
                .utilityType(utilityType)
                .data(data)
                .createdAt(Instant.now())
                .priority(5) // Default priority
                .timeoutMs(30000L) // 30 seconds default timeout
                .build();
    }
    
    /**
     * Creates a processing request with parameters
     */
    public static ProcessingRequest withParameters(UtilityType utilityType, Object data, Map<String, Object> parameters) {
        return ProcessingRequest.builder()
                .requestId(generateRequestId())
                .utilityType(utilityType)
                .data(data)
                .parameters(parameters)
                .createdAt(Instant.now())
                .priority(5)
                .timeoutMs(30000L)
                .build();
    }
    
    /**
     * Creates a high-priority processing request
     */
    public static ProcessingRequest highPriority(UtilityType utilityType, Object data) {
        return ProcessingRequest.builder()
                .requestId(generateRequestId())
                .utilityType(utilityType)
                .data(data)
                .createdAt(Instant.now())
                .priority(1) // High priority
                .timeoutMs(60000L) // Longer timeout for high priority
                .build();
    }
    
    /**
     * Gets parameter value as specific type
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, Class<T> type) {
        if (parameters == null || !parameters.containsKey(key)) {
            return null;
        }
        Object value = parameters.get(key);
        if (type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Gets parameter value with default
     */
    public <T> T getParameterOrDefault(String key, Class<T> type, T defaultValue) {
        T value = getParameter(key, type);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Gets context value
     */
    public String getContextValue(String key) {
        return context != null ? context.get(key) : null;
    }
    
    /**
     * Checks if request is expired based on timeout
     */
    public boolean isExpired() {
        if (timeoutMs == null || createdAt == null) {
            return false;
        }
        return Instant.now().isAfter(createdAt.plusMillis(timeoutMs));
    }
    
    /**
     * Checks if request is high priority
     */
    public boolean isHighPriority() {
        return priority != null && priority <= 2;
    }
    
    /**
     * Generates a unique request ID
     */
    private static String generateRequestId() {
        return "REQ-" + System.currentTimeMillis() + "-" + 
               Integer.toHexString((int)(Math.random() * 0x10000));
    }
}