package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationResult;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * DTO for ValidationResult
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResultDTO {

    private String id;

    @NotBlank(message = "Validation ID is required")
    private String validationId;

    private String entityType;

    private String entityId;

    private String entityData;

    @NotBlank(message = "Rule code is required")
    private String ruleCode;

    private String ruleName;

    private boolean passed;

    private ValidationRule.SeverityLevel severity;

    private ValidationResult.ValidationStatus status;

    private String fieldName;

    private String actualValue;

    private String expectedValue;

    private String errorMessage;

    private Map<String, String> localizedErrorMessages;

    private String errorCode;

    private List<ValidationResult.ValidationIssue> issues;

    private Map<String, Object> context;

    private String validatedBy;

    private long executionTimeMs;

    private String batchId;

    private String tenantId;

    private Map<String, Object> metadata;
}
