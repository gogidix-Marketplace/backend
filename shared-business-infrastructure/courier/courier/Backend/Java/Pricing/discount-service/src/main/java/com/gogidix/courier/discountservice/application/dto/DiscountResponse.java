package com.gogidix.courier.discountservice.application.dto;

import com.gogidix.courier.discountservice.domain.entity.DiscountCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record DiscountResponse(
        String id,
        String tenantId,
        String code,
        String description,
        DiscountCode.DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal maxDiscountAmount,
        BigDecimal minOrderAmount,
        LocalDate startDate,
        LocalDate endDate,
        Integer maxUses,
        Integer currentUses,
        Integer maxUsesPerUser,
        DiscountCode.DiscountStatus status,
        String applicableZones,
        String applicableServices,
        Instant createdAt,
        Instant updatedAt
) {
}
