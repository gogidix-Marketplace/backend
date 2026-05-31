package com.gogidix.sales.forecast.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastLineItemEvent {

    private String lineItemId;
    private String forecastId;
    private String tenantId;
    private String name;
    private String category;
    private BigDecimal likely;
    private String currency;
    private String eventType;
    private Instant timestamp;
    private String eventId;

    public static ForecastLineItemEvent create(String lineItemId, String forecastId, String tenantId,
            String name, String category, BigDecimal likely, String currency, String action) {
        return ForecastLineItemEvent.builder()
            .lineItemId(lineItemId)
            .forecastId(forecastId)
            .tenantId(tenantId)
            .name(name)
            .category(category)
            .likely(likely)
            .currency(currency)
            .eventType(action)
            .timestamp(Instant.now())
            .eventId(java.util.UUID.randomUUID().toString())
            .build();
    }
}
