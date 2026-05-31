package com.gogidix.shared.warehousing.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a pricing rule is updated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleUpdatedEvent {

    private String pricingRuleId;
    private String tenantId;
    private String name;
    private String serviceType;
    private BigDecimal newBasePrice;
    private String currency;
    private LocalDateTime updatedAt;
    private String warehouseId;
}
