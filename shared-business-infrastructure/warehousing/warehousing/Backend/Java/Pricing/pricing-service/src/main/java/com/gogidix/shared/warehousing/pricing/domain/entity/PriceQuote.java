package com.gogidix.shared.warehousing.pricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Price Quote Entity
 *
 * Represents a calculated price quote for storage services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "price_quotes")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'quoteNumber': 1}", name = "idx_tenant_quote", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'requestId': 1}", name = "idx_tenant_request")
@CompoundIndex(def = "{'tenantId': 1, 'status': 1}", name = "idx_tenant_status")
public class PriceQuote {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String quoteNumber;

    @Indexed
    private String requestId;

    private PricingRule.ServiceType serviceType;

    private PricingRule.StorageType storageType;

    private Integer quantity;

    private Integer volume;

    private Integer weight;

    private BigDecimal unitPrice;

    private PricingRule.PriceUnit priceUnit;

    private BigDecimal baseAmount;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private String currency;

    private Integer validityDays;

    private QuoteStatus status;

    private String appliedRuleIds;

    private Map<String, Object> breakdown;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime validUntil;

    private LocalDateTime expiresAt;

    public enum QuoteStatus {
        DRAFT,
        CALCULATED,
        SENT,
        ACCEPTED,
        EXPIRED,
        CANCELLED
    }
}
