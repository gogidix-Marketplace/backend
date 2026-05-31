package com.gogidix.ecommerce.payment.domain.port.out;

import com.gogidix.ecommerce.payment.domain.event.PaymentDomainEvent;

public interface PaymentEventPublisher {
    void publish(PaymentDomainEvent event);
}
