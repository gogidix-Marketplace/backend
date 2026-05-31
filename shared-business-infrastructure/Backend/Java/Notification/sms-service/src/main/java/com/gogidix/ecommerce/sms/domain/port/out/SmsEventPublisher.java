package com.gogidix.ecommerce.sms.domain.port.out;

import com.gogidix.ecommerce.sms.domain.event.SmsDomainEvent;

public interface SmsEventPublisher {
    void publish(SmsDomainEvent event);
}
