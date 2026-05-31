package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for Threshold configuration.
 * Contains fraud detection threshold settings.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ThresholdDTO extends BaseDTO {

    @NotBlank(message = "Threshold name is required")
    private String thresholdName;

    private String displayName;

    private String description;

    private Double minValue;

    private Double maxValue;

    private String actionOnViolation;

    private String category;

    private String tenantId;

    private Boolean isEnabled;

    private Integer priority;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    private Map<String, Object> metadata;
}
