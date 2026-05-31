package com.gogidix.shared.warehousing.pricing.application.dto;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Price Calculation Result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculationResultDTO {

    private String quoteId;
    private String quoteNumber;
    private PricingRule.ServiceType serviceType;
    private PricingRule.StorageType storageType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private PricingRule.PriceUnit priceUnit;
    private BigDecimal baseAmount;
    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;
    private BigDecimal taxAmount;
    private BigDecimal taxRate;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDateTime validUntil;
    private Map<String, Object> breakdown;
    private String appliedRuleDescription;
}
