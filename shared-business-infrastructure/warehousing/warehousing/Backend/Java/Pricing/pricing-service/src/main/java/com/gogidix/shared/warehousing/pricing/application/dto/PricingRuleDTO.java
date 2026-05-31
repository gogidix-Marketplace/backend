package com.gogidix.shared.warehousing.pricing.application.dto;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Pricing Rule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleDTO {

    private String id;
    private String tenantId;
    private String name;
    private String description;
    private PricingRule.ServiceType serviceType;
    private PricingRule.StorageType storageType;
    private BigDecimal basePrice;
    private PricingRule.PriceUnit priceUnit;
    private BigDecimal minimumCharge;
    private PricingRule.BillingCycle billingCycle;
    private PricingRule.ZoneType zoneType;
    private String warehouseId;
    private Integer volumeThreshold;
    private Integer weightThreshold;
    private BigDecimal volumePriceOverride;
    private BigDecimal weightPriceOverride;
    private Boolean active;
    private Integer priority;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
