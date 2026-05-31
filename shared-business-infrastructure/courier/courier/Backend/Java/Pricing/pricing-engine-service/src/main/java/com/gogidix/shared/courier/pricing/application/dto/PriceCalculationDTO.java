package com.gogidix.shared.courier.pricing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for price calculation result
 * Contains detailed breakdown of the calculated price
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculationDTO {

    private String quoteId;

    private String tenantId;

    private BigDecimal baseFare;

    private BigDecimal distanceCharge;

    private BigDecimal timeCharge;

    private BigDecimal weightCharge;

    private BigDecimal surcharge;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal subtotal;

    private BigDecimal totalAmount;

    private String currency;

    private Integer validityMinutes;

    private LocalDateTime calculatedAt;

    private LocalDateTime expiresAt;

    private List<AppliedRule> appliedRules;

    private Map<String, Object> breakdown;

    private Map<String, String> metadata;

    /**
     * Applied pricing rule with details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppliedRule {
        private String ruleId;
        private String ruleName;
        private String ruleType;
        private BigDecimal amount;
        private String description;
    }
}
