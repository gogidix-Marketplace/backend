package com.gogidix.shared.model.adapter.in.web;

import com.gogidix.shared.model.application.port.in.ModelTransformationUseCase;
import com.gogidix.shared.model.domain.model.DomainEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

/**
 * REST controller for domain model transformation operations.
 * Provides HTTP endpoints for model transformation and validation.
 */
@RestController
@RequestMapping("/api/v1/model")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Model Transformation", description = "Domain model transformation and validation operations")
public class ModelTransformationController {
    
    private final ModelTransformationUseCase modelTransformationUseCase;
    
    public ModelTransformationController(ModelTransformationUseCase modelTransformationUseCase) {
        this.modelTransformationUseCase = modelTransformationUseCase;
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate domain entity", description = "Validates a domain entity against business rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation completed"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<ValidationResponse> validateEntity(@Valid @RequestBody ValidateEntityRequest request) {
        
        ModelTransformationUseCase.ValidationResult result = modelTransformationUseCase.validateEntity(request.getEntity());
        
        return ResponseEntity.ok(ValidationResponse.from(result));
    }
    
    @PostMapping("/validate/batch")
    @Operation(summary = "Validate multiple entities", description = "Validates multiple domain entities in batch")
    @PreAuthorize("hasAuthority('MODEL_BATCH_VALIDATE')")
    public ResponseEntity<BatchValidationResponse> validateEntities(@Valid @RequestBody ValidateEntitiesRequest request) {
        
        ModelTransformationUseCase.BatchValidationResult result = modelTransformationUseCase.validateEntities(request.getEntities());
        
        return ResponseEntity.ok(BatchValidationResponse.from(result));
    }
    
    @PostMapping("/serialize")
    @Operation(summary = "Serialize entity to JSON", description = "Serializes a domain entity to JSON format")
    public ResponseEntity<SerializationResponse> serializeEntity(@Valid @RequestBody SerializeEntityRequest request) {
        
        ModelTransformationUseCase.SerializationResult result = modelTransformationUseCase.serializeToJson(request.getEntity());
        
        return ResponseEntity.ok(SerializationResponse.from(result));
    }
    
    @PostMapping("/deserialize")
    @Operation(summary = "Deserialize JSON to entity", description = "Deserializes JSON to a domain entity")
    public ResponseEntity<DeserializationResponse> deserializeEntity(@Valid @RequestBody DeserializeEntityRequest request) {
        
        ModelTransformationUseCase.DeserializationResult<?> result = modelTransformationUseCase
            .deserializeFromJson(request.getJson(), request.getEntityClass());
        
        return ResponseEntity.ok(DeserializationResponse.from(result));
    }
    
    @PostMapping("/compare")
    @Operation(summary = "Compare two entities", description = "Compares two domain entities and returns differences")
    @PreAuthorize("hasAuthority('MODEL_COMPARE')")
    public ResponseEntity<ComparisonResponse> compareEntities(@Valid @RequestBody CompareEntitiesRequest request) {
        
        ModelTransformationUseCase.ComparisonResult result = modelTransformationUseCase
            .comparEntities(request.getEntity1(), request.getEntity2());
        
        return ResponseEntity.ok(ComparisonResponse.from(result));
    }
    
    @PostMapping("/copy")
    @Operation(summary = "Copy entity", description = "Creates a deep copy of a domain entity")
    public ResponseEntity<CopyResponse> copyEntity(@Valid @RequestBody CopyEntityRequest request) {
        
        ModelTransformationUseCase.CopyResult<?> result = modelTransformationUseCase.copyEntity(request.getEntity());
        
        return ResponseEntity.ok(CopyResponse.from(result));
    }
    
    @PostMapping("/merge")
    @Operation(summary = "Merge entities", description = "Merges changes from source entity into target entity")
    @PreAuthorize("hasAuthority('MODEL_MERGE')")
    public ResponseEntity<MergeResponse> mergeEntities(@Valid @RequestBody MergeEntitiesRequest request) {
        
        ModelTransformationUseCase.MergeResult<?> result = modelTransformationUseCase
            .mergeEntities(request.getTarget(), request.getSource(), request.getUpdatedBy());
        
        return ResponseEntity.ok(MergeResponse.from(result));
    }
    
    @GetMapping("/mappings")
    @Operation(summary = "Get transformation mappings", description = "Gets available transformation mappings")
    @PreAuthorize("hasAuthority('MODEL_READ_MAPPINGS')")
    public ResponseEntity<List<TransformationMappingResponse>> getAvailableMappings() {
        
        List<ModelTransformationUseCase.TransformationMapping> mappings = 
            modelTransformationUseCase.getAvailableMappings();
        
        List<TransformationMappingResponse> response = mappings.stream()
            .map(TransformationMappingResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/mappings")
    @Operation(summary = "Register transformation mapping", description = "Registers a custom transformation mapping")
    @PreAuthorize("hasAuthority('MODEL_REGISTER_MAPPING')")
    public ResponseEntity<RegistrationResponse> registerMapping(@Valid @RequestBody RegisterMappingRequest request) {
        
        ModelTransformationUseCase.TransformationMapping mapping = new ModelTransformationUseCase.TransformationMapping(
            request.getSourceClass(),
            request.getTargetClass(),
            request.getMappingName(),
            request.getFieldMappings(),
            request.isBidirectional()
        );
        
        ModelTransformationUseCase.RegistrationResult result = modelTransformationUseCase.registerMapping(mapping);
        
        return ResponseEntity.ok(RegistrationResponse.from(result));
    }
    
    // Request DTOs
    
    public static class ValidateEntityRequest {
        @NotNull
        private DomainEntity entity;
        
        public DomainEntity getEntity() { return entity; }
        public void setEntity(DomainEntity entity) { this.entity = entity; }
    }
    
    public static class ValidateEntitiesRequest {
        @NotNull
        private List<DomainEntity> entities;
        
        public List<DomainEntity> getEntities() { return entities; }
        public void setEntities(List<DomainEntity> entities) { this.entities = entities; }
    }
    
    public static class SerializeEntityRequest {
        @NotNull
        private DomainEntity entity;
        
        public DomainEntity getEntity() { return entity; }
        public void setEntity(DomainEntity entity) { this.entity = entity; }
    }
    
    public static class DeserializeEntityRequest {
        @NotNull
        private String json;
        @NotNull
        private Class<? extends DomainEntity> entityClass;
        
        public String getJson() { return json; }
        public void setJson(String json) { this.json = json; }
        public Class<? extends DomainEntity> getEntityClass() { return entityClass; }
        public void setEntityClass(Class<? extends DomainEntity> entityClass) { this.entityClass = entityClass; }
    }
    
    public static class CompareEntitiesRequest {
        @NotNull
        private DomainEntity entity1;
        @NotNull
        private DomainEntity entity2;
        
        public DomainEntity getEntity1() { return entity1; }
        public void setEntity1(DomainEntity entity1) { this.entity1 = entity1; }
        public DomainEntity getEntity2() { return entity2; }
        public void setEntity2(DomainEntity entity2) { this.entity2 = entity2; }
    }
    
    public static class CopyEntityRequest {
        @NotNull
        private DomainEntity entity;
        
        public DomainEntity getEntity() { return entity; }
        public void setEntity(DomainEntity entity) { this.entity = entity; }
    }
    
    public static class MergeEntitiesRequest {
        @NotNull
        private DomainEntity target;
        @NotNull
        private DomainEntity source;
        @NotNull
        private String updatedBy;
        
        public DomainEntity getTarget() { return target; }
        public void setTarget(DomainEntity target) { this.target = target; }
        public DomainEntity getSource() { return source; }
        public void setSource(DomainEntity source) { this.source = source; }
        public String getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    }
    
    public static class RegisterMappingRequest {
        @NotNull
        private Class<?> sourceClass;
        @NotNull
        private Class<?> targetClass;
        @NotNull
        private String mappingName;
        private Map<String, String> fieldMappings;
        private boolean bidirectional;
        
        public Class<?> getSourceClass() { return sourceClass; }
        public void setSourceClass(Class<?> sourceClass) { this.sourceClass = sourceClass; }
        public Class<?> getTargetClass() { return targetClass; }
        public void setTargetClass(Class<?> targetClass) { this.targetClass = targetClass; }
        public String getMappingName() { return mappingName; }
        public void setMappingName(String mappingName) { this.mappingName = mappingName; }
        public Map<String, String> getFieldMappings() { return fieldMappings; }
        public void setFieldMappings(Map<String, String> fieldMappings) { this.fieldMappings = fieldMappings; }
        public boolean isBidirectional() { return bidirectional; }
        public void setBidirectional(boolean bidirectional) { this.bidirectional = bidirectional; }
    }
    
    // Response DTOs
    
    public static class ValidationResponse {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private Map<String, Object> validationDetails;
        
        public static ValidationResponse from(ModelTransformationUseCase.ValidationResult result) {
            ValidationResponse response = new ValidationResponse();
            response.valid = result.isValid();
            response.errors = result.getErrors();
            response.warnings = result.getWarnings();
            response.validationDetails = result.getValidationDetails();
            return response;
        }
        
        public boolean isValid() { return valid; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public Map<String, Object> getValidationDetails() { return validationDetails; }
    }
    
    public static class BatchValidationResponse {
        private int totalCount;
        private int validCount;
        private int invalidCount;
        private List<ValidationResponse> results;
        private List<String> allErrors;
        private List<String> allWarnings;
        
        public static BatchValidationResponse from(ModelTransformationUseCase.BatchValidationResult result) {
            BatchValidationResponse response = new BatchValidationResponse();
            response.totalCount = result.getTotalCount();
            response.validCount = result.getValidCount();
            response.invalidCount = result.getInvalidCount();
            response.results = result.getResults().stream()
                .map(ValidationResponse::from)
                .toList();
            response.allErrors = result.getAllErrors();
            response.allWarnings = result.getAllWarnings();
            return response;
        }
        
        public int getTotalCount() { return totalCount; }
        public int getValidCount() { return validCount; }
        public int getInvalidCount() { return invalidCount; }
        public List<ValidationResponse> getResults() { return results; }
        public List<String> getAllErrors() { return allErrors; }
        public List<String> getAllWarnings() { return allWarnings; }
    }
    
    public static class SerializationResponse {
        private boolean success;
        private String serializedData;
        private String format;
        private String errorMessage;
        
        public static SerializationResponse from(ModelTransformationUseCase.SerializationResult result) {
            SerializationResponse response = new SerializationResponse();
            response.success = result.isSuccess();
            response.serializedData = result.getSerializedData();
            response.format = result.getFormat();
            response.errorMessage = result.getErrorMessage();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public String getSerializedData() { return serializedData; }
        public String getFormat() { return format; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    public static class DeserializationResponse {
        private boolean success;
        private DomainEntity entity;
        private String errorMessage;
        
        public static DeserializationResponse from(ModelTransformationUseCase.DeserializationResult<?> result) {
            DeserializationResponse response = new DeserializationResponse();
            response.success = result.isSuccess();
            response.entity = result.getEntity();
            response.errorMessage = result.getErrorMessage();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public DomainEntity getEntity() { return entity; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    public static class ComparisonResponse {
        private boolean identical;
        private Map<String, FieldDifferenceResponse> differences;
        private double similarityScore;
        
        public static ComparisonResponse from(ModelTransformationUseCase.ComparisonResult result) {
            ComparisonResponse response = new ComparisonResponse();
            response.identical = result.isIdentical();
            response.similarityScore = result.getSimilarityScore();
            response.differences = result.getDifferences().entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> FieldDifferenceResponse.from(entry.getValue())
                ));
            return response;
        }
        
        public boolean isIdentical() { return identical; }
        public Map<String, FieldDifferenceResponse> getDifferences() { return differences; }
        public double getSimilarityScore() { return similarityScore; }
        
        public static class FieldDifferenceResponse {
            private String fieldName;
            private Object oldValue;
            private Object newValue;
            
            public static FieldDifferenceResponse from(ModelTransformationUseCase.ComparisonResult.FieldDifference diff) {
                FieldDifferenceResponse response = new FieldDifferenceResponse();
                response.fieldName = diff.getFieldName();
                response.oldValue = diff.getOldValue();
                response.newValue = diff.getNewValue();
                return response;
            }
            
            public String getFieldName() { return fieldName; }
            public Object getOldValue() { return oldValue; }
            public Object getNewValue() { return newValue; }
        }
    }
    
    public static class CopyResponse {
        private boolean success;
        private DomainEntity copiedEntity;
        private String errorMessage;
        
        public static CopyResponse from(ModelTransformationUseCase.CopyResult<?> result) {
            CopyResponse response = new CopyResponse();
            response.success = result.isSuccess();
            response.copiedEntity = result.getCopiedEntity();
            response.errorMessage = result.getErrorMessage();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public DomainEntity getCopiedEntity() { return copiedEntity; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    public static class MergeResponse {
        private boolean success;
        private DomainEntity mergedEntity;
        private List<String> changedFields;
        private String errorMessage;
        
        public static MergeResponse from(ModelTransformationUseCase.MergeResult<?> result) {
            MergeResponse response = new MergeResponse();
            response.success = result.isSuccess();
            response.mergedEntity = result.getMergedEntity();
            response.changedFields = result.getChangedFields();
            response.errorMessage = result.getErrorMessage();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public DomainEntity getMergedEntity() { return mergedEntity; }
        public List<String> getChangedFields() { return changedFields; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    public static class TransformationMappingResponse {
        private String sourceClass;
        private String targetClass;
        private String mappingName;
        private Map<String, String> fieldMappings;
        private boolean bidirectional;
        
        public static TransformationMappingResponse from(ModelTransformationUseCase.TransformationMapping mapping) {
            TransformationMappingResponse response = new TransformationMappingResponse();
            response.sourceClass = mapping.getSourceClass().getSimpleName();
            response.targetClass = mapping.getTargetClass().getSimpleName();
            response.mappingName = mapping.getMappingName();
            response.fieldMappings = mapping.getFieldMappings();
            response.bidirectional = mapping.isBidirectional();
            return response;
        }
        
        public String getSourceClass() { return sourceClass; }
        public String getTargetClass() { return targetClass; }
        public String getMappingName() { return mappingName; }
        public Map<String, String> getFieldMappings() { return fieldMappings; }
        public boolean isBidirectional() { return bidirectional; }
    }
    
    public static class RegistrationResponse {
        private boolean success;
        private String mappingId;
        private String errorMessage;
        
        public static RegistrationResponse from(ModelTransformationUseCase.RegistrationResult result) {
            RegistrationResponse response = new RegistrationResponse();
            response.success = result.isSuccess();
            response.mappingId = result.getMappingId();
            response.errorMessage = result.getErrorMessage();
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public String getMappingId() { return mappingId; }
        public String getErrorMessage() { return errorMessage; }
    }
}