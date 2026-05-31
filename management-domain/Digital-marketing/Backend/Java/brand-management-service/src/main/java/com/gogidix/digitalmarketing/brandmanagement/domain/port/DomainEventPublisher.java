package com.gogidix.digitalmarketing.brandmanagement.domain.port;

import com.gogidix.digitalmarketing.brandmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishBrandAssetCreated(BrandAssetCreatedEvent event);
    void publishBrandAssetUpdated(BrandAssetUpdatedEvent event);
    void publishBrandAssetDeleted(BrandAssetDeletedEvent event);
    void publishBrandGuidelineCreated(BrandGuidelineCreatedEvent event);
    void publishBrandGuidelineUpdated(BrandGuidelineUpdatedEvent event);
    void publishBrandGuidelineDeleted(BrandGuidelineDeletedEvent event);
}
