package com.gogidix.ecommerce.influencer.commission.application.dto;

import java.math.BigDecimal;

public record CreateCommissionRequest(
    String affiliateId, String orderId, BigDecimal amount, BigDecimal commissionRate
) {}
