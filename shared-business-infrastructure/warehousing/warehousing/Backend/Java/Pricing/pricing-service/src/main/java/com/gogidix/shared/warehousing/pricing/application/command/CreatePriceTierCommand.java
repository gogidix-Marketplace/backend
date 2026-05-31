package com.gogidix.shared.warehousing.pricing.application.command;

import com.gogidix.shared.warehousing.pricing.domain.entity.PriceTier;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Command to create a new price tier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePriceTierCommand {

    @NotBlank(message = "Pricing rule ID is required")
    private String pricingRuleId;

    private String name;

    @NotNull(message = "Tier type is required")
    private PriceTier.TierType tierType;

    @NotNull(message = "Minimum quantity is required")
    @Min(value = 1, message = "Minimum quantity must be at least 1")
    private Integer minQuantity;

    private Integer maxQuantity;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount percentage must be positive")
    private BigDecimal discountPercentage;

    private BigDecimal adjustedPrice;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer priority = 0;
}
