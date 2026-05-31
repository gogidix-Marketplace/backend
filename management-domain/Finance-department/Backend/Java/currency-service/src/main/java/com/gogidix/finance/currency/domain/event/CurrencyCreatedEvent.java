package com.gogidix.finance.currency.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyCreatedEvent extends DomainEvent {

    private String currencyCode;

    public CurrencyCreatedEvent() {}

    public CurrencyCreatedEvent(String arg1, String arg2, String arg3) {
        super(arg1, "CURRENCY_CREATED", arg2, Instant.now());
        this.currencyCode = arg3;
    }
}
