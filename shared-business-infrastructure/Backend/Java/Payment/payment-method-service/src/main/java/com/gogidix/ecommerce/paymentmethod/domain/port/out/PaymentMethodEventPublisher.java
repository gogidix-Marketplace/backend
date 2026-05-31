package com.gogidix.ecommerce.paymentmethod.domain.port.out;

import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodDomainEvent;

public interface PaymentMethodEventPublisher {
    void publish(PaymentMethodDomainEvent event);
}
