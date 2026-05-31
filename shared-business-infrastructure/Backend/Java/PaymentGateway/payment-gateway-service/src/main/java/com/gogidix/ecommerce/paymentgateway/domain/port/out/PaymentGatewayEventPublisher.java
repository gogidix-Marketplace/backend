package com.gogidix.ecommerce.paymentgateway.domain.port.out;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;

public interface PaymentGatewayEventPublisher {

    void publishCreated(PaymentGateway entity);

    void publishUpdated(PaymentGateway entity);

    void publishDeleted(PaymentGateway entity);
}