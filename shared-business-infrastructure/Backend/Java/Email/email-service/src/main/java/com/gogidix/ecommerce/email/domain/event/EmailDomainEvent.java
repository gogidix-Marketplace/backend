package com.gogidix.ecommerce.email.domain.event;

public interface EmailDomainEvent {

    String getEventId();

    String getEventType();

    String getAggregateId();

    String getTenantId();
}