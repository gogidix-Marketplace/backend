package com.gogidix.ecommerce.pricing.domain.event;

public interface PricingDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    java.time.Instant timestamp();
}
