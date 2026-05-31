package com.gogidix.courier.dynamicpricingservice.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CalculateSurgeRequest(
        @NotBlank(message = "Zone ID is required")
        String zoneId,

        @NotBlank(message = "Base price is required")
        @DecimalMin(value = "0.01", message = "Base price must be positive")
        BigDecimal basePrice,

        String serviceType
) {
}
