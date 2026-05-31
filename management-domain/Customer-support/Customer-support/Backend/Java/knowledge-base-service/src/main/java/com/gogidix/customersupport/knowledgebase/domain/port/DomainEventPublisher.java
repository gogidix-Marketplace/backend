package com.gogidix.customersupport.knowledgebase.domain.port;

import com.gogidix.customersupport.knowledgebase.domain.event.*;

public interface DomainEventPublisher {
    void publishArticleCreated(ArticleCreatedEvent event);
    void publishArticleUpdated(ArticleUpdatedEvent event);
    void publishArticleDeleted(ArticleDeletedEvent event);
    void publishArticleCategoryCreated(ArticleCategoryCreatedEvent event);
    void publishArticleCategoryUpdated(ArticleCategoryUpdatedEvent event);
    void publishArticleCategoryDeleted(ArticleCategoryDeletedEvent event);
    void publishArticleTagCreated(ArticleTagCreatedEvent event);
    void publishArticleTagUpdated(ArticleTagUpdatedEvent event);
    void publishArticleTagDeleted(ArticleTagDeletedEvent event);
}
