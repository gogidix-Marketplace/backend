package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.domain.model.ForecastLineItem;
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
 * Forecast Commands (Input Port)
 * Defines the input commands for forecast operations
 */
public interface ForecastCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Period is required")
        private Forecast.ForecastPeriod period;

        @NotNull(message = "Start date is required")
        private YearMonth startDate;

        @NotNull(message = "End date is required")
        private YearMonth endDate;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        @NotBlank(message = "Currency is required")
        private String currency;

        private String region;

        private String territory;

        private String businessUnit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        private String name;

        private String description;

        private YearMonth startDate;

        private YearMonth endDate;

        private String region;

        private String territory;

        private String businessUnit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        @NotBlank(message = "Approver is required")
        private String approver;

        @NotNull(message = "Approval level is required")
        private Forecast.ApprovalLevel approvalLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        @NotBlank(message = "Rejecter is required")
        private String rejecter;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PublishForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddLineItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Category is required")
        private ForecastLineItem.ForecastCategory category;

        @NotNull(message = "Type is required")
        private ForecastLineItem.LineItemType type;

        @NotNull(message = "Best case is required")
        @Positive(message = "Best case must be positive")
        private BigDecimal bestCase;

        @NotNull(message = "Likely is required")
        @Positive(message = "Likely must be positive")
        private BigDecimal likely;

        @NotNull(message = "Worst case is required")
        @Positive(message = "Worst case must be positive")
        private BigDecimal worstCase;

        @NotBlank(message = "Currency is required")
        private String currency;

        private String productId;

        private String productName;

        private String territoryId;

        private String territoryName;

        private String customerSegmentId;

        private String customerSegmentName;

        private String salesChannel;

        private String notes;

        private String owner;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateLineItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        @NotBlank(message = "Line item ID is required")
        private String lineItemId;

        private String name;

        private ForecastLineItem.ForecastCategory category;

        private BigDecimal bestCase;

        private BigDecimal likely;

        private BigDecimal worstCase;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveLineItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        @NotBlank(message = "Line item ID is required")
        private String lineItemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateVersionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LockForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UnlockForecastCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Forecast ID is required")
        private String forecastId;
    }
}
