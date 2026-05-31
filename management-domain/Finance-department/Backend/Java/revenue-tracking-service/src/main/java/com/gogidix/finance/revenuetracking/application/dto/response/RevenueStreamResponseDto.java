package com.gogidix.finance.revenuetracking.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Revenue Stream Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStreamResponseDto {

    private String id;

    private String streamId;

    private String tenantId;

    private String customerId;

    private String customerName;

    private String streamName;

    private String description;

    private RevenueStreamTypeDto type;

    private StreamStatusDto status;

    private BigDecimal recurringAmount;

    private String currency;

    private BillingCycleDto billingCycle;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer contractTermMonths;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextBillingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastBillingDate;

    private Integer billingCount;

    private Integer totalBillingCycles;

    private BigDecimal totalValue;

    private BigDecimal recognizedValue;

    private PaymentTermsDto paymentTerms;

    private AutoRenewalDto autoRenewal;

    private Integer renewalReminderDays;

    private String productId;

    private String productSku;

    private String salespersonId;

    private String department;

    private String costCenter;

    private String region;

    private String contractUrl;

    private List<StreamTierDto> tiers;

    private List<String> tags;

    private String notes;

    private BigDecimal mrr;

    private BigDecimal arr;

    private Boolean isTrial;

    private Integer trialDays;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trialEndDate;

    private String cancellationReason;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate cancellationDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum RevenueStreamTypeDto {
        SUBSCRIPTION, RETAINER, MAINTENANCE, SUPPORT, LICENSE,
        SERVICE_CONTRACT, LEASE, MEMBERSHIP, WARRANTY, OTHER
    }

    public enum StreamStatusDto {
        ACTIVE, PENDING, SUSPENDED, CANCELLED, EXPIRED, TRIAL, PAST_DUE
    }

    public enum BillingCycleDto {
        WEEKLY, BI_WEEKLY, MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY, CUSTOM
    }

    public enum PaymentTermsDto {
        NET_15, NET_30, NET_45, NET_60, COD, PREPAID, AUTO_CHARGE
    }

    public enum AutoRenewalDto {
        ENABLED, DISABLED, OPT_IN, OPT_OUT
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StreamTierDto {
        private String tierId;
        private String name;
        private String description;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private BigDecimal pricePerUnit;
        private Boolean included;
    }
}
