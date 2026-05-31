package com.gogidix.courier.dynamicpricingservice.application.dto;

import com.gogidix.courier.dynamicpricingservice.domain.entity.SurgeMultiplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SurgeStatusResponse(
        String zoneId,
        BigDecimal multiplier,
        SurgeMultiplier.DemandLevel demandLevel,
        LocalDateTime effectiveTime,
        LocalDateTime expiryTime,
        boolean active
) {
}
