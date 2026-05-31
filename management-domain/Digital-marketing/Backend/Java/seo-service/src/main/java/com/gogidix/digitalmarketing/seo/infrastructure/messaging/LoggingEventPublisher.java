package com.gogidix.digitalmarketing.seo.infrastructure.messaging;

import com.gogidix.digitalmarketing.seo.domain.event.*;
import com.gogidix.digitalmarketing.seo.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishKeywordCreated(KeywordCreatedEvent event) {
        log.info("Keyword created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishKeywordUpdated(KeywordUpdatedEvent event) {
        log.info("Keyword updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishKeywordDeleted(KeywordDeletedEvent event) {
        log.info("Keyword deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
