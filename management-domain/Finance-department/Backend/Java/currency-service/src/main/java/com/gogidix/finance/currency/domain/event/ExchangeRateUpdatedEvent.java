package com.gogidix.finance.currency.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExchangeRateUpdatedEvent extends DomainEvent {

    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal newRate;
    private BigDecimal oldRate;
    private String source;

    public ExchangeRateUpdatedEvent() {}

    public ExchangeRateUpdatedEvent(String tenantId, String baseCurrency, String quoteCurrency,
                                     BigDecimal newRate, BigDecimal oldRate, String source) {
        super(null, "EXCHANGE_RATE_UPDATED", tenantId, java.time.Instant.now());
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.newRate = newRate;
        this.oldRate = oldRate;
        this.source = source;
    }
}
