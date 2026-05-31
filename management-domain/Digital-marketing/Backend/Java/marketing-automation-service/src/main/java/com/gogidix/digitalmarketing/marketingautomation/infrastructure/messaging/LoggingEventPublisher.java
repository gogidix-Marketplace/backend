package com.gogidix.digitalmarketing.marketingautomation.infrastructure.messaging;

import com.gogidix.digitalmarketing.marketingautomation.domain.event.*;
import com.gogidix.digitalmarketing.marketingautomation.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCampaignCreated(CampaignCreatedEvent event) {
        log.info("Campaign created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignUpdated(CampaignUpdatedEvent event) {
        log.info("Campaign updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignDeleted(CampaignDeletedEvent event) {
        log.info("Campaign deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
