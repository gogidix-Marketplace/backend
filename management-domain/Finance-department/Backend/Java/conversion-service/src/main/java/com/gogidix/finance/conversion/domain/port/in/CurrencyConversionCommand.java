package com.gogidix.finance.conversion.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Currency Conversion Commands (Input Port)
 * Defines the input commands for currency conversion operations
 */
public interface CurrencyConversionCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ConvertAmountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requested by is required")
        private String requestedBy;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Source currency is required")
        private String fromCurrency;

        @NotBlank(message = "Target currency is required")
        private String toCurrency;

        private String correlationId;

        private String provider;

        private Boolean applyFee;

        private BigDecimal feePercentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BatchConvertCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requested by is required")
        private String requestedBy;

        @NotEmpty(message = "Conversions list cannot be empty")
        private List<ConversionItem> conversions;

        private String correlationId;

        private String provider;

        private Boolean applyFee;

        private BigDecimal feePercentage;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ConversionItem {
            @NotNull(message = "Amount is required")
            @Positive(message = "Amount must be positive")
            private BigDecimal amount;

            @NotBlank(message = "Source currency is required")
            private String fromCurrency;

            @NotBlank(message = "Target currency is required")
            private String toCurrency;

            private String reference;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReverseConversionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversion ID is required")
        private String conversionId;

        @NotBlank(message = "Requested by is required")
        private String requestedBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RefreshRateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Source currency is required")
        private String fromCurrency;

        @NotBlank(message = "Target currency is required")
        private String toCurrency;

        private String provider;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class InvalidateRateCacheCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Source currency is required")
        private String fromCurrency;

        @NotBlank(message = "Target currency is required")
        private String toCurrency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateRateThresholdsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        private BigDecimal rateChangeThreshold;

        private Long cacheTtlSeconds;
    }
}
