package com.gogidix.ecommerce.influencer.commission.application.dto;

import java.math.BigDecimal;

public record CommissionResponse(
    String id, String affiliateId, BigDecimal commission, String status
) {
    public static CommissionResponse from(CommissionDto dto) {
        return new CommissionResponse(dto.id(), dto.affiliateId(), dto.commission(), dto.status());
    }
}
