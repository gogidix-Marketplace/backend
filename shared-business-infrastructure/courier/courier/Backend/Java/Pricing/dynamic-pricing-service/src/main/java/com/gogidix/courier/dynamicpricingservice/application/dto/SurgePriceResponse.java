package com.gogidix.courier.dynamicpricingservice.application.dto;

import java.math.BigDecimal;

public record SurgePriceResponse(
        BigDecimal basePrice,
        BigDecimal surgeMultiplier,
        BigDecimal surgeAmount,
        BigDecimal totalPrice,
        String demandLevel
) {
}
