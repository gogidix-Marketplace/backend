package com.gogidix.ecommerce.influencer.affiliate.application.dto;

import java.math.BigDecimal;

public record CreateAffiliateRequest(
    String userId, String affiliateCode, BigDecimal commissionRate
) {}
