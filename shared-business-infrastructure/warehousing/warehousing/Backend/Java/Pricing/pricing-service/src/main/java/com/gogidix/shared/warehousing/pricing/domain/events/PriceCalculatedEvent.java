package com.gogidix.shared.warehousing.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a price is calculated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculatedEvent {

    private String quoteId;
    private String quoteNumber;
    private String tenantId;
    private String requestId;
    private String serviceType;
    private String storageType;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDateTime calculatedAt;
    private LocalDateTime validUntil;
}
