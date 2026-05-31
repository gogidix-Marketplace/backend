package com.gogidix.digitalmarketing.contentmanagement.infrastructure.messaging;

import com.gogidix.digitalmarketing.contentmanagement.domain.event.*;
import com.gogidix.digitalmarketing.contentmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishContentPieceCreated(ContentPieceCreatedEvent event) {
        log.info("ContentPiece created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentPieceUpdated(ContentPieceUpdatedEvent event) {
        log.info("ContentPiece updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentPieceDeleted(ContentPieceDeletedEvent event) {
        log.info("ContentPiece deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
