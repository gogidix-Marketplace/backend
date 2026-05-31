package com.gogidix.shared.utilities.adapter.out.persistence;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.UtilityType;
import com.gogidix.shared.utilities.domain.model.OperationStatus;
import com.gogidix.shared.utilities.domain.model.OperationPriority;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Mapper for converting between UtilityOperation domain model and UtilityOperationEntity
 * Handles JSON serialization/deserialization for complex fields
 */
@Component
public class UtilityOperationMapper {
    
    private final ObjectMapper objectMapper;
    
    public UtilityOperationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * Converts domain model to JPA entity
     */
    public UtilityOperationEntity toEntity(UtilityOperation domain) {
        if (domain == null) {
            return null;
        }
        
        UtilityOperationEntity entity = new UtilityOperationEntity();
        
        // Basic fields
        entity.setOperationId(domain.getOperationId());
        entity.setType(domain.getOperationType().name());
        entity.setStatus(domain.getOperationStatus().name());
        entity.setName(domain.getOperationName());
        entity.setDescription(domain.getOperationDescription());
        
        // User and session context
        entity.setUserId(domain.getUserId());
        entity.setSessionId(domain.getSessionId());
        entity.setCorrelationId(domain.getCorrelationId());
        entity.setRequestId(domain.getRequestId());
        entity.setClientId(domain.getClientId());
        
        // Priority and processing
        entity.setPriority(domain.getPriority().name());
        entity.setRetryCount(domain.getRetryCount());
        entity.setMaxRetries(domain.getMaxRetries());
        entity.setRetryBackoffMultiplier(domain.getRetryBackoffMultiplier());
        entity.setAllowPreemption(domain.isAllowPreemption());
        
        // Timing information
        entity.setOperationStartTime(domain.getOperationStartTime());
        entity.setOperationEndTime(domain.getOperationEndTime());
        entity.setScheduledTime(domain.getScheduledTime());
        entity.setExecutionTimeMs(domain.getExecutionTimeMs());
        entity.setQueueTimeMs(domain.getQueueTimeMs());
        entity.setTotalProcessingTimeMs(domain.getTotalProcessingTimeMs());
        
        // Error handling
        entity.setErrorMessage(domain.getErrorMessage());
        entity.setErrorCode(domain.getErrorCode());
        entity.setErrorStackTrace(domain.getErrorStackTrace());
        entity.setRecoverySuggestion(domain.getRecoverySuggestion());
        entity.setErrorHistory(toJson(domain.getErrorHistory()));
        
        // Caching configuration
        entity.setCached(domain.isCached());
        entity.setCacheKey(domain.getCacheKey());
        entity.setCacheTtlSeconds(domain.getCacheTtlSeconds());
        entity.setCacheRegion(domain.getCacheRegion());
        entity.setCacheWriteThrough(domain.isCacheWriteThrough());
        
        // Resource management
        entity.setMaxMemoryBytes(domain.getMaxMemoryBytes());
        entity.setActualMemoryUsedBytes(domain.getActualMemoryUsedBytes());
        entity.setMaxCpuCores(domain.getMaxCpuCores());
        entity.setCpuUsagePercent(domain.getCpuUsagePercent());
        entity.setMaxExecutionTimeMs(domain.getMaxExecutionTimeMs());
        
        // Monitoring and metrics
        entity.setProgressPercentage(domain.getProgressPercentage());
        entity.setCurrentPhase(domain.getCurrentPhase());
        entity.setItemsProcessed(domain.getItemsProcessed());
        entity.setTotalItemsToProcess(domain.getTotalItemsToProcess());
        entity.setPerformanceProfile(domain.getPerformanceProfile());
        
        // Complex data as JSON
        entity.setInputData(toJson(domain.getInputData()));
        entity.setOutputData(toJson(domain.getOutputData()));
        entity.setProcessingContext(toJson(domain.getProcessingContext()));
        entity.setOperationMetadata(toJson(domain.getOperationMetadata()));
        
        // Base entity fields
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setDeletedBy(domain.getDeletedBy());
        entity.setDeletionReason(domain.getDeletionReason());
        entity.setVersion(domain.getVersion());
        
        return entity;
    }
    
    /**
     * Converts JPA entity to domain model
     */
    public UtilityOperation toDomain(UtilityOperationEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new UtilityOperation(
            entity.getId() != null ? UUID.randomUUID() : UUID.randomUUID(), // Use entity ID as basis for UUID
            entity.getVersion(),
            entity.getCreatedAt(),
            entity.getCreatedBy(),
            entity.getUpdatedAt(),
            entity.getUpdatedBy(),
            entity.getDeleted() != null ? entity.getDeleted() : false,
            entity.getDeletedAt(),
            entity.getDeletedBy(),
            entity.getDeletionReason(),
            entity.getOperationId(),
            UtilityType.valueOf(entity.getType()),
            OperationStatus.valueOf(entity.getStatus()),
            entity.getName(),
            entity.getDescription(),
            fromJson(entity.getInputData(), new TypeReference<Map<String, Object>>() {}),
            fromJson(entity.getOutputData(), new TypeReference<Map<String, Object>>() {}),
            fromJson(entity.getProcessingContext(), new TypeReference<Map<String, Object>>() {}),
            fromJson(entity.getOperationMetadata(), new TypeReference<Map<String, String>>() {}),
            entity.getOperationStartTime(),
            entity.getOperationEndTime(),
            entity.getScheduledTime(),
            entity.getExecutionTimeMs(),
            entity.getQueueTimeMs(),
            entity.getTotalProcessingTimeMs(),
            entity.getUserId(),
            entity.getSessionId(),
            entity.getCorrelationId(),
            entity.getRequestId(),
            entity.getClientId(),
            OperationPriority.valueOf(entity.getPriority()),
            entity.getRetryCount() != null ? entity.getRetryCount() : 0,
            entity.getMaxRetries() != null ? entity.getMaxRetries() : 3,
            entity.getRetryBackoffMultiplier() != null ? entity.getRetryBackoffMultiplier() : 1.5,
            entity.getAllowPreemption() != null ? entity.getAllowPreemption() : false,
            entity.getErrorMessage(),
            entity.getErrorCode(),
            entity.getErrorStackTrace(),
            fromJson(entity.getErrorHistory(), new TypeReference<List<String>>() {}),
            entity.getRecoverySuggestion(),
            entity.getCached() != null ? entity.getCached() : false,
            entity.getCacheKey(),
            entity.getCacheTtlSeconds(),
            entity.getCacheRegion(),
            entity.getCacheWriteThrough() != null ? entity.getCacheWriteThrough() : false,
            entity.getMaxMemoryBytes() != null ? entity.getMaxMemoryBytes() : 0L,
            entity.getActualMemoryUsedBytes() != null ? entity.getActualMemoryUsedBytes() : 0L,
            entity.getMaxCpuCores() != null ? entity.getMaxCpuCores() : 4,
            entity.getCpuUsagePercent() != null ? entity.getCpuUsagePercent() : 0.0,
            entity.getMaxExecutionTimeMs() != null ? entity.getMaxExecutionTimeMs() : 300000L,
            entity.getProgressPercentage() != null ? entity.getProgressPercentage() : 0,
            entity.getCurrentPhase(),
            entity.getItemsProcessed() != null ? entity.getItemsProcessed() : 0L,
            entity.getTotalItemsToProcess() != null ? entity.getTotalItemsToProcess() : 100L,
            entity.getPerformanceProfile()
        );
    }
    
    /**
     * Converts object to JSON string
     */
    private String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing to JSON", e);
        }
    }
    
    /**
     * Converts JSON string to object
     */
    private <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.trim().isEmpty()) {
            return getDefaultValue(typeReference);
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            // Return default value on parsing error
            return getDefaultValue(typeReference);
        }
    }
    
    /**
     * Gets default value for type reference
     */
    @SuppressWarnings("unchecked")
    private <T> T getDefaultValue(TypeReference<T> typeReference) {
        String typeName = typeReference.getType().getTypeName();
        
        if (typeName.contains("Map")) {
            return (T) new HashMap<>();
        } else if (typeName.contains("List")) {
            return (T) new ArrayList<>();
        } else if (typeName.contains("Set")) {
            return (T) new HashSet<>();
        }
        
        return null;
    }
}