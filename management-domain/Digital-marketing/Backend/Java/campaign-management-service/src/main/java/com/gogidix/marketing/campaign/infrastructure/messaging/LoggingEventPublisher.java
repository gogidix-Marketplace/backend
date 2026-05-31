package com.gogidix.marketing.campaign.infrastructure.messaging;

import com.gogidix.marketing.campaign.domain.event.*;
import com.gogidix.marketing.campaign.domain.port.DomainEventPublisher;
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
    @Override
    public void publishCampaignAssetCreated(CampaignAssetCreatedEvent event) {
        log.info("CampaignAsset created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignAssetUpdated(CampaignAssetUpdatedEvent event) {
        log.info("CampaignAsset updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignAssetDeleted(CampaignAssetDeletedEvent event) {
        log.info("CampaignAsset deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignChannelCreated(CampaignChannelCreatedEvent event) {
        log.info("CampaignChannel created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignChannelUpdated(CampaignChannelUpdatedEvent event) {
        log.info("CampaignChannel updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignChannelDeleted(CampaignChannelDeletedEvent event) {
        log.info("CampaignChannel deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignMetricsCreated(CampaignMetricsCreatedEvent event) {
        log.info("CampaignMetrics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignMetricsUpdated(CampaignMetricsUpdatedEvent event) {
        log.info("CampaignMetrics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignMetricsDeleted(CampaignMetricsDeletedEvent event) {
        log.info("CampaignMetrics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignTargetCreated(CampaignTargetCreatedEvent event) {
        log.info("CampaignTarget created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignTargetUpdated(CampaignTargetUpdatedEvent event) {
        log.info("CampaignTarget updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignTargetDeleted(CampaignTargetDeletedEvent event) {
        log.info("CampaignTarget deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
