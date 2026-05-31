package com.gogidix.shared.warehousing.pricing.application.command;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Command to create a new pricing rule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePricingRuleCommand {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Service type is required")
    private PricingRule.ServiceType serviceType;

    @NotNull(message = "Storage type is required")
    private PricingRule.StorageType storageType;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base price must be positive")
    private BigDecimal basePrice;

    @NotNull(message = "Price unit is required")
    private PricingRule.PriceUnit priceUnit;

    private BigDecimal minimumCharge;

    @Builder.Default
    private PricingRule.BillingCycle billingCycle = PricingRule.BillingCycle.MONTHLY;

    private PricingRule.ZoneType zoneType;

    private String warehouseId;

    private Integer volumeThreshold;

    private Integer weightThreshold;

    private BigDecimal volumePriceOverride;

    private BigDecimal weightPriceOverride;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer priority = 0;

    @Builder.Default
    private String currency = "USD";
}
