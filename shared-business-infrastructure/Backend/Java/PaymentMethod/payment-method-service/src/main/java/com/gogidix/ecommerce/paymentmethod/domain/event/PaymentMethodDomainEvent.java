package com.gogidix.ecommerce.paymentmethod.domain.event;

public interface PaymentMethodDomainEvent {

    String getEventId();

    String getEventType();

    String getAggregateId();

    String getTenantId();
}