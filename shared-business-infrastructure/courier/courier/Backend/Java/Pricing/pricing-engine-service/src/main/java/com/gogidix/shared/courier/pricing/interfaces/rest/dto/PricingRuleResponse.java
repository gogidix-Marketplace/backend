package com.gogidix.shared.courier.pricing.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleResponse {

    private String id;
    private String tenantId;
    private String ruleId;
    private String serviceType;
    private String pricingModel;
    private Double basePrice;
    private Double pricePerKm;
    private Double pricePerKg;
    private Double expressMultiplier;
    private String currency;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
