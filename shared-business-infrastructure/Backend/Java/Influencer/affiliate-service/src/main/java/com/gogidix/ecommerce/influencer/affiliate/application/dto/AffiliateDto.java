package com.gogidix.ecommerce.influencer.affiliate.application.dto;

import java.time.Instant;
import java.math.BigDecimal;

public record AffiliateDto(
    String id, String tenantId, String userId, String affiliateCode,
    BigDecimal commissionRate, BigDecimal totalEarnings,
    boolean active, Instant createdAt, Instant updatedAt
) {}
