package com.gogidix.shared.courier.pricing.interfaces.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePricingRuleRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "Base price must be positive")
    private Double basePrice;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price per km must be positive")
    private Double pricePerKm;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price per kg must be positive")
    private Double pricePerKg;

    @DecimalMin(value = "1.0", inclusive = true, message = "Express multiplier must be at least 1.0")
    private Double expressMultiplier;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
