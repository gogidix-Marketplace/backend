package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
import com.gogidix.sales.dealmanagement.domain.model.Competitor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Deal Response DTO
 * Represents deal data in API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealResponseDto {

    private String id;
    private String dealId;
    private String tenantId;
    private String dealName;
    private String dealCode;

    private DealStageDto stage;
    private Integer stageOrder;
    private Integer probability;

    private BigDecimal amount;
    private BigDecimal weightedAmount;
    private String currency;

    private String accountId;
    private String accountName;
    private String contactId;
    private String contactName;

    private String ownerId;
    private String ownerName;
    private List<String> teamMemberIds;

    private DealPriorityDto priority;
    private DealStatusDto status;
    private ApprovalStatusDto approvalStatus;
    private Boolean approvalRequired;
    private String approvedBy;
    private Instant approvedAt;

    private LocalDate expectedCloseDate;
    private LocalDate actualCloseDate;
    private LocalDate createdDate;
    private Integer dealDurationDays;

    private String source;
    private String campaign;
    private String leadSource;

    private String description;
    private String nextSteps;

    private String lossReason;
    private String lossReasonDetails;

    private List<ProductDto> products;
    private List<ActivityDto> activities;
    private List<CompetitorDto> competitors;

    private List<String> tags;
    private String region;
    private String industry;
    private String segment;
    private String territory;

    private String contractType;
    private Integer contractLengthMonths;
    private Boolean renewal;
    private String renewalDealId;

    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;

    private Instant createdAt;
    private Instant updatedAt;

    // Enums
    public enum DealStageDto {
        LEAD, QUALIFIED, PROPOSAL, NEGOTIATION, VERBAL_COMMIT, CLOSED_WON, CLOSED_LOST
    }

    public enum DealPriorityDto {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum DealStatusDto {
        OPEN, WON, LOST, ABANDONED, ON_HOLD
    }

    public enum ApprovalStatusDto {
        NOT_REQUIRED, PENDING, APPROVED, REJECTED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDto {
        private String productId;
        private String productName;
        private String productCode;
        private String productCategory;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private String currency;
        private Boolean isRecurring;
        private DealProduct.BillingCycle billingCycle;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityDto {
        private String activityId;
        private DealActivity.ActivityType activityType;
        private String subject;
        private String description;
        private String userName;
        private Instant activityDate;
        private Boolean isCompleted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetitorDto {
        private String competitorId;
        private String competitorName;
        private Competitor.StrengthLevel strength;
        private Competitor.ThreatLevel threat;
        private Integer probabilityOfWin;
        private String competingProduct;
    }

    /**
     * Pipeline view DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PipelineViewDto {
        private DealStageDto stage;
        private Integer order;
        private Integer probability;
        private BigDecimal totalAmount;
        private BigDecimal weightedAmount;
        private Integer dealCount;
        private List<DealSummaryDto> deals;
    }

    /**
     * Deal summary DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealSummaryDto {
        private String dealId;
        private String dealName;
        private String dealCode;
        private BigDecimal amount;
        private BigDecimal weightedAmount;
        private String currency;
        private DealStageDto stage;
        private Integer probability;
        private DealPriorityDto priority;
        private String ownerName;
        private String accountName;
        private LocalDate expectedCloseDate;
        private Integer daysInStage;
    }

    /**
     * Forecast DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastDto {
        private String period;
        private BigDecimal totalPipeline;
        private BigDecimal weightedForecast;
        private BigDecimal bestCase;
        private BigDecimal worstCase;
        private Integer openDeals;
        private Integer wonDeals;
        private BigDecimal wonAmount;
        private Double winRate;
    }
}
