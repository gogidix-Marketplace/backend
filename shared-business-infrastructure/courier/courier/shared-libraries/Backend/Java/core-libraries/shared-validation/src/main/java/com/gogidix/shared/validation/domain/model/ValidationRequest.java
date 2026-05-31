package com.gogidix.shared.validation.domain.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clean domain model representing a validation request.
 * 
 * This immutable entity encapsulates validation request data and business logic
 * following Domain Driven Design principles with zero external dependencies.
 * 
 * @author Gogidix Development Team
 * @version 2.0.0
 * @since 2024-01-01
 */
public final class ValidationRequest {
    
    private final String requestId;
    private final Object dataToValidate;
    private final String dataType;
    private final List<String> ruleIds;
    private final Map<String, Object> parameters;
    private final ValidationContext context;
    private final RequestPriority priority;
    private final LocalDateTime requestTimestamp;
    private final long timeoutMs;
    private final boolean failFast;
    private final String correlationId;
    private final Map<String, Object> metadata;
    private final ValidationScope scope;
    private final String requesterInfo;
    
    // Private constructor enforces immutability
    private ValidationRequest(Builder builder) {
        this.requestId = Objects.requireNonNull(builder.requestId, "Request ID cannot be null");
        this.dataToValidate = builder.dataToValidate;
        this.dataType = Objects.requireNonNull(builder.dataType, "Data type cannot be null");
        this.ruleIds = Collections.unmodifiableList(new ArrayList<>(builder.ruleIds));
        this.parameters = Collections.unmodifiableMap(new ConcurrentHashMap<>(builder.parameters));
        this.context = Objects.requireNonNull(builder.context, "Validation context cannot be null");
        this.priority = Objects.requireNonNull(builder.priority, "Priority cannot be null");
        this.requestTimestamp = Objects.requireNonNull(builder.requestTimestamp, "Request timestamp cannot be null");
        this.timeoutMs = builder.timeoutMs;
        this.failFast = builder.failFast;
        this.correlationId = Objects.requireNonNull(builder.correlationId, "Correlation ID cannot be null");
        this.metadata = Collections.unmodifiableMap(new ConcurrentHashMap<>(builder.metadata));
        this.scope = Objects.requireNonNull(builder.scope, "Scope cannot be null");
        this.requesterInfo = builder.requesterInfo;
        
        validateInvariant();
    }
    
    // Business invariant validation
    private void validateInvariant() {
        if (dataType.trim().isEmpty()) {
            throw new IllegalArgumentException("Data type cannot be empty");
        }
        
        if (timeoutMs <= 0 || timeoutMs > 300000) { // Max 5 minutes
            throw new IllegalArgumentException("Timeout must be between 1ms and 300000ms");
        }
        
        if (requestTimestamp.isAfter(LocalDateTime.now().plusMinutes(1))) {
            throw new IllegalArgumentException("Request timestamp cannot be significantly in the future");
        }
        
        // Validate rule IDs are not empty if specified
        if (ruleIds.stream().anyMatch(id -> id == null || id.trim().isEmpty())) {
            throw new IllegalArgumentException("Rule IDs cannot be null or empty");
        }
    }
    
    // Rich business logic methods
    
    /**
     * Checks if the request is expired based on current time and timeout.
     */
    public boolean isExpired() {
        LocalDateTime expiryTime = requestTimestamp.plusNanos(timeoutMs * 1_000_000);
        return LocalDateTime.now().isAfter(expiryTime);
    }
    
    /**
     * Gets remaining time before timeout in milliseconds.
     */
    public long getRemainingTimeoutMs() {
        if (isExpired()) return 0;
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = requestTimestamp.plusNanos(timeoutMs * 1_000_000);
        
        return java.time.Duration.between(now, expiryTime).toMillis();
    }
    
    /**
     * Checks if this request should use high priority processing.
     */
    public boolean requiresHighPriorityProcessing() {
        return priority == RequestPriority.HIGH || 
               priority == RequestPriority.CRITICAL ||
               context.getSecurityLevel() == ValidationContext.SecurityLevel.CRITICAL;
    }
    
    /**
     * Validates if the data type matches expected validation types.
     */
    public boolean isValidDataType(Set<String> supportedTypes) {
        return supportedTypes.contains(dataType.toLowerCase());
    }
    
    /**
     * Gets effective timeout based on priority and context.
     */
    public long getEffectiveTimeoutMs() {
        long baseTimeout = timeoutMs;
        
        // Adjust based on priority
        return switch (priority) {
            case LOW -> Math.min(baseTimeout, 10000L);
            case NORMAL -> Math.min(baseTimeout, 30000L);
            case HIGH -> Math.min(baseTimeout, 60000L);
            case CRITICAL -> Math.min(baseTimeout, 120000L);
        };
    }
    
    /**
     * Checks if request requires specific validation rules.
     */
    public boolean hasSpecificRules() {
        return !ruleIds.isEmpty();
    }
    
    /**
     * Gets parameter value with type safety.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getParameter(String key, Class<T> type) {
        Object value = parameters.get(key);
        if (value != null && type.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }
    
    /**
     * Creates a child request for nested validation.
     */
    public ValidationRequest createChildRequest(Object childData, String childDataType) {
        return ValidationRequest.builder()
            .dataToValidate(childData)
            .dataType(childDataType)
            .ruleIds(new ArrayList<>(ruleIds))
            .parameters(new HashMap<>(parameters))
            .context(context.createChildContext("nested-validation"))
            .priority(priority)
            .timeoutMs(Math.max(1000L, getRemainingTimeoutMs()))
            .failFast(failFast)
            .correlationId(correlationId)
            .metadata(new HashMap<>(metadata))
            .scope(scope)
            .requesterInfo(requesterInfo)
            .build();
    }
    
    /**
     * Checks if data to validate contains sensitive information.
     */
    public boolean containsSensitiveData() {
        if (dataToValidate == null) return false;
        
        String dataStr = dataToValidate.toString().toLowerCase();
        List<String> sensitivePatterns = Arrays.asList(
            "password", "ssn", "credit", "secret", "token", "key"
        );
        
        return sensitivePatterns.stream().anyMatch(dataStr::contains) ||
               parameters.containsKey("sensitive") ||
               context.getSecurityLevel() == ValidationContext.SecurityLevel.HIGH ||
               context.getSecurityLevel() == ValidationContext.SecurityLevel.CRITICAL;
    }
    
    /**
     * Gets estimated complexity score for this validation request.
     */
    public double getComplexityScore() {
        double baseScore = 1.0;
        
        // Add complexity based on rule count
        baseScore += ruleIds.size() * 0.2;
        
        // Add complexity based on data size (rough estimate)
        if (dataToValidate != null) {
            String dataStr = dataToValidate.toString();
            baseScore += Math.min(dataStr.length() / 1000.0, 2.0);
        }
        
        // Add complexity based on parameter count
        baseScore += parameters.size() * 0.1;
        
        // Multiply based on priority (higher priority = assumed more complex)
        baseScore *= switch (priority) {
            case LOW -> 0.8;
            case NORMAL -> 1.0;
            case HIGH -> 1.3;
            case CRITICAL -> 1.5;
        };
        
        return Math.min(baseScore, 10.0); // Cap at 10.0
    }
    
    /**
     * Sanitizes the request for logging (removes sensitive data).
     */
    public ValidationRequest sanitizeForLogging() {
        Object sanitizedData = containsSensitiveData() ? "[SENSITIVE_DATA_REDACTED]" : dataToValidate;
        
        Map<String, Object> sanitizedParams = parameters.entrySet().stream()
            .collect(HashMap::new, 
                    (map, entry) -> {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (key.toLowerCase().contains("password") || 
                            key.toLowerCase().contains("secret") ||
                            key.toLowerCase().contains("token")) {
                            map.put(key, "[REDACTED]");
                        } else {
                            map.put(key, value);
                        }
                    },
                    HashMap::putAll);
        
        return ValidationRequest.builder()
            .requestId(requestId)
            .dataToValidate(sanitizedData)
            .dataType(dataType)
            .ruleIds(ruleIds)
            .parameters(sanitizedParams)
            .context(context)
            .priority(priority)
            .requestTimestamp(requestTimestamp)
            .timeoutMs(timeoutMs)
            .failFast(failFast)
            .correlationId(correlationId)
            .metadata(metadata)
            .scope(scope)
            .requesterInfo(requesterInfo)
            .build();
    }
    
    // Getters (immutable access)
    public String getRequestId() { return requestId; }
    public Object getDataToValidate() { return dataToValidate; }
    public String getDataType() { return dataType; }
    public List<String> getRuleIds() { return ruleIds; }
    public Map<String, Object> getParameters() { return parameters; }
    public ValidationContext getContext() { return context; }
    public RequestPriority getPriority() { return priority; }
    public LocalDateTime getRequestTimestamp() { return requestTimestamp; }
    public long getTimeoutMs() { return timeoutMs; }
    public boolean isFailFast() { return failFast; }
    public String getCorrelationId() { return correlationId; }
    public Map<String, Object> getMetadata() { return metadata; }
    public ValidationScope getScope() { return scope; }
    public String getRequesterInfo() { return requesterInfo; }
    
    // Builder pattern for construction
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String requestId = UUID.randomUUID().toString();
        private Object dataToValidate;
        private String dataType = "unknown";
        private List<String> ruleIds = new ArrayList<>();
        private Map<String, Object> parameters = new HashMap<>();
        private ValidationContext context;
        private RequestPriority priority = RequestPriority.NORMAL;
        private LocalDateTime requestTimestamp = LocalDateTime.now();
        private long timeoutMs = 30000L; // 30 seconds default
        private boolean failFast = false;
        private String correlationId = UUID.randomUUID().toString();
        private Map<String, Object> metadata = new HashMap<>();
        private ValidationScope scope = ValidationScope.DEVELOPMENT;
        private String requesterInfo;
        
        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }
        
        public Builder dataToValidate(Object dataToValidate) {
            this.dataToValidate = dataToValidate;
            return this;
        }
        
        public Builder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }
        
        public Builder ruleIds(List<String> ruleIds) {
            this.ruleIds = new ArrayList<>(ruleIds);
            return this;
        }
        
        public Builder addRuleId(String ruleId) {
            this.ruleIds.add(ruleId);
            return this;
        }
        
        public Builder parameters(Map<String, Object> parameters) {
            this.parameters = new HashMap<>(parameters);
            return this;
        }
        
        public Builder addParameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }
        
        public Builder context(ValidationContext context) {
            this.context = context;
            return this;
        }
        
        public Builder priority(RequestPriority priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder requestTimestamp(LocalDateTime requestTimestamp) {
            this.requestTimestamp = requestTimestamp;
            return this;
        }
        
        public Builder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }
        
        public Builder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }
        
        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }
        
        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = new HashMap<>(metadata);
            return this;
        }
        
        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }
        
        public Builder scope(ValidationScope scope) {
            this.scope = scope;
            return this;
        }
        
        public Builder requesterInfo(String requesterInfo) {
            this.requesterInfo = requesterInfo;
            return this;
        }
        
        public ValidationRequest build() {
            // Set default context if not provided
            if (context == null) {
                context = ValidationContext.builder().build();
            }
            
            return new ValidationRequest(this);
        }
    }
    
    // Domain enums
    public enum RequestPriority {
        LOW(1),
        NORMAL(2),
        HIGH(3),
        CRITICAL(4);
        
        private final int level;
        
        RequestPriority(int level) {
            this.level = level;
        }
        
        public int getLevel() { return level; }
        
        public boolean isHigherThan(RequestPriority other) {
            return this.level > other.level;
        }
    }
    
    public enum ValidationScope {
        DEVELOPMENT, TESTING, STAGING, PRODUCTION
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationRequest that = (ValidationRequest) o;
        return Objects.equals(requestId, that.requestId) &&
               Objects.equals(correlationId, that.correlationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(requestId, correlationId);
    }
    
    @Override
    public String toString() {
        return String.format("ValidationRequest{requestId='%s', dataType='%s', rulesCount=%d, priority=%s, timeout=%dms}", 
                           requestId, dataType, ruleIds.size(), priority, timeoutMs);
    }
}