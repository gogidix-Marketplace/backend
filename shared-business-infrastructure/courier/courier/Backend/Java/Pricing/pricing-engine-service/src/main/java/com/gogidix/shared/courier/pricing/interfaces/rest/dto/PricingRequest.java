package com.gogidix.shared.courier.pricing.interfaces.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRequest {

    @NotBlank(message = "Service type is required")
    private String serviceType; // STANDARD, EXPRESS, SAME_DAY

    @NotNull(message = "Distance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Distance must be positive")
    private Double distanceKm;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Weight must be positive")
    private Double weightKg;
}
