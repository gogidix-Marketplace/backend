package com.gogidix.ecommerce.paymentmethod.domain.event;

import java.time.Instant;

public interface PaymentMethodDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
