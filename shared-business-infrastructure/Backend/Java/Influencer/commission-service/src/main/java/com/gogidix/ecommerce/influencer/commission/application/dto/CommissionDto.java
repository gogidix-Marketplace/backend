package com.gogidix.ecommerce.influencer.commission.application.dto;

import java.time.Instant;
import java.math.BigDecimal;

public record CommissionDto(
    String id, String tenantId, String affiliateId, String orderId,
    BigDecimal amount, BigDecimal commissionRate, BigDecimal commission,
    String status, Instant createdAt, Instant updatedAt
) {}
