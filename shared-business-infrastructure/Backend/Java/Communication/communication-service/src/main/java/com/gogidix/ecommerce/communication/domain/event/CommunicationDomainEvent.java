package com.gogidix.ecommerce.communication.domain.event;

import java.time.Instant;

public interface CommunicationDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
