package com.gogidix.courier.discountservice.application.dto;

import java.math.BigDecimal;

public record ApplyDiscountResponse(
        String usageId,
        String discountCode,
        BigDecimal originalAmount,
        BigDecimal discountAmount,
        BigDecimal finalAmount,
        String message
) {
}
