package com.gogidix.finance.conversion.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionCompletedEvent {

    private String conversionId;
    private String tenantId;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private BigDecimal rate;
    private String provider;
    private String requestedBy;
    private String eventType;
    private Instant timestamp;
    private String eventId;

    public static ConversionCompletedEvent createBatchEvent(String tenantId, String requestedBy, int count, String eventType) {
        return ConversionCompletedEvent.builder()
            .eventId(java.util.UUID.randomUUID().toString())
            .tenantId(tenantId)
            .requestedBy(requestedBy)
            .eventType(eventType)
            .timestamp(Instant.now())
            .build();
    }
}
