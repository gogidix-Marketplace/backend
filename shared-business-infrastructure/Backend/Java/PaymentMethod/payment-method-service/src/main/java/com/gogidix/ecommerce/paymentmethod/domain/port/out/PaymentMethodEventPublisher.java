package com.gogidix.ecommerce.paymentmethod.domain.port.out;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;

public interface PaymentMethodEventPublisher {

    void publishCreated(PaymentMethod entity);

    void publishUpdated(PaymentMethod entity);

    void publishDeleted(PaymentMethod entity);
}