package com.gogidix.shared.warehousing.pricing.application.dto;

import com.gogidix.shared.warehousing.pricing.domain.entity.PriceTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for Price Tier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceTierDTO {

    private String id;
    private String tenantId;
    private String pricingRuleId;
    private String name;
    private PriceTier.TierType tierType;
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal discountPercentage;
    private BigDecimal adjustedPrice;
    private Boolean active;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
