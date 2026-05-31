package com.gogidix.shared.warehousing.pricing.application.command;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Command to calculate price for storage service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatePriceCommand {

    @NotNull(message = "Service type is required")
    private PricingRule.ServiceType serviceType;

    @NotNull(message = "Storage type is required")
    private PricingRule.StorageType storageType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Integer volume;

    private Integer weight;

    private String warehouseId;

    private PricingRule.ZoneType zoneType;

    private Integer durationDays;

    private String currency;

    private String requestId;

    @Builder.Default
    private Integer validityDays = 30;

    private String promoCode;
}
