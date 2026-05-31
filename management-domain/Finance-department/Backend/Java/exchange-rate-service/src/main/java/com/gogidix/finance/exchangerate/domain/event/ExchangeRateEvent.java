package com.gogidix.finance.exchangerate.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String currencyPair;
    private BigDecimal rate;
    private String source;
    private LocalDateTime updatedAt;
    private Instant timestamp;

    public ExchangeRateEvent(String eventId, String eventType, String currencyPair,
                              BigDecimal rate, String source, LocalDateTime updatedAt, String tenantId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.currencyPair = currencyPair;
        this.rate = rate;
        this.source = source;
        this.updatedAt = updatedAt;
        this.tenantId = tenantId;
    }
}
