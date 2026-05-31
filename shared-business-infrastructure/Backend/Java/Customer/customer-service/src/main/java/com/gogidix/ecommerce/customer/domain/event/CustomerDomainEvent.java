package com.gogidix.ecommerce.customer.domain.event;

import java.time.Instant;

public interface CustomerDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
