package com.gogidix.finance.budgettracking.domain.port.in;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/**
 * Budget Monitor Commands (Input Port)
 * Defines the input commands for budget monitoring operations
 */
public interface BudgetMonitorCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateMonitorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Budget ID is required")
        private String budgetId;

        @NotBlank(message = "Budget code is required")
        private String budgetCode;

        @NotBlank(message = "Budget name is required")
        private String budgetName;

        @NotBlank(message = "Budget period is required")
        private String budgetPeriod;

        @NotNull(message = "Period is required")
        private YearMonth period;

        @NotNull(message = "Allocated amount is required")
        @Positive(message = "Allocated amount must be positive")
        private BigDecimal allocatedAmount;

        @NotBlank(message = "Currency is required")
        private String currency;

        private String category;

        private String department;

        private String costCenter;

        private String fiscalYear;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        private List<String> alertRecipients;

        private List<ThresholdConfig> thresholds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ThresholdConfig {
        private String thresholdType;
        private BigDecimal thresholdValue;
        private BudgetMonitor.ThresholdLevel level;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecordExpenditureCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecordCommitmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReleaseCommitmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AdjustAllocationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotNull(message = "New allocation amount is required")
        private BigDecimal newAllocation;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReverseExpenditureCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CheckThresholdCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotBlank(message = "Threshold type is required")
        private String thresholdType;

        @NotNull(message = "Threshold value is required")
        private BigDecimal thresholdValue;

        @NotNull(message = "Threshold level is required")
        private BudgetMonitor.ThresholdLevel level;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AcknowledgeThresholdCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotBlank(message = "Threshold type is required")
        private String thresholdType;

        @NotBlank(message = "Acknowledged by is required")
        private String acknowledgedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAlertRecipientCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotBlank(message = "Recipient is required")
        private String recipient;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveAlertRecipientCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        @NotBlank(message = "Recipient is required")
        private String recipient;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateMonitorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Monitor ID is required")
        private String monitorId;

        private String budgetName;

        private BigDecimal allocatedAmount;

        private String category;

        private String department;

        private String costCenter;
    }
}
