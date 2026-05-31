package com.gogidix.shared.utilities.domain.model;

import com.gogidix.shared.utilities.domain.valueobject.UtilityType;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pure domain model for utility processing requests with zero external dependencies.
 * Represents the input for any utility processing operation with comprehensive business logic.
 * NOTE: @Getter added to generate accessors for final fields.
 */
@Getter
public class ProcessingRequestClean {
    
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
    private final String traceId;
    private final String parentRequestId;
    private final String tenantId;
    private final boolean requiresAuthentication;
    private final boolean requiresAudit;
    
    public ProcessingRequestClean(String requestId, UtilityType utilityType, Object data,
                                 Map<String, Object> parameters, Map<String, String> context,
                                 String userId, String sessionId, Instant createdAt,
                                 Integer priority, Long timeoutMs, String traceId,
                                 String parentRequestId, String tenantId,
                                 boolean requiresAuthentication, boolean requiresAudit) {
        
        this.requestId = validateRequestId(requestId);
        this.utilityType = Objects.requireNonNull(utilityType, "Utility type cannot be null");
        this.data = data; // Can be null for some operations
        this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
        this.context = context != null ? new HashMap<>(context) : new HashMap<>();
        this.userId = userId; // Can be null for system operations
        this.sessionId = sessionId; // Can be null for background operations
        this.createdAt = Objects.requireNonNull(createdAt, "Created time cannot be null");
        this.priority = validatePriority(priority);
        this.timeoutMs = validateTimeout(timeoutMs);
        this.traceId = traceId;
        this.parentRequestId = parentRequestId;
        this.tenantId = validateTenantId(tenantId);
        this.requiresAuthentication = requiresAuthentication;
        this.requiresAudit = requiresAudit;
        
        validateRequestConsistency();
    }
    
    // ==============================================
    // STATIC FACTORY METHODS
    // ==============================================
    
    /**
     * Creates a basic processing request
     */
    public static ProcessingRequestClean create(UtilityType utilityType, Object data) {
        return new ProcessingRequestClean(
            generateRequestId(), utilityType, data, null, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false
        );
    }
    
    /**
     * Creates a processing request with parameters
     */
    public static ProcessingRequestClean withParameters(UtilityType utilityType, Object data, 
                                                       Map<String, Object> parameters) {
        return new ProcessingRequestClean(
            generateRequestId(), utilityType, data, parameters, null, null, null,
            Instant.now(), 5, 30000L, null, null, "default", false, false
        );
    }
    
    /**
     * Creates a high-priority processing request
     */
    public static ProcessingRequestClean highPriority(UtilityType utilityType, Object data) {
        return new ProcessingRequestClean(
            generateRequestId(), utilityType, data, null, null, null, null,
            Instant.now(), 1, 60000L, null, null, "default", false, true
        );
    }
    
    /**
     * Creates a user-initiated request
     */
    public static ProcessingRequestClean userRequest(UtilityType utilityType, Object data, 
                                                    String userId, String sessionId, String tenantId) {
        return new ProcessingRequestClean(
            generateRequestId(), utilityType, data, null, null, userId, sessionId,
            Instant.now(), 3, 45000L, null, null, tenantId, true, true
        );
    }
    
    /**
     * Creates a system background request
     */
    public static ProcessingRequestClean systemRequest(UtilityType utilityType, Object data) {
        return new ProcessingRequestClean(
            generateRequestId(), utilityType, data, null, null, "SYSTEM", null,
            Instant.now(), 7, 120000L, null, null, "system", false, false
        );
    }
    
    /**
     * Creates a child request from a parent request
     */
    public static ProcessingRequestClean childRequest(ProcessingRequestClean parent, 
                                                     UtilityType utilityType, Object data) {
        return new ProcessingRequestClean(
            generateRequestId(), utilityType, data, null, parent.context, 
            parent.userId, parent.sessionId, Instant.now(), parent.priority, 
            parent.timeoutMs, parent.traceId, parent.requestId, parent.tenantId,
            parent.requiresAuthentication, parent.requiresAudit
        );
    }
    
    // ==============================================
    // BUSINESS LOGIC METHODS
    // ==============================================
    
    /**
     * Business Rule: Checks if request is expired based on timeout
     */
    public boolean isExpired() {
        if (timeoutMs == null || createdAt == null) {
            return false;
        }
        return Instant.now().isAfter(createdAt.plusMillis(timeoutMs));
    }
    
    /**
     * Business Rule: Checks if request is high priority
     */
    public boolean isHighPriority() {
        return priority != null && priority <= 2;
    }
    
    /**
     * Business Rule: Checks if request is low priority
     */
    public boolean isLowPriority() {
        return priority != null && priority >= 8;
    }
    
    /**
     * Business Rule: Checks if request is user-initiated
     */
    public boolean isUserInitiated() {
        return userId != null && !isSystemRequest() && requiresAuthentication;
    }
    
    /**
     * Business Rule: Checks if request is system-generated
     */
    public boolean isSystemRequest() {
        return "SYSTEM".equals(userId) || userId == null;
    }
    
    /**
     * Business Rule: Checks if request requires audit trail
     */
    public boolean requiresAuditTrail() {
        return requiresAudit || isUserInitiated() || isHighPriority() || utilityType.isCritical();
    }
    
    /**
     * Business Rule: Checks if request requires authentication
     */
    public boolean requiresAuthenticationCheck() {
        return requiresAuthentication && !isSystemRequest();
    }
    
    /**
     * Business Rule: Checks if request is part of a trace
     */
    public boolean isPartOfTrace() {
        return traceId != null && !traceId.trim().isEmpty();
    }
    
    /**
     * Business Rule: Checks if request is a child request
     */
    public boolean isChildRequest() {
        return parentRequestId != null && !parentRequestId.trim().isEmpty();
    }
    
    /**
     * Business Rule: Checks if request has valid session
     */
    public boolean hasValidSession() {
        return sessionId != null && !sessionId.trim().isEmpty() && isUserInitiated();
    }
    
    /**
     * Business Rule: Checks if request is executable (all prerequisites met)
     */
    public boolean isExecutable() {
        if (isExpired()) return false;
        if (requiresAuthenticationCheck() && !hasValidSession()) return false;
        if (data == null && utilityType.requiresData()) return false;
        return isValidForTenant();
    }
    
    /**
     * Business Rule: Checks if request is valid for the current tenant
     */
    public boolean isValidForTenant() {
        return tenantId != null && !tenantId.trim().isEmpty();
    }
    
    /**
     * Business Rule: Checks if request should be cached
     */
    public boolean shouldCache() {
        return utilityType.isCacheable() && !isUserInitiated() && !requiresAuditTrail();
    }
    
    /**
     * Business Rule: Checks if request allows parallel processing
     */
    public boolean allowsParallelProcessing() {
        return !utilityType.requiresSequentialProcessing() && !isHighPriority();
    }
    
    // ==============================================
    // UTILITY CALCULATION METHODS
    // ==============================================
    
    /**
     * Business Calculation: Get time until expiration
     */
    public Duration getTimeUntilExpiration() {
        if (isExpired()) return Duration.ZERO;
        if (timeoutMs == null) return Duration.ofDays(1); // Default if no timeout
        
        Instant expirationTime = createdAt.plusMillis(timeoutMs);
        return Duration.between(Instant.now(), expirationTime);
    }
    
    /**
     * Business Calculation: Get processing age
     */
    public Duration getAge() {
        return Duration.between(createdAt, Instant.now());
    }
    
    /**
     * Business Calculation: Get queue priority score (lower = higher priority)
     */
    public int getQueuePriorityScore() {
        int score = priority != null ? priority : 5;
        
        // Adjust for aging - older requests get higher priority
        long ageSeconds = getAge().getSeconds();
        score -= Math.min(3, (int) (ageSeconds / 60)); // -1 per minute, max -3
        
        // Adjust for utility type criticality
        if (utilityType.isCritical()) {
            score -= 2;
        }
        
        // Adjust for user requests
        if (isUserInitiated()) {
            score -= 1;
        }
        
        return Math.max(1, score); // Minimum priority of 1
    }
    
    /**
     * Business Calculation: Get estimated processing time based on utility type and data size
     */
    public long getEstimatedProcessingTimeMs() {
        long baseTime = utilityType.getEstimatedProcessingTimeMs();
        
        // Adjust for data size if applicable
        if (data != null) {
            long dataSize = estimateDataSize(data);
            if (dataSize > 1024) { // More than 1KB
                baseTime += (dataSize / 1024) * 10; // +10ms per KB
            }
        }
        
        // Adjust for parameters complexity
        baseTime += parameters.size() * 5; // +5ms per parameter
        
        return Math.min(300000, baseTime); // Cap at 5 minutes
    }
    
    /**
     * Business Calculation: Get resource requirement score
     */
    public int getResourceRequirementScore() {
        int score = utilityType.getResourceIntensity();
        
        // Adjust for data size
        if (data != null) {
            long dataSize = estimateDataSize(data);
            score += Math.min(5, (int) (dataSize / (1024 * 1024))); // +1 per MB, max +5
        }
        
        // Adjust for parameter complexity
        score += Math.min(3, parameters.size() / 5); // +1 per 5 parameters, max +3
        
        return Math.min(10, score);
    }
    
    // ==============================================
    // PARAMETER ACCESS METHODS
    // ==============================================
    
    /**
     * Gets parameter value as specific type
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, Class<T> type) {
        if (!parameters.containsKey(key)) {
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
     * Gets string parameter
     */
    public String getStringParameter(String key) {
        return getParameter(key, String.class);
    }
    
    /**
     * Gets string parameter with default
     */
    public String getStringParameter(String key, String defaultValue) {
        return getParameterOrDefault(key, String.class, defaultValue);
    }
    
    /**
     * Gets integer parameter
     */
    public Integer getIntParameter(String key) {
        return getParameter(key, Integer.class);
    }
    
    /**
     * Gets integer parameter with default
     */
    public Integer getIntParameter(String key, Integer defaultValue) {
        return getParameterOrDefault(key, Integer.class, defaultValue);
    }
    
    /**
     * Gets boolean parameter
     */
    public Boolean getBooleanParameter(String key) {
        return getParameter(key, Boolean.class);
    }
    
    /**
     * Gets boolean parameter with default
     */
    public Boolean getBooleanParameter(String key, Boolean defaultValue) {
        return getParameterOrDefault(key, Boolean.class, defaultValue);
    }
    
    /**
     * Checks if parameter exists
     */
    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
    
    // ==============================================
    // CONTEXT ACCESS METHODS
    // ==============================================
    
    /**
     * Gets context value
     */
    public String getContextValue(String key) {
        return context.get(key);
    }
    
    /**
     * Gets context value with default
     */
    public String getContextValue(String key, String defaultValue) {
        String value = getContextValue(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Checks if context contains key
     */
    public boolean hasContextValue(String key) {
        return context.containsKey(key);
    }
    
    /**
     * Gets client IP from context
     */
    public String getClientIp() {
        return getContextValue("client_ip");
    }
    
    /**
     * Gets user agent from context
     */
    public String getUserAgent() {
        return getContextValue("user_agent");
    }
    
    /**
     * Gets request source from context
     */
    public String getRequestSource() {
        return getContextValue("source", "unknown");
    }
    
    // ==============================================
    // TRANSFORMATION METHODS
    // ==============================================
    
    /**
     * Create a copy with additional parameters
     */
    public ProcessingRequestClean withAdditionalParameters(Map<String, Object> additionalParams) {
        Map<String, Object> newParams = new HashMap<>(this.parameters);
        if (additionalParams != null) {
            newParams.putAll(additionalParams);
        }
        
        return new ProcessingRequestClean(
            this.requestId, this.utilityType, this.data, newParams, this.context,
            this.userId, this.sessionId, this.createdAt, this.priority, this.timeoutMs,
            this.traceId, this.parentRequestId, this.tenantId, 
            this.requiresAuthentication, this.requiresAudit
        );
    }
    
    /**
     * Create a copy with additional context
     */
    public ProcessingRequestClean withAdditionalContext(Map<String, String> additionalContext) {
        Map<String, String> newContext = new HashMap<>(this.context);
        if (additionalContext != null) {
            newContext.putAll(additionalContext);
        }
        
        return new ProcessingRequestClean(
            this.requestId, this.utilityType, this.data, this.parameters, newContext,
            this.userId, this.sessionId, this.createdAt, this.priority, this.timeoutMs,
            this.traceId, this.parentRequestId, this.tenantId, 
            this.requiresAuthentication, this.requiresAudit
        );
    }
    
    /**
     * Create a copy with different priority
     */
    public ProcessingRequestClean withPriority(int newPriority) {
        return new ProcessingRequestClean(
            this.requestId, this.utilityType, this.data, this.parameters, this.context,
            this.userId, this.sessionId, this.createdAt, newPriority, this.timeoutMs,
            this.traceId, this.parentRequestId, this.tenantId, 
            this.requiresAuthentication, this.requiresAudit
        );
    }
    
    /**
     * Create a copy with different timeout
     */
    public ProcessingRequestClean withTimeout(long newTimeoutMs) {
        return new ProcessingRequestClean(
            this.requestId, this.utilityType, this.data, this.parameters, this.context,
            this.userId, this.sessionId, this.createdAt, this.priority, newTimeoutMs,
            this.traceId, this.parentRequestId, this.tenantId, 
            this.requiresAuthentication, this.requiresAudit
        );
    }
    
    /**
     * Create a copy with trace information
     */
    public ProcessingRequestClean withTrace(String traceId) {
        return new ProcessingRequestClean(
            this.requestId, this.utilityType, this.data, this.parameters, this.context,
            this.userId, this.sessionId, this.createdAt, this.priority, this.timeoutMs,
            traceId, this.parentRequestId, this.tenantId, 
            this.requiresAuthentication, this.requiresAudit
        );
    }
    
    // ==============================================
    // HELPER METHODS
    // ==============================================
    
    private static String generateRequestId() {
        return "REQ-" + System.currentTimeMillis() + "-" + 
               Integer.toHexString(ThreadLocalRandom.current().nextInt(0x10000));
    }
    
    private long estimateDataSize(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof String) return ((String) obj).length() * 2L;
        if (obj instanceof byte[]) return ((byte[]) obj).length;
        if (obj instanceof Map) return ((Map<?, ?>) obj).size() * 100L;
        if (obj instanceof java.util.Collection) return ((java.util.Collection<?>) obj).size() * 50L;
        return 100; // Default estimate
    }
    
    // ==============================================
    // DOMAIN VALIDATION METHODS
    // ==============================================
    
    private String validateRequestId(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new IllegalArgumentException("Request ID cannot be null or empty");
        }
        if (requestId.length() > 100) {
            throw new IllegalArgumentException("Request ID cannot exceed 100 characters");
        }
        return requestId.trim();
    }
    
    private Integer validatePriority(Integer priority) {
        if (priority == null) return 5; // Default priority
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Priority must be between 1 and 10");
        }
        return priority;
    }
    
    private Long validateTimeout(Long timeoutMs) {
        if (timeoutMs == null) return 30000L; // Default 30 seconds
        if (timeoutMs < 1000 || timeoutMs > 3600000) { // 1s to 1 hour
            throw new IllegalArgumentException("Timeout must be between 1 second and 1 hour");
        }
        return timeoutMs;
    }
    
    private String validateTenantId(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            return "default";
        }
        if (tenantId.length() > 50) {
            throw new IllegalArgumentException("Tenant ID cannot exceed 50 characters");
        }
        return tenantId.trim();
    }
    
    private void validateRequestConsistency() {
        if (requiresAuthentication && userId == null) {
            throw new IllegalArgumentException("Authentication required but no user ID provided");
        }
        
        if (isChildRequest() && !isPartOfTrace()) {
            throw new IllegalArgumentException("Child requests must be part of a trace");
        }
    }
    
    // ==============================================
    // GETTERS (NO LOMBOK DEPENDENCY)
    // ==============================================
    
    public String getRequestId() { return requestId; }
    public UtilityType getUtilityType() { return utilityType; }
    public Object getData() { return data; }
    public Map<String, Object> getParameters() { return new HashMap<>(parameters); }
    public Map<String, String> getContext() { return new HashMap<>(context); }
    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public Instant getCreatedAt() { return createdAt; }
    public Integer getPriority() { return priority; }
    public Long getTimeoutMs() { return timeoutMs; }
    public String getTraceId() { return traceId; }
    public String getParentRequestId() { return parentRequestId; }
    public String getTenantId() { return tenantId; }
    public boolean isRequiresAuthentication() { return requiresAuthentication; }
    public boolean isRequiresAudit() { return requiresAudit; }
    
    // ==============================================
    // OBJECT METHODS
    // ==============================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessingRequestClean that = (ProcessingRequestClean) o;
        return Objects.equals(requestId, that.requestId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
    
    @Override
    public String toString() {
        return String.format("ProcessingRequest{id='%s', type=%s, priority=%d, user='%s', tenant='%s'}", 
                           requestId, utilityType, priority, userId, tenantId);
    }
}