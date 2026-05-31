package com.gogidix.platform.subscription.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for subscription responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {

    private String id;
    private String tenantId;
    private String subscriptionNumber;
    private String customerId;
    private String planId;
    private String status;
    private LocalDate trialStart;
    private LocalDate trialEnd;
    private LocalDateTime currentPeriodStart;
    private LocalDateTime currentPeriodEnd;
    private String billingCycle;
    private BigDecimal basePrice;
    private BigDecimal discountPercentage;
    private BigDecimal taxRate;
    private BigDecimal totalAmount;
    private String currency;
    private Integer currentUsers;
    private BigDecimal currentStorageGb;
    private Map<String, Object> usageJson;
    private boolean isTrial;
    private Integer trialDaysRemaining;
    private boolean autoRenew;
    private boolean cancelAtPeriodEnd;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private Map<String, Object> metadata;
    private String stripeSubscriptionId;
    private String stripeCustomerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
