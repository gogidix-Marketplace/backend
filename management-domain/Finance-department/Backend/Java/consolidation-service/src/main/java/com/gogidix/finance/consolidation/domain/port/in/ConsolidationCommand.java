package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Consolidation Commands (Input Port)
 * Defines the input commands for consolidation operations
 */
public interface ConsolidationCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateConsolidationRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule name is required")
        private String ruleName;

        private String description;

        @NotNull(message = "Rule type is required")
        private ConsolidationRule.RuleType ruleType;

        @NotNull(message = "Rule scope is required")
        private ConsolidationRule.RuleScope ruleScope;

        @NotNull(message = "Consolidation method is required")
        private ConsolidationRule.ConsolidationMethod consolidationMethod;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        private List<String> applicableDepartments;

        private List<String> applicableCostCenters;

        private List<String> applicableSubsidiaries;

        private LocalDate effectiveFrom;

        private LocalDate effectiveTo;

        private String currencyCode;

        private ConsolidationRule.CurrencyConversionMethod conversionMethod;

        private Boolean enableIntercompanyElimination;

        private ConsolidationRule.IntercompanyElimination eliminationRule;

        private List<ConsolidationRule.AdjustmentRule> adjustmentRules;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateConsolidationRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        private String ruleName;

        private String description;

        private ConsolidationRule.ConsolidationMethod consolidationMethod;

        private String currencyCode;

        private ConsolidationRule.CurrencyConversionMethod conversionMethod;

        private BigDecimal exchangeRate;

        private String exchangeRateSource;

        private Boolean active;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class StartConsolidationJobCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Job name is required")
        private String jobName;

        private String description;

        @NotNull(message = "Job type is required")
        private com.gogidix.finance.consolidation.domain.model.ConsolidationJob.JobType jobType;

        @NotBlank(message = "Initiated by is required")
        private String initiatedBy;

        @NotNull(message = "Period start is required")
        private LocalDate periodStart;

        @NotNull(message = "Period end is required")
        private LocalDate periodEnd;

        private String ruleId;

        private String baseCurrency;

        private List<String> includedSubsidiaries;

        private List<String> includedDepartments;

        private List<String> includedCostCenters;

        private Map<String, Object> parameters;

        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelConsolidationJobCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Job ID is required")
        private String jobId;

        private String cancelledBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RetryConsolidationJobCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Job ID is required")
        private String jobId;

        @NotBlank(message = "Retried by is required")
        private String retriedBy;

        private String fromStep;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveConsolidationRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteConsolidationRuleCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        @NotBlank(message = "Deleted by is required")
        private String deletedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GenerateConsolidationReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report name is required")
        private String reportName;

        @NotNull(message = "Report type is required")
        private com.gogidix.finance.consolidation.domain.model.ConsolidationReport.ReportType reportType;

        @NotNull(message = "Period end is required")
        private LocalDate periodEnd;

        private LocalDate periodStart;

        @NotBlank(message = "Base currency is required")
        private String baseCurrency;

        @NotBlank(message = "Generated by is required")
        private String generatedBy;

        private String jobId;

        private List<String> includedSubsidiaries;

        private Map<String, Object> parameters;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveConsolidationReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectConsolidationReportCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Report ID is required")
        private String reportId;

        @NotBlank(message = "Rejected by is required")
        private String rejectedBy;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateExchangeRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Rule ID is required")
        private String ruleId;

        @NotNull(message = "Exchange rate is required")
        private BigDecimal exchangeRate;

        @NotBlank(message = "Source is required")
        private String source;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }
}
