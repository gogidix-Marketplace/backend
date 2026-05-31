package com.gogidix.ecommerce.search.infrastructure.messaging.event;

import com.gogidix.ecommerce.search.domain.event.SearchCreatedEvent;
import com.gogidix.ecommerce.search.domain.event.SearchUpdatedEvent;
import com.gogidix.ecommerce.search.domain.event.SearchDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SearchDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SearchDomainEventHandler.class);

    @EventListener
    public void handleCreated(SearchCreatedEvent event) {
        log.info("Search created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(SearchUpdatedEvent event) {
        log.info("Search updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(SearchDeletedEvent event) {
        log.info("Search deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
