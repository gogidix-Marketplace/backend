package com.gogidix.shared.warehousing.pricing.application.dto;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceQuote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Price Quote
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceQuoteDTO {

    private String id;
    private String tenantId;
    private String quoteNumber;
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
    private PriceQuote.QuoteStatus status;
    private String appliedRuleIds;
    private Map<String, Object> breakdown;
    private LocalDateTime createdAt;
    private LocalDateTime validUntil;
    private LocalDateTime expiresAt;
}
