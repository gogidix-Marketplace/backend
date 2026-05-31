package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.model.Quota;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Quota Commands (Input Port)
 * Defines the input commands for quota operations
 */
public interface QuotaCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Territory ID is required")
        private String territoryId;

        private String salesRepresentativeId;

        @NotNull(message = "Quota type is required")
        private Quota.QuotaType type;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Period is required")
        private Quota.QuotaPeriod period;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        @NotNull(message = "End date is required")
        private LocalDate endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;

        @NotBlank(message = "Approved by is required")
        private String approvedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PauseQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ResumeQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AdjustQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;

        @NotNull(message = "New amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal newAmount;

        @NotBlank(message = "Adjusted by is required")
        private String adjustedBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateAchievementCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;

        @NotNull(message = "Achievement is required")
        private BigDecimal achievement;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddQuotaBreakdownCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;

        @NotBlank(message = "Category is required")
        private String category;

        @NotNull(message = "Amount is required")
        private BigDecimal amount;

        private String description;
        private String productId;
        private String productCategoryId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteQuotaCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Quota ID is required")
        private String quotaId;
    }
}
