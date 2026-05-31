package com.gogidix.courier.dynamicpricingservice.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateDemandRequest(
        @NotBlank(message = "Zone ID is required")
        String zoneId,

        @NotNull(message = "Active orders is required")
        @Min(value = 0, message = "Active orders cannot be negative")
        Integer activeOrders,

        @NotNull(message = "Available drivers is required")
        @Min(value = 0, message = "Available drivers cannot be negative")
        Integer availableDrivers
) {
}
