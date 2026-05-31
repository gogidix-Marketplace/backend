package com.gogidix.ecommerce.paymentgateway.domain.event;

public interface PaymentGatewayDomainEvent {

    String getEventId();

    String getEventType();

    String getAggregateId();

    String getTenantId();
}