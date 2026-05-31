package com.gogidix.ecommerce.storecredit.domain.event;

import java.time.Instant;

public interface StoreCreditDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
