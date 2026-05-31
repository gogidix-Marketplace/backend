package com.gogidix.corporate.website.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingInfo {
    private BigDecimal basePrice;
    private String currency;
    private String billingCycle;
    private Map<String, BigDecimal> regionalPricing;
    private boolean displayPricing;
    private String startingFromText;
}
