package com.gogidix.customersupport.knowledgebase.infrastructure.messaging;

import com.gogidix.customersupport.knowledgebase.domain.event.*;
import com.gogidix.customersupport.knowledgebase.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishArticleCreated(ArticleCreatedEvent event) {
        log.info("Article created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleUpdated(ArticleUpdatedEvent event) {
        log.info("Article updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleDeleted(ArticleDeletedEvent event) {
        log.info("Article deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleCategoryCreated(ArticleCategoryCreatedEvent event) {
        log.info("ArticleCategory created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleCategoryUpdated(ArticleCategoryUpdatedEvent event) {
        log.info("ArticleCategory updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleCategoryDeleted(ArticleCategoryDeletedEvent event) {
        log.info("ArticleCategory deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleTagCreated(ArticleTagCreatedEvent event) {
        log.info("ArticleTag created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleTagUpdated(ArticleTagUpdatedEvent event) {
        log.info("ArticleTag updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishArticleTagDeleted(ArticleTagDeletedEvent event) {
        log.info("ArticleTag deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
