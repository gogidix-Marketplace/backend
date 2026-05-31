package com.gogidix.ecommerce.influencer.affiliate.application.dto;

import java.math.BigDecimal;

public record AffiliateResponse(
    String id, String userId, String affiliateCode, BigDecimal totalEarnings
) {
    public static AffiliateResponse from(AffiliateDto dto) {
        return new AffiliateResponse(dto.id(), dto.userId(), dto.affiliateCode(), dto.totalEarnings());
    }
}
