package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Reconciliation Commands (Input Port)
 * Defines the input commands for reconciliation operations
 */
public interface ReconciliationCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Account ID is required")
        private String accountId;

        @NotBlank(message = "Account number is required")
        private String accountNumber;

        @NotBlank(message = "Statement ID is required")
        private String statementId;

        @NotNull(message = "Reconciliation date is required")
        private LocalDate reconciliationDate;

        @NotNull(message = "Period start date is required")
        private LocalDate periodStart;

        @NotNull(message = "Period end date is required")
        private LocalDate periodEnd;

        @NotNull(message = "Starting balance is required")
        private BigDecimal startingBalance;

        @NotNull(message = "Ending balance is required")
        private BigDecimal endingBalance;

        private BigDecimal tolerance;

        @NotNull(message = "Reconciliation method is required")
        private Reconciliation.ReconciliationMethod reconciliationMethod;

        private Boolean autoReconcile;

        private String notes;

        private String createdBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class StartReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        private String startedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        @NotBlank(message = "Reconciled by is required")
        private String reconciledBy;

        private BigDecimal bookBalance;

        private BigDecimal bankBalance;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        private String cancelledBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateBalancesCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        @NotNull(message = "Book balance is required")
        private BigDecimal bookBalance;

        @NotNull(message = "Bank balance is required")
        private BigDecimal bankBalance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetToleranceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        @NotNull(message = "Tolerance is required")
        private BigDecimal tolerance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MatchTransactionsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        @NotBlank(message = "Bank transaction ID is required")
        private String bankTransactionId;

        @NotBlank(message = "Book transaction ID is required")
        private String bookTransactionId;

        private String matchedBy;

        private Double matchConfidence;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UnmatchTransactionsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation line ID is required")
        private String reconciliationLineId;

        private String unmatchedBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class VerifyMatchCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation line ID is required")
        private String reconciliationLineId;

        @NotBlank(message = "Verified by is required")
        private String verifiedBy;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkDiscrepancyCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation line ID is required")
        private String reconciliationLineId;

        @NotNull(message = "Discrepancy category is required")
        private com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine.DiscrepancyCategory discrepancyCategory;

        @NotBlank(message = "Reason is required")
        private String reason;

        private com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine.ActionRequired actionRequired;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AutoReconcileCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        private BigDecimal tolerance;

        private Boolean requireExactAmountMatch;

        private Boolean allowDateVariance;

        private Integer dateVarianceDays;

        private Double minimumMatchConfidence;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteReconciliationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddNotesCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Reconciliation ID is required")
        private String reconciliationId;

        @NotBlank(message = "Notes are required")
        private String notes;
    }
}
