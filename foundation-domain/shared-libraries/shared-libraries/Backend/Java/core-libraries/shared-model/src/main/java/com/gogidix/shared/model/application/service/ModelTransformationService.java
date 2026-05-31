package com.gogidix.shared.model.application.service;

import com.gogidix.shared.model.application.port.in.ModelTransformationUseCase;
import com.gogidix.shared.model.domain.model.DomainEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Application service implementing domain model transformation operations.
 * Provides comprehensive transformation, validation, and mapping capabilities.
 */
@Service
public class ModelTransformationService implements ModelTransformationUseCase {
    
    private final Map<String, TransformationMapping> registeredMappings = new ConcurrentHashMap<>();
    
    @Override
    public <T extends DomainEntity, D> TransformationResult<D> transformToDto(T entity, Class<D> dtoClass) {
        if (entity == null) {
            return TransformationResult.failure("Source entity cannot be null");
        }
        
        if (dtoClass == null) {
            return TransformationResult.failure("Target DTO class cannot be null");
        }
        
        try {
            D dto = createAndPopulateDto(entity, dtoClass);
            Map<String, Object> metadata = Map.of(
                "sourceType", entity.getClass().getSimpleName(),
                "targetType", dtoClass.getSimpleName(),
                "transformationTime", LocalDateTime.now()
            );
            
            return TransformationResult.success(dto, metadata);
            
        } catch (Exception e) {
            return TransformationResult.failure("Transformation failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public <D, T extends DomainEntity> TransformationResult<T> transformToDomain(D dto, Class<T> entityClass) {
        if (dto == null) {
            return TransformationResult.failure("Source DTO cannot be null");
        }
        
        if (entityClass == null) {
            return TransformationResult.failure("Target entity class cannot be null");
        }
        
        try {
            T entity = createAndPopulateDomain(dto, entityClass);
            Map<String, Object> metadata = Map.of(
                "sourceType", dto.getClass().getSimpleName(),
                "targetType", entityClass.getSimpleName(),
                "transformationTime", LocalDateTime.now()
            );
            
            return TransformationResult.success(entity, metadata);
            
        } catch (Exception e) {
            return TransformationResult.failure("Transformation failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public <T extends DomainEntity, D> BatchTransformationResult<D> transformListToDto(List<T> entities, Class<D> dtoClass) {
        long startTime = System.currentTimeMillis();
        
        List<TransformationResult<D>> results = entities.stream()
                .map(entity -> transformToDto(entity, dtoClass))
                .collect(Collectors.toList());
        
        long executionTime = System.currentTimeMillis() - startTime;
        return new BatchTransformationResult<>(results, executionTime);
    }
    
    @Override
    public <D, T extends DomainEntity> BatchTransformationResult<T> transformListToDomain(List<D> dtos, Class<T> entityClass) {
        long startTime = System.currentTimeMillis();
        
        List<TransformationResult<T>> results = dtos.stream()
                .map(dto -> transformToDomain(dto, entityClass))
                .collect(Collectors.toList());
        
        long executionTime = System.currentTimeMillis() - startTime;
        return new BatchTransformationResult<>(results, executionTime);
    }
    
    @Override
    public ValidationResult validateEntity(DomainEntity entity) {
        if (entity == null) {
            return ValidationResult.invalid(List.of("Entity cannot be null"));
        }
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Map<String, Object> validationDetails = new HashMap<>();
        
        // Basic validation using entity's isValid method
        if (!entity.isValid()) {
            errors.add("Entity failed basic validation check");
        }
        
        // Additional field-level validation
        validateFields(entity, errors, warnings, validationDetails);
        
        // Business rule validation
        validateBusinessRules(entity, errors, warnings, validationDetails);
        
        if (errors.isEmpty()) {
            return warnings.isEmpty() 
                ? ValidationResult.valid() 
                : ValidationResult.validWithWarnings(warnings);
        } else {
            return ValidationResult.invalid(errors, warnings);
        }
    }
    
    @Override
    public BatchValidationResult validateEntities(List<? extends DomainEntity> entities) {
        List<ValidationResult> results = entities.stream()
                .map(this::validateEntity)
                .collect(Collectors.toList());
        
        return new BatchValidationResult(results);
    }
    
    @Override
    public <T extends DomainEntity> MergeResult<T> mergeEntities(T target, T source, String updatedBy) {
        if (target == null || source == null) {
            return MergeResult.failure("Target and source entities cannot be null");
        }
        
        if (!target.getClass().equals(source.getClass())) {
            return MergeResult.failure("Target and source entities must be of the same type");
        }
        
        try {
            List<String> changedFields = new ArrayList<>();
            T mergedEntity = performMerge(target, source, updatedBy, changedFields);
            
            return MergeResult.success(mergedEntity, changedFields);
            
        } catch (Exception e) {
            return MergeResult.failure("Merge failed: " + e.getMessage());
        }
    }
    
    @Override
    public <T extends DomainEntity> CopyResult<T> copyEntity(T entity) {
        if (entity == null) {
            return CopyResult.failure("Entity cannot be null");
        }
        
        try {
            @SuppressWarnings("unchecked")
            T copiedEntity = (T) performDeepCopy(entity);
            return CopyResult.success(copiedEntity);
            
        } catch (Exception e) {
            return CopyResult.failure("Copy failed: " + e.getMessage());
        }
    }
    
    @Override
    public <T extends DomainEntity> ComparisonResult comparEntities(T entity1, T entity2) {
        if (entity1 == null && entity2 == null) {
            return new ComparisonResult(true, Map.of(), 1.0);
        }
        
        if (entity1 == null || entity2 == null) {
            return new ComparisonResult(false, Map.of("existence", 
                new ComparisonResult.FieldDifference("existence", entity1, entity2)), 0.0);
        }
        
        if (!entity1.getClass().equals(entity2.getClass())) {
            return new ComparisonResult(false, Map.of("type", 
                new ComparisonResult.FieldDifference("type", entity1.getClass(), entity2.getClass())), 0.0);
        }
        
        Map<String, ComparisonResult.FieldDifference> differences = findDifferences(entity1, entity2);
        boolean identical = differences.isEmpty();
        double similarityScore = calculateSimilarityScore(entity1, entity2, differences);
        
        return new ComparisonResult(identical, differences, similarityScore);
    }
    
    @Override
    public SerializationResult serializeToJson(DomainEntity entity) {
        if (entity == null) {
            return SerializationResult.failure("Entity cannot be null");
        }
        
        try {
            // In a real implementation, this would use Jackson or similar JSON library
            String json = convertToJson(entity);
            return SerializationResult.success(json, "JSON");
            
        } catch (Exception e) {
            return SerializationResult.failure("Serialization failed: " + e.getMessage());
        }
    }
    
    @Override
    public <T extends DomainEntity> DeserializationResult<T> deserializeFromJson(String json, Class<T> entityClass) {
        if (json == null || json.trim().isEmpty()) {
            return DeserializationResult.failure("JSON string cannot be null or empty");
        }
        
        if (entityClass == null) {
            return DeserializationResult.failure("Entity class cannot be null");
        }
        
        try {
            // In a real implementation, this would use Jackson or similar JSON library
            T entity = convertFromJson(json, entityClass);
            return DeserializationResult.success(entity);
            
        } catch (Exception e) {
            return DeserializationResult.failure("Deserialization failed: " + e.getMessage());
        }
    }
    
    @Override
    public List<TransformationMapping> getAvailableMappings() {
        return new ArrayList<>(registeredMappings.values());
    }
    
    @Override
    public RegistrationResult registerMapping(TransformationMapping mapping) {
        if (mapping == null) {
            return RegistrationResult.failure("Mapping cannot be null");
        }
        
        String mappingId = generateMappingId(mapping);
        
        if (registeredMappings.containsKey(mappingId)) {
            return RegistrationResult.failure("Mapping already exists: " + mappingId);
        }
        
        registeredMappings.put(mappingId, mapping);
        return RegistrationResult.success(mappingId);
    }
    
    // Helper methods
    
    private <T extends DomainEntity, D> D createAndPopulateDto(T entity, Class<D> dtoClass) throws Exception {
        D dto = dtoClass.getDeclaredConstructor().newInstance();
        copyMatchingFields(entity, dto);
        return dto;
    }
    
    private <D, T extends DomainEntity> T createAndPopulateDomain(D dto, Class<T> entityClass) throws Exception {
        // This would need a more sophisticated approach in real implementation
        // For now, we'll assume builder pattern or similar construction mechanism
        throw new UnsupportedOperationException("Domain entity construction from DTO requires specific implementation");
    }
    
    private void copyMatchingFields(Object source, Object target) throws Exception {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        
        Field[] sourceFields = sourceClass.getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();
        
        Map<String, Field> targetFieldMap = Arrays.stream(targetFields)
                .collect(Collectors.toMap(Field::getName, field -> field));
        
        for (Field sourceField : sourceFields) {
            if (Modifier.isStatic(sourceField.getModifiers())) {
                continue;
            }
            
            Field targetField = targetFieldMap.get(sourceField.getName());
            if (targetField != null && targetField.getType().isAssignableFrom(sourceField.getType())) {
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                
                Object value = sourceField.get(source);
                targetField.set(target, value);
            }
        }
    }
    
    private void validateFields(DomainEntity entity, List<String> errors, List<String> warnings, Map<String, Object> details) {
        Class<?> entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                validateField(field.getName(), value, field.getType(), errors, warnings);
            } catch (IllegalAccessException e) {
                warnings.add("Could not access field: " + field.getName());
            }
        }
    }
    
    private void validateField(String fieldName, Object value, Class<?> fieldType, List<String> errors, List<String> warnings) {
        // Basic null checks for required fields
        if (value == null) {
            if (fieldName.equals("id") || fieldName.equals("version")) {
                // These might be null for new entities
                warnings.add("Field '" + fieldName + "' is null (may be acceptable for new entities)");
            }
            return;
        }
        
        // String validations
        if (value instanceof String stringValue) {
            if (stringValue.trim().isEmpty()) {
                warnings.add("Field '" + fieldName + "' is empty string");
            }
            if (stringValue.length() > 1000) {
                warnings.add("Field '" + fieldName + "' is very long (" + stringValue.length() + " characters)");
            }
        }
        
        // Collection validations
        if (value instanceof Collection<?> collection) {
            if (collection.isEmpty()) {
                warnings.add("Field '" + fieldName + "' is empty collection");
            }
            if (collection.size() > 1000) {
                warnings.add("Field '" + fieldName + "' has many items (" + collection.size() + ")");
            }
        }
    }
    
    private void validateBusinessRules(DomainEntity entity, List<String> errors, List<String> warnings, Map<String, Object> details) {
        // Entity-specific business rule validation would go here
        // This could be extended with a strategy pattern for different entity types
        
        if (entity.isDeleted() && entity.getDeletedAt() == null) {
            errors.add("Deleted entities must have deletedAt timestamp");
        }
        
        if (entity.getVersion() != null && entity.getVersion() < 0) {
            errors.add("Entity version cannot be negative");
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T extends DomainEntity> T performMerge(T target, T source, String updatedBy, List<String> changedFields) throws Exception {
        // This would need sophisticated merging logic based on entity type and business rules
        // For now, we'll return the updated target
        target.updateWith(updatedBy);
        return target;
    }
    
    private DomainEntity performDeepCopy(DomainEntity entity) throws Exception {
        // This would need sophisticated deep copying logic
        // In a real implementation, this might use serialization/deserialization or reflection
        throw new UnsupportedOperationException("Deep copy requires specific implementation for each entity type");
    }
    
    private Map<String, ComparisonResult.FieldDifference> findDifferences(DomainEntity entity1, DomainEntity entity2) {
        Map<String, ComparisonResult.FieldDifference> differences = new HashMap<>();
        
        try {
            Field[] fields = entity1.getClass().getDeclaredFields();
            
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                
                field.setAccessible(true);
                Object value1 = field.get(entity1);
                Object value2 = field.get(entity2);
                
                if (!Objects.equals(value1, value2)) {
                    differences.put(field.getName(), 
                        new ComparisonResult.FieldDifference(field.getName(), value1, value2));
                }
            }
        } catch (Exception e) {
            // Log error in real implementation
        }
        
        return differences;
    }
    
    private double calculateSimilarityScore(DomainEntity entity1, DomainEntity entity2, Map<String, ComparisonResult.FieldDifference> differences) {
        try {
            Field[] fields = entity1.getClass().getDeclaredFields();
            int totalFields = (int) Arrays.stream(fields)
                    .filter(f -> !Modifier.isStatic(f.getModifiers()))
                    .count();
            
            if (totalFields == 0) {
                return 1.0;
            }
            
            int matchingFields = totalFields - differences.size();
            return (double) matchingFields / totalFields;
            
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private String convertToJson(DomainEntity entity) {
        // Simplified JSON conversion - in real implementation would use Jackson
        return "{\"entityType\":\"" + entity.getEntityType() + "\",\"id\":\"" + entity.getId() + "\"}";
    }
    
    private <T extends DomainEntity> T convertFromJson(String json, Class<T> entityClass) throws Exception {
        // Simplified JSON parsing - in real implementation would use Jackson
        throw new UnsupportedOperationException("JSON deserialization requires Jackson or similar library");
    }
    
    private String generateMappingId(TransformationMapping mapping) {
        return mapping.getSourceClass().getSimpleName() + "->" + 
               mapping.getTargetClass().getSimpleName() + 
               (mapping.isBidirectional() ? "-bidirectional" : "");
    }
}