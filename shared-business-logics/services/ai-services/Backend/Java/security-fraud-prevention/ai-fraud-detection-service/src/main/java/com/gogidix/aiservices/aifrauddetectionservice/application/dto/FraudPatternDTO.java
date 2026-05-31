package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for Fraud Pattern information.
 * Contains fraud pattern details and configuration.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FraudPatternDTO extends BaseDTO {

    @NotBlank(message = "Pattern ID is required")
    private String patternId;

    @NotBlank(message = "Pattern name is required")
    private String patternName;

    @NotBlank(message = "Pattern type is required")
    private String patternType;

    private String description;

    private String tenantId;

    @NotNull(message = "Confidence score is required")
    @DecimalMin(value = "0.0", message = "Confidence score must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Confidence score must be at most 1.0")
    private Double confidenceScore;

    private Double threshold;

    private Instant lastSeen;

    private Integer occurrenceCount;

    private Boolean isActive;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    private Map<String, Object> metadata;
}
