package com.gogidix.digitalmarketing.contentmanagement.domain.port;

import com.gogidix.digitalmarketing.contentmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishContentPieceCreated(ContentPieceCreatedEvent event);
    void publishContentPieceUpdated(ContentPieceUpdatedEvent event);
    void publishContentPieceDeleted(ContentPieceDeletedEvent event);
}
