package com.gogidix.shared.warehousing.pricing.application.command;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Command to update an existing pricing rule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePricingRuleCommand {

    private String name;

    private String description;

    private PricingRule.ServiceType serviceType;

    private PricingRule.StorageType storageType;

    @DecimalMin(value = "0.0", inclusive = true, message = "Base price must be positive")
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
}
