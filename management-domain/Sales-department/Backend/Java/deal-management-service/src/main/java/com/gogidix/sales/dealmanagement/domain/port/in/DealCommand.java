package com.gogidix.sales.dealmanagement.domain.port.in;

import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
import com.gogidix.sales.dealmanagement.domain.model.Competitor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Deal Commands (Input Port)
 * Defines the input commands for deal operations
 */
public interface DealCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateDealCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal name is required")
        private String dealName;

        private String accountId;

        private String accountName;

        private String contactId;

        private String contactName;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        private Deal.DealStage stage;

        @NotBlank(message = "Owner ID is required")
        private String ownerId;

        private String ownerName;

        private LocalDate expectedCloseDate;

        private Deal.DealPriority priority;

        private String source;

        private String campaign;

        private String leadSource;

        private String description;

        private String nextSteps;

        private String region;

        private String industry;

        private String segment;

        private String territory;

        private String contractType;

        private Integer contractLengthMonths;

        private Boolean renewal;

        private String renewalDealId;

        private List<String> teamMemberIds;

        private List<String> tags;

        private List<CreateProductCommand> products;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateDealCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        private String dealName;

        private BigDecimal amount;

        private LocalDate expectedCloseDate;

        private Deal.DealPriority priority;

        private String description;

        private String nextSteps;

        private Integer probability;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AdvanceStageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RegressStageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotNull(message = "Target stage is required")
        private Deal.DealStage targetStage;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsWonCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        private BigDecimal finalAmount;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsLostCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "Loss reason is required")
        private String lossReason;

        private String lossDetails;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddProductCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "Product name is required")
        private String productName;

        private String productCode;

        private String productDescription;

        private String productCategory;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        private BigDecimal unitPrice;

        private String currency;

        private String serviceType;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean isRecurring;

        private DealProduct.BillingCycle billingCycle;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddActivityCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotNull(message = "Activity type is required")
        private DealActivity.ActivityType activityType;

        @NotBlank(message = "Subject is required")
        private String subject;

        private String description;

        private Instant dueDate;

        private DealActivity.Priority priority;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddCompetitorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "Competitor name is required")
        private String competitorName;

        private Competitor.StrengthLevel strength;

        private Competitor.ThreatLevel threat;

        private BigDecimal estimatedDealValue;

        private String competingProduct;

        private String competitorStrengths;

        private String competitorWeaknesses;

        private String ourAdvantage;

        private Integer probabilityOfWin;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTeamMemberCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveTeamMemberCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RequestApprovalCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveDealCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "Approver is required")
        private String approver;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectApprovalCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;

        @NotBlank(message = "Rejecter is required")
        private String rejecter;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteDealCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Deal ID is required")
        private String dealId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateProductCommand {
        private String productCode;

        @NotBlank(message = "Product name is required")
        private String productName;

        private String productDescription;

        private String productCategory;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        private BigDecimal unitPrice;

        private BigDecimal discountAmount;

        private BigDecimal discountPercentage;

        private String serviceType;

        private LocalDate startDate;

        private LocalDate endDate;

        private Boolean isRecurring;

        private DealProduct.BillingCycle billingCycle;
    }
}
