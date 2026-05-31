package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * DTO for ValidationRule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRuleDTO {

    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Code is required")
    private String code;

    private String description;

    @NotNull(message = "Rule type is required")
    private ValidationRule.RuleType ruleType;

    private String entityType;

    private String fieldName;

    private String jsonPath;

    @NotNull(message = "Operator is required")
    private ValidationRule.ValidationOperator operator;

    private String value;

    private List<String> allowedValues;

    private Map<String, Object> parameters;

    @NotNull(message = "Severity is required")
    private ValidationRule.SeverityLevel severity;

    private boolean enabled;

    private int priority;

    private String errorMessageTemplate;

    private Map<String, String> localizedErrorMessages;

    private ValidationRule.RuleStatus status;

    private int version;

    private String createdBy;

    private List<String> tags;

    private boolean requiresContext;

    private List<String> requiredContextFields;
}
