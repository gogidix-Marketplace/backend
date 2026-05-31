package com.gogidix.ecommerce.email.domain.port.out;

import com.gogidix.ecommerce.email.domain.event.EmailDomainEvent;

public interface EmailEventPublisher {
    void publish(EmailDomainEvent event);
}
