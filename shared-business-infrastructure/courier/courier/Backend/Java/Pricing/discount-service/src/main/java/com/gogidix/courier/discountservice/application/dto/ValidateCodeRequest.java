package com.gogidix.courier.discountservice.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ValidateCodeRequest(
        @NotBlank(message = "Discount code is required")
        String discountCode,

        @NotBlank(message = "User ID is required")
        String userId,

        @NotNull(message = "Order amount is required")
        @DecimalMin(value = "0.01", message = "Order amount must be positive")
        BigDecimal orderAmount,

        String zoneId,

        String serviceType
) {
}
