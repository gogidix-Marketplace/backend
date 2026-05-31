package com.gogidix.finance.tax.domain.port.out;

public interface TaxDomainEventPublisher {
    void publish(Object domainEvent);
}
