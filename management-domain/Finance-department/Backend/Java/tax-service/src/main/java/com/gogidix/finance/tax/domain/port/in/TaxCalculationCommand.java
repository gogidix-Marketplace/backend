package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Tax Calculation Commands (Input Port)
 * Defines the input commands for tax calculation operations
 */
public interface TaxCalculationCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateCalculationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotNull(message = "Transaction type is required")
        private TaxCalculation.TransactionType transactionType;

        @NotNull(message = "Transaction date is required")
        private LocalDate transactionDate;

        @NotNull(message = "Jurisdiction is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.Jurisdiction jurisdiction;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Base amount is required")
        @Positive(message = "Base amount must be positive")
        private BigDecimal baseAmount;

        @NotBlank(message = "Calculated by is required")
        private String calculatedBy;

        private Map<String, Object> context;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;

        @NotBlank(message = "Tax code is required")
        private String taxCode;

        @NotNull(message = "Tax type is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.TaxType taxType;

        @NotNull(message = "Rate is required")
        private BigDecimal rate;

        private Boolean isRecoverable;

        private String description;

        private Boolean isCompound;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddExemptionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;

        @NotBlank(message = "Exemption code is required")
        private String exemptionCode;

        @NotBlank(message = "Exemption type is required")
        private String exemptionType;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;

        private String reason;

        private String certificateNumber;

        private LocalDate certificateExpiry;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddDeductionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;

        @NotBlank(message = "Deduction type is required")
        private String deductionType;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;

        private String description;

        private String reference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class FinalizeCalculationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class VerifyCalculationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;

        @NotBlank(message = "Verified by is required")
        private String verifiedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsAppliedCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReverseCalculationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Calculation ID is required")
        private String calculationId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CalculateTaxCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotNull(message = "Transaction type is required")
        private TaxCalculation.TransactionType transactionType;

        @NotNull(message = "Transaction date is required")
        private LocalDate transactionDate;

        @NotNull(message = "Jurisdiction is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.Jurisdiction jurisdiction;

        @NotNull(message = "Tax type is required")
        private com.gogidix.finance.tax.domain.model.TaxRate.TaxType taxType;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Calculated by is required")
        private String calculatedBy;

        private String category;

        private String entityCode;

        private Map<String, Object> additionalContext;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BatchCalculateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotNull(message = "Transactions are required")
        private List<TransactionForCalculation> transactions;

        @NotBlank(message = "Calculated by is required")
        private String calculatedBy;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TransactionForCalculation {
            private String transactionId;
            private TaxCalculation.TransactionType transactionType;
            private LocalDate transactionDate;
            private com.gogidix.finance.tax.domain.model.TaxRate.Jurisdiction jurisdiction;
            private com.gogidix.finance.tax.domain.model.TaxRate.TaxType taxType;
            private String currency;
            private BigDecimal amount;
            private String category;
            private Map<String, Object> context;
        }
    }
}
