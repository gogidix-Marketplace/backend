package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Compliance Rule Commands (Input Port)
 * Defines the input commands for compliance rule operations
 */
public interface ComplianceRuleCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateComplianceRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule name is required")
        private String name;

        private String description;

        @NotNull(message = "Rule type is required")
        private ComplianceRule.RuleType ruleType;

        @NotNull(message = "Category is required")
        private ComplianceRule.RuleCategory category;

        @NotNull(message = "Severity level is required")
        private ComplianceRule.SeverityLevel severity;

        private Map<String, Object> parameters;

        private BigDecimal thresholdAmount;

        private String thresholdCurrency;

        private String conditionExpression;

        private List<String> applicableDepartments;

        private List<String> applicableCostCenters;

        private List<String> applicableExpenseCategories;

        @NotBlank(message = "Created by user ID is required")
        private String createdByUserId;

        private Instant effectiveFrom;

        private Instant effectiveTo;

        private String approvalRequiredBy;

        private Boolean autoApproveThreshold;

        private Integer priority;

        private List<String> tags;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateComplianceRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        private String name;

        private String description;

        private Map<String, Object> parameters;

        private BigDecimal thresholdAmount;

        private String thresholdCurrency;

        private String conditionExpression;

        private List<String> applicableDepartments;

        private List<String> applicableCostCenters;

        private List<String> applicableExpenseCategories;

        @NotBlank(message = "Modified by user ID is required")
        private String modifiedByUserId;

        private Instant effectiveFrom;

        private Instant effectiveTo;

        private Integer priority;

        private List<String> tags;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        @NotBlank(message = "Activated by user ID is required")
        private String activatedByUserId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        @NotBlank(message = "Deactivated by user ID is required")
        private String deactivatedByUserId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;
    }
}
