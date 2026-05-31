package com.gogidix.ecommerce.email.infrastructure.messaging.event;

import com.gogidix.ecommerce.email.domain.event.EmailCreatedEvent;
import com.gogidix.ecommerce.email.domain.event.EmailUpdatedEvent;
import com.gogidix.ecommerce.email.domain.event.EmailDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailDomainEventHandler {

    @EventListener
    public void handleCreated(EmailCreatedEvent event) {
    }

    @EventListener
    public void handleUpdated(EmailUpdatedEvent event) {
    }

    @EventListener
    public void handleDeleted(EmailDeletedEvent event) {
    }
}