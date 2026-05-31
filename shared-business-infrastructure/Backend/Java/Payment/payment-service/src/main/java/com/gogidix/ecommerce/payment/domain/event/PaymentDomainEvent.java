package com.gogidix.ecommerce.payment.domain.event;

import java.time.Instant;

public interface PaymentDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
