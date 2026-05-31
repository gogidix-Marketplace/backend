package com.gogidix.shared.utilities.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity for utility operations
 * Maps utility operation domain model to database table
 */
@Entity
@Table(name = "utility_operations", indexes = {
    @Index(name = "idx_operation_id", columnList = "operation_id"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_type", columnList = "type"),
    @Index(name = "idx_user_status", columnList = "user_id, status"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class UtilityOperationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "operation_id", unique = true, nullable = false, length = 100)
    private String operationId;
    
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "user_id", length = 100)
    private String userId;
    
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Column(name = "correlation_id", length = 100)
    private String correlationId;
    
    @Column(name = "request_id", length = 100)
    private String requestId;
    
    @Column(name = "client_id", length = 100)
    private String clientId;
    
    @Column(name = "priority", nullable = false, length = 20)
    private String priority;
    
    @Column(name = "retry_count")
    private Integer retryCount = 0;
    
    @Column(name = "max_retries")
    private Integer maxRetries = 3;
    
    @Column(name = "retry_backoff_multiplier")
    private Double retryBackoffMultiplier = 1.5;
    
    @Column(name = "allow_preemption")
    private Boolean allowPreemption = false;
    
    @Column(name = "operation_start_time")
    private LocalDateTime operationStartTime;
    
    @Column(name = "operation_end_time")
    private LocalDateTime operationEndTime;
    
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
    
    @Column(name = "queue_time_ms")
    private Long queueTimeMs;
    
    @Column(name = "total_processing_time_ms")
    private Long totalProcessingTimeMs;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "error_code", length = 50)
    private String errorCode;
    
    @Column(name = "error_stack_trace", columnDefinition = "TEXT")
    private String errorStackTrace;
    
    @Column(name = "recovery_suggestion", columnDefinition = "TEXT")
    private String recoverySuggestion;
    
    @Column(name = "cached")
    private Boolean cached = false;
    
    @Column(name = "cache_key", length = 255)
    private String cacheKey;
    
    @Column(name = "cache_ttl_seconds")
    private Long cacheTtlSeconds;
    
    @Column(name = "cache_region", length = 100)
    private String cacheRegion;
    
    @Column(name = "cache_write_through")
    private Boolean cacheWriteThrough = false;
    
    @Column(name = "max_memory_bytes")
    private Long maxMemoryBytes;
    
    @Column(name = "actual_memory_used_bytes")
    private Long actualMemoryUsedBytes;
    
    @Column(name = "max_cpu_cores")
    private Integer maxCpuCores;
    
    @Column(name = "cpu_usage_percent")
    private Double cpuUsagePercent;
    
    @Column(name = "max_execution_time_ms")
    private Long maxExecutionTimeMs;
    
    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;
    
    @Column(name = "current_phase", length = 100)
    private String currentPhase;
    
    @Column(name = "items_processed")
    private Long itemsProcessed = 0L;
    
    @Column(name = "total_items_to_process")
    private Long totalItemsToProcess = 100L;
    
    @Column(name = "performance_profile", length = 50)
    private String performanceProfile;
    
    @Column(name = "input_data", columnDefinition = "TEXT")
    private String inputData; // JSON string
    
    @Column(name = "output_data", columnDefinition = "TEXT")
    private String outputData; // JSON string
    
    @Column(name = "processing_context", columnDefinition = "TEXT")
    private String processingContext; // JSON string
    
    @Column(name = "operation_metadata", columnDefinition = "TEXT")
    private String operationMetadata; // JSON string
    
    @Column(name = "error_history", columnDefinition = "TEXT")
    private String errorHistory; // JSON string
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by", nullable = false, length = 100)
    private String updatedBy;
    
    @Column(name = "deleted")
    private Boolean deleted = false;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @Column(name = "deleted_by", length = 100)
    private String deletedBy;
    
    @Column(name = "deletion_reason", length = 255)
    private String deletionReason;
    
    @Version
    @Column(name = "version")
    private Long version = 1L;
    
    // Constructors
    public UtilityOperationEntity() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOperationId() { return operationId; }
    public void setOperationId(String operationId) { this.operationId = operationId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    
    public Double getRetryBackoffMultiplier() { return retryBackoffMultiplier; }
    public void setRetryBackoffMultiplier(Double retryBackoffMultiplier) { this.retryBackoffMultiplier = retryBackoffMultiplier; }
    
    public Boolean getAllowPreemption() { return allowPreemption; }
    public void setAllowPreemption(Boolean allowPreemption) { this.allowPreemption = allowPreemption; }
    
    public LocalDateTime getOperationStartTime() { return operationStartTime; }
    public void setOperationStartTime(LocalDateTime operationStartTime) { this.operationStartTime = operationStartTime; }
    
    public LocalDateTime getOperationEndTime() { return operationEndTime; }
    public void setOperationEndTime(LocalDateTime operationEndTime) { this.operationEndTime = operationEndTime; }
    
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    
    public Long getQueueTimeMs() { return queueTimeMs; }
    public void setQueueTimeMs(Long queueTimeMs) { this.queueTimeMs = queueTimeMs; }
    
    public Long getTotalProcessingTimeMs() { return totalProcessingTimeMs; }
    public void setTotalProcessingTimeMs(Long totalProcessingTimeMs) { this.totalProcessingTimeMs = totalProcessingTimeMs; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorStackTrace() { return errorStackTrace; }
    public void setErrorStackTrace(String errorStackTrace) { this.errorStackTrace = errorStackTrace; }
    
    public String getRecoverySuggestion() { return recoverySuggestion; }
    public void setRecoverySuggestion(String recoverySuggestion) { this.recoverySuggestion = recoverySuggestion; }
    
    public Boolean getCached() { return cached; }
    public void setCached(Boolean cached) { this.cached = cached; }
    
    public String getCacheKey() { return cacheKey; }
    public void setCacheKey(String cacheKey) { this.cacheKey = cacheKey; }
    
    public Long getCacheTtlSeconds() { return cacheTtlSeconds; }
    public void setCacheTtlSeconds(Long cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }
    
    public String getCacheRegion() { return cacheRegion; }
    public void setCacheRegion(String cacheRegion) { this.cacheRegion = cacheRegion; }
    
    public Boolean getCacheWriteThrough() { return cacheWriteThrough; }
    public void setCacheWriteThrough(Boolean cacheWriteThrough) { this.cacheWriteThrough = cacheWriteThrough; }
    
    public Long getMaxMemoryBytes() { return maxMemoryBytes; }
    public void setMaxMemoryBytes(Long maxMemoryBytes) { this.maxMemoryBytes = maxMemoryBytes; }
    
    public Long getActualMemoryUsedBytes() { return actualMemoryUsedBytes; }
    public void setActualMemoryUsedBytes(Long actualMemoryUsedBytes) { this.actualMemoryUsedBytes = actualMemoryUsedBytes; }
    
    public Integer getMaxCpuCores() { return maxCpuCores; }
    public void setMaxCpuCores(Integer maxCpuCores) { this.maxCpuCores = maxCpuCores; }
    
    public Double getCpuUsagePercent() { return cpuUsagePercent; }
    public void setCpuUsagePercent(Double cpuUsagePercent) { this.cpuUsagePercent = cpuUsagePercent; }
    
    public Long getMaxExecutionTimeMs() { return maxExecutionTimeMs; }
    public void setMaxExecutionTimeMs(Long maxExecutionTimeMs) { this.maxExecutionTimeMs = maxExecutionTimeMs; }
    
    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }
    
    public String getCurrentPhase() { return currentPhase; }
    public void setCurrentPhase(String currentPhase) { this.currentPhase = currentPhase; }
    
    public Long getItemsProcessed() { return itemsProcessed; }
    public void setItemsProcessed(Long itemsProcessed) { this.itemsProcessed = itemsProcessed; }
    
    public Long getTotalItemsToProcess() { return totalItemsToProcess; }
    public void setTotalItemsToProcess(Long totalItemsToProcess) { this.totalItemsToProcess = totalItemsToProcess; }
    
    public String getPerformanceProfile() { return performanceProfile; }
    public void setPerformanceProfile(String performanceProfile) { this.performanceProfile = performanceProfile; }
    
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    
    public String getOutputData() { return outputData; }
    public void setOutputData(String outputData) { this.outputData = outputData; }
    
    public String getProcessingContext() { return processingContext; }
    public void setProcessingContext(String processingContext) { this.processingContext = processingContext; }
    
    public String getOperationMetadata() { return operationMetadata; }
    public void setOperationMetadata(String operationMetadata) { this.operationMetadata = operationMetadata; }
    
    public String getErrorHistory() { return errorHistory; }
    public void setErrorHistory(String errorHistory) { this.errorHistory = errorHistory; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    
    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
    
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    
    public String getDeletedBy() { return deletedBy; }
    public void setDeletedBy(String deletedBy) { this.deletedBy = deletedBy; }
    
    public String getDeletionReason() { return deletionReason; }
    public void setDeletionReason(String deletionReason) { this.deletionReason = deletionReason; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}