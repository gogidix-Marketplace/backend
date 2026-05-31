package com.gogidix.ecommerce.sms.domain.event;

public interface SmsDomainEvent {

    String getEventId();

    String getEventType();

    String getAggregateId();

    String getTenantId();
}