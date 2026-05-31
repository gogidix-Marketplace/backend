package com.gogidix.finance.exchangerate.domain.port.out;

import com.gogidix.finance.exchangerate.domain.event.ExchangeRateEvent;

public interface EventPublisher {
    void publish(ExchangeRateEvent event);
}
