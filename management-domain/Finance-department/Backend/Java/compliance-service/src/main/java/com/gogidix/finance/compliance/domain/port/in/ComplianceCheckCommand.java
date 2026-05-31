package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Compliance Check Commands (Input Port)
 * Defines the input commands for compliance check operations
 */
public interface ComplianceCheckCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        @NotBlank(message = "Rule name is required")
        private String ruleName;

        @NotBlank(message = "Entity type is required")
        private String entityType;

        @NotBlank(message = "Entity ID is required")
        private String entityId;

        private String referenceNumber;

        @NotBlank(message = "Evaluated by user ID is required")
        private String evaluatedByUserId;

        private Map<String, Object> context;

        private BigDecimal evaluatedAmount;

        private String evaluatedCurrency;

        private BigDecimal thresholdAmount;

        private String thresholdCurrency;

        private String department;

        private String costCenter;

        private String expenseCategory;

        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ExecuteCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Evaluated by is required")
        private String evaluatedBy;

        @NotNull(message = "Result is required")
        private ComplianceCheck.CheckResult result;

        private String violationDescription;

        private ComplianceRule.SeverityLevel severity;

        private Map<String, Object> context;

        private BigDecimal variance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class WaiveViolationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Waived by is required")
        private String waivedBy;

        @NotBlank(message = "Waiver reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssignRemediationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Assigned to is required")
        private String assignedTo;

        @NotBlank(message = "Action is required")
        private String action;

        @NotNull(message = "Due date is required")
        private Instant dueDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteRemediationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Completed by is required")
        private String completedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;
    }
}
