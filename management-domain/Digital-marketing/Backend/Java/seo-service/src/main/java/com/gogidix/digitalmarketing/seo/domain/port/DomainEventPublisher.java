package com.gogidix.digitalmarketing.seo.domain.port;

import com.gogidix.digitalmarketing.seo.domain.event.*;

public interface DomainEventPublisher {
    void publishKeywordCreated(KeywordCreatedEvent event);
    void publishKeywordUpdated(KeywordUpdatedEvent event);
    void publishKeywordDeleted(KeywordDeletedEvent event);
}
