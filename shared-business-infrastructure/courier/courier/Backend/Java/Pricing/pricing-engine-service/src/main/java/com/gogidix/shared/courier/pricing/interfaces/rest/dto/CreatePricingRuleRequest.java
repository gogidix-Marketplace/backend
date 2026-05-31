package com.gogidix.shared.courier.pricing.interfaces.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePricingRuleRequest {

    @NotBlank(message = "Service type is required")
    private String serviceType; // STANDARD, EXPRESS, SAME_DAY

    @NotBlank(message = "Pricing model is required")
    private String pricingModel; // DISTANCE_BASED, WEIGHT_BASED, FLAT_RATE, DYNAMIC, TIERED

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base price must be positive")
    private Double basePrice;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price per km must be positive")
    private Double pricePerKm;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price per kg must be positive")
    private Double pricePerKg;

    @DecimalMin(value = "1.0", inclusive = true, message = "Express multiplier must be at least 1.0")
    private Double expressMultiplier;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Effective from date is required")
    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveTo;
}
