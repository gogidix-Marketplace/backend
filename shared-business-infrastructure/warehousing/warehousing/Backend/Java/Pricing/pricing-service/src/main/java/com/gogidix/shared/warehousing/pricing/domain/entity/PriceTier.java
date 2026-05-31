package com.gogidix.shared.warehousing.pricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Price Tier Entity
 *
 * Defines volume-based pricing tiers for discounted rates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "price_tiers")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'pricingRuleId': 1, 'minQuantity': 1}", name = "idx_tenant_rule_qty")
@CompoundIndex(def = "{'tenantId': 1, 'pricingRuleId': 1}", name = "idx_tenant_rule")
@CompoundIndex(def = "{'tenantId': 1, 'active': 1}", name = "idx_tenant_active")
public class PriceTier {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String pricingRuleId;

    private String name;

    private TierType tierType;

    private Integer minQuantity;

    private Integer maxQuantity;

    private BigDecimal discountPercentage;

    private BigDecimal adjustedPrice;

    private Boolean active;

    private Integer priority;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum TierType {
        VOLUME,
        WEIGHT,
        DURATION,
        VALUE
    }
}
