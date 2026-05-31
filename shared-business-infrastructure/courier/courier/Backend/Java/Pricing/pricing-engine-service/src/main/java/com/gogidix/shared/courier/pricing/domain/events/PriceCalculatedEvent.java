package com.gogidix.shared.courier.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain Event: Price has been calculated
 * Published when a price quote is generated
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCalculatedEvent {

    private String tenantId;

    private String quoteId;

    private String serviceType;

    private String vehicleType;

    private String totalAmount;

    private String currency;

    private LocalDateTime calculatedAt;

    private Map<String, Object> metadata;
}
