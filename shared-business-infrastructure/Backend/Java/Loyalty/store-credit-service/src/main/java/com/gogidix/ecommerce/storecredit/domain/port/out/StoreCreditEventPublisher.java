package com.gogidix.ecommerce.storecredit.domain.port.out;

import com.gogidix.ecommerce.storecredit.domain.event.StoreCreditDomainEvent;

public interface StoreCreditEventPublisher {
    void publish(StoreCreditDomainEvent event);
}
