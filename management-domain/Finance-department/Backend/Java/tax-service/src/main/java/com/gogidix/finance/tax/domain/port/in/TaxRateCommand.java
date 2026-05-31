package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.model.TaxRate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Tax Rate Commands (Input Port)
 * Defines the input commands for tax rate operations
 */
public interface TaxRateCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotNull(message = "Jurisdiction is required")
        private TaxRate.Jurisdiction jurisdiction;

        @NotNull(message = "Tax type is required")
        private TaxRate.TaxType taxType;

        @NotBlank(message = "Tax code is required")
        private String taxCode;

        @NotNull(message = "Rate percentage is required")
        @Positive(message = "Rate must be positive")
        private BigDecimal ratePercentage;

        @NotNull(message = "Effective date is required")
        private LocalDate effectiveDate;

        private LocalDate expiryDate;

        private String description;

        private Boolean isCompound;

        private Boolean isRecoverable;

        private BigDecimal recoveryRate;

        private BigDecimal minThreshold;

        private BigDecimal maxThreshold;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Tax rate ID is required")
        private String taxRateId;

        private BigDecimal newRate;

        private String description;

        private LocalDate newExpiryDate;

        private Boolean isCompound;

        private Boolean isRecoverable;

        private BigDecimal recoveryRate;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Tax rate ID is required")
        private String taxRateId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ExpireTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Tax rate ID is required")
        private String taxRateId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Tax rate ID is required")
        private String taxRateId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateNewVersionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Tax rate ID is required")
        private String taxRateId;

        @NotNull(message = "New rate is required")
        private BigDecimal newRate;

        @NotNull(message = "New effective date is required")
        private LocalDate newEffectiveDate;

        @NotBlank(message = "Created by is required")
        private String createdBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteTaxRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Tax rate ID is required")
        private String taxRateId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BatchUpdateRatesCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotNull(message = "Rates are required")
        private List<RateUpdate> rates;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RateUpdate {
            private String taxRateId;
            private BigDecimal newRate;
            private LocalDate newEffectiveDate;
        }
    }
}
