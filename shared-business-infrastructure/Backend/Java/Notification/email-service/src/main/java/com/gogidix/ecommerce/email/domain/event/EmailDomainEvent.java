package com.gogidix.ecommerce.email.domain.event;

import java.time.Instant;

public interface EmailDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
