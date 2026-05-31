package com.gogidix.digitalmarketing.brandmanagement.infrastructure.messaging;

import com.gogidix.digitalmarketing.brandmanagement.domain.event.*;
import com.gogidix.digitalmarketing.brandmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishBrandAssetCreated(BrandAssetCreatedEvent event) {
        log.info("BrandAsset created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBrandAssetUpdated(BrandAssetUpdatedEvent event) {
        log.info("BrandAsset updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBrandAssetDeleted(BrandAssetDeletedEvent event) {
        log.info("BrandAsset deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBrandGuidelineCreated(BrandGuidelineCreatedEvent event) {
        log.info("BrandGuideline created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBrandGuidelineUpdated(BrandGuidelineUpdatedEvent event) {
        log.info("BrandGuideline updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBrandGuidelineDeleted(BrandGuidelineDeletedEvent event) {
        log.info("BrandGuideline deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
