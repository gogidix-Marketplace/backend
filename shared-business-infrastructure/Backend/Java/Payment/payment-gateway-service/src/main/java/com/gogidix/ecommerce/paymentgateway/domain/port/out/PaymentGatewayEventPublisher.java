package com.gogidix.ecommerce.paymentgateway.domain.port.out;

import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayDomainEvent;

public interface PaymentGatewayEventPublisher {
    void publish(PaymentGatewayDomainEvent event);
}
