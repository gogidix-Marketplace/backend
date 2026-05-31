package com.gogidix.sales.forecast.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

/**
 * Forecast Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponseDto {

    private String id;

    private String forecastId;

    private String tenantId;

    private String name;

    private String description;

    private ForecastPeriodDto period;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth startDate;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth endDate;

    private ForecastStatusDto status;

    private String createdBy;

    private String approvedBy;

    private Integer version;

    private String parentForecastId;

    private BigDecimal totalBestCase;

    private BigDecimal totalLikely;

    private BigDecimal totalWorstCase;

    private String currency;

    private String region;

    private String territory;

    private String businessUnit;

    private List<ForecastLineItemDto> lineItems;

    private ApprovalLevelDto currentApprovalLevel;

    private String rejectionReason;

    private Boolean locked;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ForecastPeriodDto {
        MONTHLY, QUARTERLY, ANNUAL
    }

    public enum ForecastStatusDto {
        DRAFT, SUBMITTED, PENDING_APPROVAL, APPROVED, REJECTED, PUBLISHED, ARCHIVED
    }

    public enum ApprovalLevelDto {
        NONE, MANAGER, DIRECTOR, VP, EXECUTIVE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastLineItemDto {
        private String lineItemId;
        private String name;
        private String description;
        private String category;
        private String type;
        private BigDecimal bestCase;
        private BigDecimal likely;
        private BigDecimal worstCase;
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
        private Boolean active;
    }
}
