package com.gogidix.ecommerce.paymentgateway.domain.event;

import java.time.Instant;

public interface PaymentGatewayDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
