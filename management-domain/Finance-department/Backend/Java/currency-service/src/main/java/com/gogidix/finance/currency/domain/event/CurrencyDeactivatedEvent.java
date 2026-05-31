package com.gogidix.finance.currency.domain.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyDeactivatedEvent extends DomainEvent {

    private String currencyCode;

    public CurrencyDeactivatedEvent() {}

    public CurrencyDeactivatedEvent(String arg1, String arg2, String arg3) {
        super(arg1, "CURRENCY_DEACTIVATED", arg2, Instant.now());
        this.currencyCode = arg3;
    }
}
