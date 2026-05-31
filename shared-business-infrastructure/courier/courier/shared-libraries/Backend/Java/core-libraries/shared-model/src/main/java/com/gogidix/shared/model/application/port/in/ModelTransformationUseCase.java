package com.gogidix.shared.model.application.port.in;

import com.gogidix.shared.model.domain.model.DomainEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Input port for domain model transformation operations.
 * Provides contracts for converting between domain models and external representations.
 */
public interface ModelTransformationUseCase {
    
    /**
     * Transforms a domain entity to a data transfer object.
     */
    <T extends DomainEntity, D> TransformationResult<D> transformToDto(T entity, Class<D> dtoClass);
    
    /**
     * Transforms a data transfer object to a domain entity.
     */
    <D, T extends DomainEntity> TransformationResult<T> transformToDomain(D dto, Class<T> entityClass);
    
    /**
     * Transforms a list of domain entities to DTOs.
     */
    <T extends DomainEntity, D> BatchTransformationResult<D> transformListToDto(List<T> entities, Class<D> dtoClass);
    
    /**
     * Transforms a list of DTOs to domain entities.
     */
    <D, T extends DomainEntity> BatchTransformationResult<T> transformListToDomain(List<D> dtos, Class<T> entityClass);
    
    /**
     * Validates domain entity state and business rules.
     */
    ValidationResult validateEntity(DomainEntity entity);
    
    /**
     * Validates a list of domain entities.
     */
    BatchValidationResult validateEntities(List<? extends DomainEntity> entities);
    
    /**
     * Merges changes from source entity into target entity.
     */
    <T extends DomainEntity> MergeResult<T> mergeEntities(T target, T source, String updatedBy);
    
    /**
     * Creates a deep copy of a domain entity.
     */
    <T extends DomainEntity> CopyResult<T> copyEntity(T entity);
    
    /**
     * Compares two domain entities and returns differences.
     */
    <T extends DomainEntity> ComparisonResult comparEntities(T entity1, T entity2);
    
    /**
     * Serializes domain entity to JSON.
     */
    SerializationResult serializeToJson(DomainEntity entity);
    
    /**
     * Deserializes JSON to domain entity.
     */
    <T extends DomainEntity> DeserializationResult<T> deserializeFromJson(String json, Class<T> entityClass);
    
    /**
     * Gets available transformation mappings.
     */
    List<TransformationMapping> getAvailableMappings();
    
    /**
     * Registers custom transformation mapping.
     */
    RegistrationResult registerMapping(TransformationMapping mapping);
    
    /**
     * Result of a single transformation operation.
     */
    class TransformationResult<T> {
        private final boolean success;
        private final T result;
        private final String errorMessage;
        private final Exception error;
        private final Map<String, Object> metadata;
        
        public TransformationResult(boolean success, T result, String errorMessage, Exception error, Map<String, Object> metadata) {
            this.success = success;
            this.result = result;
            this.errorMessage = errorMessage;
            this.error = error;
            this.metadata = metadata;
        }
        
        public static <T> TransformationResult<T> success(T result) {
            return new TransformationResult<>(true, result, null, null, Map.of());
        }
        
        public static <T> TransformationResult<T> success(T result, Map<String, Object> metadata) {
            return new TransformationResult<>(true, result, null, null, metadata);
        }
        
        public static <T> TransformationResult<T> failure(String errorMessage) {
            return new TransformationResult<>(false, null, errorMessage, null, Map.of());
        }
        
        public static <T> TransformationResult<T> failure(String errorMessage, Exception error) {
            return new TransformationResult<>(false, null, errorMessage, error, Map.of());
        }
        
        public boolean isSuccess() { return success; }
        public T getResult() { return result; }
        public String getErrorMessage() { return errorMessage; }
        public Optional<Exception> getError() { return Optional.ofNullable(error); }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    /**
     * Result of a batch transformation operation.
     */
    class BatchTransformationResult<T> {
        private final List<TransformationResult<T>> results;
        private final int totalCount;
        private final int successCount;
        private final int failureCount;
        private final long executionTimeMs;
        
        public BatchTransformationResult(List<TransformationResult<T>> results, long executionTimeMs) {
            this.results = results;
            this.totalCount = results.size();
            this.successCount = (int) results.stream().filter(TransformationResult::isSuccess).count();
            this.failureCount = totalCount - successCount;
            this.executionTimeMs = executionTimeMs;
        }
        
        public List<TransformationResult<T>> getResults() { return results; }
        public int getTotalCount() { return totalCount; }
        public int getSuccessCount() { return successCount; }
        public int getFailureCount() { return failureCount; }
        public long getExecutionTimeMs() { return executionTimeMs; }
        public double getSuccessRate() { return totalCount > 0 ? (double) successCount / totalCount : 0.0; }
        public boolean isAllSuccessful() { return failureCount == 0; }
        
        public List<T> getSuccessfulResults() {
            return results.stream()
                    .filter(TransformationResult::isSuccess)
                    .map(TransformationResult::getResult)
                    .toList();
        }
        
        public List<String> getErrorMessages() {
            return results.stream()
                    .filter(r -> !r.isSuccess())
                    .map(TransformationResult::getErrorMessage)
                    .toList();
        }
    }
    
    /**
     * Result of entity validation.
     */
    class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        private final List<String> warnings;
        private final Map<String, Object> validationDetails;
        
        public ValidationResult(boolean valid, List<String> errors, List<String> warnings, Map<String, Object> validationDetails) {
            this.valid = valid;
            this.errors = errors;
            this.warnings = warnings;
            this.validationDetails = validationDetails;
        }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, List.of(), List.of(), Map.of());
        }
        
        public static ValidationResult validWithWarnings(List<String> warnings) {
            return new ValidationResult(true, List.of(), warnings, Map.of());
        }
        
        public static ValidationResult invalid(List<String> errors) {
            return new ValidationResult(false, errors, List.of(), Map.of());
        }
        
        public static ValidationResult invalid(List<String> errors, List<String> warnings) {
            return new ValidationResult(false, errors, warnings, Map.of());
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public Map<String, Object> getValidationDetails() { return validationDetails; }
        public boolean hasWarnings() { return !warnings.isEmpty(); }
    }
    
    /**
     * Result of batch entity validation.
     */
    class BatchValidationResult {
        private final List<ValidationResult> results;
        private final int totalCount;
        private final int validCount;
        private final int invalidCount;
        
        public BatchValidationResult(List<ValidationResult> results) {
            this.results = results;
            this.totalCount = results.size();
            this.validCount = (int) results.stream().filter(ValidationResult::isValid).count();
            this.invalidCount = totalCount - validCount;
        }
        
        public List<ValidationResult> getResults() { return results; }
        public int getTotalCount() { return totalCount; }
        public int getValidCount() { return validCount; }
        public int getInvalidCount() { return invalidCount; }
        public boolean isAllValid() { return invalidCount == 0; }
        
        public List<String> getAllErrors() {
            return results.stream()
                    .flatMap(r -> r.getErrors().stream())
                    .toList();
        }
        
        public List<String> getAllWarnings() {
            return results.stream()
                    .flatMap(r -> r.getWarnings().stream())
                    .toList();
        }
    }
    
    /**
     * Result of entity merge operation.
     */
    class MergeResult<T extends DomainEntity> {
        private final boolean success;
        private final T mergedEntity;
        private final List<String> changedFields;
        private final String errorMessage;
        
        public MergeResult(boolean success, T mergedEntity, List<String> changedFields, String errorMessage) {
            this.success = success;
            this.mergedEntity = mergedEntity;
            this.changedFields = changedFields;
            this.errorMessage = errorMessage;
        }
        
        public static <T extends DomainEntity> MergeResult<T> success(T mergedEntity, List<String> changedFields) {
            return new MergeResult<>(true, mergedEntity, changedFields, null);
        }
        
        public static <T extends DomainEntity> MergeResult<T> failure(String errorMessage) {
            return new MergeResult<>(false, null, List.of(), errorMessage);
        }
        
        public boolean isSuccess() { return success; }
        public T getMergedEntity() { return mergedEntity; }
        public List<String> getChangedFields() { return changedFields; }
        public String getErrorMessage() { return errorMessage; }
        public boolean hasChanges() { return !changedFields.isEmpty(); }
    }
    
    /**
     * Result of entity copy operation.
     */
    class CopyResult<T extends DomainEntity> {
        private final boolean success;
        private final T copiedEntity;
        private final String errorMessage;
        
        public CopyResult(boolean success, T copiedEntity, String errorMessage) {
            this.success = success;
            this.copiedEntity = copiedEntity;
            this.errorMessage = errorMessage;
        }
        
        public static <T extends DomainEntity> CopyResult<T> success(T copiedEntity) {
            return new CopyResult<>(true, copiedEntity, null);
        }
        
        public static <T extends DomainEntity> CopyResult<T> failure(String errorMessage) {
            return new CopyResult<>(false, null, errorMessage);
        }
        
        public boolean isSuccess() { return success; }
        public T getCopiedEntity() { return copiedEntity; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Result of entity comparison.
     */
    class ComparisonResult {
        private final boolean identical;
        private final Map<String, FieldDifference> differences;
        private final double similarityScore;
        
        public ComparisonResult(boolean identical, Map<String, FieldDifference> differences, double similarityScore) {
            this.identical = identical;
            this.differences = differences;
            this.similarityScore = similarityScore;
        }
        
        public boolean isIdentical() { return identical; }
        public Map<String, FieldDifference> getDifferences() { return differences; }
        public double getSimilarityScore() { return similarityScore; }
        public boolean hasDifferences() { return !differences.isEmpty(); }
        
        public static class FieldDifference {
            private final String fieldName;
            private final Object oldValue;
            private final Object newValue;
            
            public FieldDifference(String fieldName, Object oldValue, Object newValue) {
                this.fieldName = fieldName;
                this.oldValue = oldValue;
                this.newValue = newValue;
            }
            
            public String getFieldName() { return fieldName; }
            public Object getOldValue() { return oldValue; }
            public Object getNewValue() { return newValue; }
        }
    }
    
    /**
     * Result of serialization operation.
     */
    class SerializationResult {
        private final boolean success;
        private final String serializedData;
        private final String format;
        private final String errorMessage;
        
        public SerializationResult(boolean success, String serializedData, String format, String errorMessage) {
            this.success = success;
            this.serializedData = serializedData;
            this.format = format;
            this.errorMessage = errorMessage;
        }
        
        public static SerializationResult success(String serializedData, String format) {
            return new SerializationResult(true, serializedData, format, null);
        }
        
        public static SerializationResult failure(String errorMessage) {
            return new SerializationResult(false, null, null, errorMessage);
        }
        
        public boolean isSuccess() { return success; }
        public String getSerializedData() { return serializedData; }
        public String getFormat() { return format; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Result of deserialization operation.
     */
    class DeserializationResult<T extends DomainEntity> {
        private final boolean success;
        private final T entity;
        private final String errorMessage;
        
        public DeserializationResult(boolean success, T entity, String errorMessage) {
            this.success = success;
            this.entity = entity;
            this.errorMessage = errorMessage;
        }
        
        public static <T extends DomainEntity> DeserializationResult<T> success(T entity) {
            return new DeserializationResult<>(true, entity, null);
        }
        
        public static <T extends DomainEntity> DeserializationResult<T> failure(String errorMessage) {
            return new DeserializationResult<>(false, null, errorMessage);
        }
        
        public boolean isSuccess() { return success; }
        public T getEntity() { return entity; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Transformation mapping configuration.
     */
    class TransformationMapping {
        private final Class<?> sourceClass;
        private final Class<?> targetClass;
        private final String mappingName;
        private final Map<String, String> fieldMappings;
        private final boolean bidirectional;
        
        public TransformationMapping(Class<?> sourceClass, Class<?> targetClass, String mappingName,
                                   Map<String, String> fieldMappings, boolean bidirectional) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
            this.mappingName = mappingName;
            this.fieldMappings = fieldMappings;
            this.bidirectional = bidirectional;
        }
        
        public Class<?> getSourceClass() { return sourceClass; }
        public Class<?> getTargetClass() { return targetClass; }
        public String getMappingName() { return mappingName; }
        public Map<String, String> getFieldMappings() { return fieldMappings; }
        public boolean isBidirectional() { return bidirectional; }
    }
    
    /**
     * Result of mapping registration.
     */
    class RegistrationResult {
        private final boolean success;
        private final String mappingId;
        private final String errorMessage;
        
        public RegistrationResult(boolean success, String mappingId, String errorMessage) {
            this.success = success;
            this.mappingId = mappingId;
            this.errorMessage = errorMessage;
        }
        
        public static RegistrationResult success(String mappingId) {
            return new RegistrationResult(true, mappingId, null);
        }
        
        public static RegistrationResult failure(String errorMessage) {
            return new RegistrationResult(false, null, errorMessage);
        }
        
        public boolean isSuccess() { return success; }
        public String getMappingId() { return mappingId; }
        public String getErrorMessage() { return errorMessage; }
    }
}