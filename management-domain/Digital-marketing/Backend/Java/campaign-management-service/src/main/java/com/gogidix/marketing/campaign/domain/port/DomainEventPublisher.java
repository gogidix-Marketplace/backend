package com.gogidix.marketing.campaign.domain.port;

import com.gogidix.marketing.campaign.domain.event.*;

public interface DomainEventPublisher {
    void publishCampaignCreated(CampaignCreatedEvent event);
    void publishCampaignUpdated(CampaignUpdatedEvent event);
    void publishCampaignDeleted(CampaignDeletedEvent event);
    void publishCampaignAssetCreated(CampaignAssetCreatedEvent event);
    void publishCampaignAssetUpdated(CampaignAssetUpdatedEvent event);
    void publishCampaignAssetDeleted(CampaignAssetDeletedEvent event);
    void publishCampaignChannelCreated(CampaignChannelCreatedEvent event);
    void publishCampaignChannelUpdated(CampaignChannelUpdatedEvent event);
    void publishCampaignChannelDeleted(CampaignChannelDeletedEvent event);
    void publishCampaignMetricsCreated(CampaignMetricsCreatedEvent event);
    void publishCampaignMetricsUpdated(CampaignMetricsUpdatedEvent event);
    void publishCampaignMetricsDeleted(CampaignMetricsDeletedEvent event);
    void publishCampaignTargetCreated(CampaignTargetCreatedEvent event);
    void publishCampaignTargetUpdated(CampaignTargetUpdatedEvent event);
    void publishCampaignTargetDeleted(CampaignTargetDeletedEvent event);
}
