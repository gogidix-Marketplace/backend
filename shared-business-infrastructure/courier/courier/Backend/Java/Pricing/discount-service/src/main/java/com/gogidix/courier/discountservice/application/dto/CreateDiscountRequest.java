package com.gogidix.courier.discountservice.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateDiscountRequest(
        @NotBlank(message = "Code is required")
        @Pattern(regexp = "^[A-Z0-9-_]{3,50}$", message = "Code must be 3-50 characters, uppercase letters, numbers, hyphens, underscores")
        String code,

        String description,

        @NotNull(message = "Discount type is required")
        DiscountTypeDto discountType,

        @NotNull(message = "Discount value is required")
        @DecimalMin(value = "0.01", message = "Discount value must be positive")
        BigDecimal discountValue,

        @DecimalMin(value = "0.01", message = "Max discount amount must be positive")
        BigDecimal maxDiscountAmount,

        @DecimalMin(value = "0.01", message = "Min order amount must be positive")
        BigDecimal minOrderAmount,

        LocalDate startDate,

        LocalDate endDate,

        @Min(value = 1, message = "Max uses must be at least 1")
        @Max(value = 1000000, message = "Max uses cannot exceed 1000000")
        Integer maxUses,

        @Min(value = 1, message = "Max uses per user must be at least 1")
        @Max(value = 100, message = "Max uses per user cannot exceed 100")
        Integer maxUsesPerUser,

        List<String> applicableZones,

        List<String> applicableServices
) {
    public enum DiscountTypeDto {
        PERCENTAGE, FIXED
    }
}
