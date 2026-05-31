package com.gogidix.shared.warehousing.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a pricing rule is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleCreatedEvent {

    private String pricingRuleId;
    private String tenantId;
    private String name;
    private String serviceType;
    private String storageType;
    private BigDecimal basePrice;
    private String priceUnit;
    private String currency;
    private LocalDateTime createdAt;
    private String warehouseId;
}
