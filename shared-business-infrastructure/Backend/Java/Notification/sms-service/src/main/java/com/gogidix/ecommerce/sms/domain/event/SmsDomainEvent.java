package com.gogidix.ecommerce.sms.domain.event;

import java.time.Instant;

public interface SmsDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
