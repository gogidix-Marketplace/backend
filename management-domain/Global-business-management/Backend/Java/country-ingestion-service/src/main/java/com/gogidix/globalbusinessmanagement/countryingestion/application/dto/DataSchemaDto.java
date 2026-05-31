package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.DataSchema;

/**
 * DTO for DataSchema entity.
 * Used for transferring schema information between layers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Schema DTO for validation rules and field mappings")
public class DataSchemaDto {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Schema identifier", example = "COUNTRY_DATA_V1")
    @NotBlank(message = "Schema ID is required")
    private String schemaId;

    @Schema(description = "Schema name", example = "Country Data Schema")
    @NotBlank(message = "Schema name is required")
    private String schemaName;

    @Schema(description = "Schema description")
    private String description;

    @Schema(description = "Schema version", example = "1.0")
    @NotNull(message = "Version is required")
    private String version;

    @Schema(description = "Active status")
    private Boolean active;

    @Schema(description = "Schema type")
    private SchemaTypeDto schemaType;

    @Schema(description = "Target entity name")
    private String targetEntity;

    @Schema(description = "Field definitions")
    @Valid
    private List<SchemaFieldDto> fields;

    @Schema(description = "Field mappings from source to target")
    private Map<String, String> fieldMappings;

    @Schema(description = "Validation rules by field")
    private Map<String, DataSchema.ValidationRule> validationRules;

    @Schema(description = "Transformation rules by field")
    private Map<String, DataSchema.TransformationRule> transformationRules;

    @Schema(description = "Strict validation mode")
    private Boolean strictValidation;

    @Schema(description = "Allow unknown fields")
    private Boolean allowUnknownFields;

    @Schema(description = "Stop on first error")
    private Boolean stopOnFirstError;

    @Schema(description = "User who created the schema")
    private String createdBy;

    @Schema(description = "Creation timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @Schema(description = "Last modified by user")
    private String lastModifiedBy;

    @Schema(description = "Last modification timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "Organization ID")
    private String organizationId;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Number of batches using this schema")
    private Integer usageCount;

    /**
     * Schema field DTO.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Schema field definition")
    public static class SchemaFieldDto {
        @Schema(description = "Field name")
        private String name;

        @Schema(description = "Display name")
        private String displayName;

        @Schema(description = "Field type")
        private FieldTypeDto type;

        @Schema(description = "Is required")
        private boolean required;

        @Schema(description = "Is unique")
        private boolean unique;

        @Schema(description = "Minimum length")
        private Integer minLength;

        @Schema(description = "Maximum length")
        private Integer maxLength;

        @Schema(description = "Validation pattern")
        private String pattern;

        @Schema(description = "Minimum value")
        private Object minValue;

        @Schema(description = "Maximum value")
        private Object maxValue;

        @Schema(description = "Default value")
        private String defaultValue;

        @Schema(description = "Allowed values")
        private List<String> allowedValues;

        @Schema(description = "Field description")
        private String description;

        @Schema(description = "Field order")
        private int order;
    }

    /**
     * Schema type DTO enum.
     */
    @Schema(description = "Schema type enumeration")
    public enum SchemaTypeDto {
        COUNTRY_DATA,
        REGIONAL_DATA,
        ECONOMIC_DATA,
        DEMOGRAPHIC_DATA,
        TRADE_DATA,
        CUSTOM
    }

    /**
     * Field type DTO enum.
     */
    @Schema(description = "Field type enumeration")
    public enum FieldTypeDto {
        STRING,
        INTEGER,
        LONG,
        DECIMAL,
        BOOLEAN,
        DATE,
        DATETIME,
        ENUM,
        ARRAY,
        OBJECT
    }

    /**
     * Creates a minimal DTO for schema creation.
     */
    public static DataSchemaDto createMinimal(String schemaId, String schemaName, String version) {
        return DataSchemaDto.builder()
                .schemaId(schemaId)
                .schemaName(schemaName)
                .version(version)
                .active(true)
                .strictValidation(false)
                .allowUnknownFields(false)
                .stopOnFirstError(false)
                .build();
    }

    /**
     * Checks if schema is active.
     */
    public boolean isActive() {
        return active != null && active;
    }

    /**
     * Checks if strict validation is enabled.
     */
    public boolean isStrict() {
        return strictValidation != null && strictValidation;
    }
}
