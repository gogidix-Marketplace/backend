package com.gogidix.digitalmarketing.marketingautomation.domain.port;

import com.gogidix.digitalmarketing.marketingautomation.domain.event.*;

public interface DomainEventPublisher {
    void publishCampaignCreated(CampaignCreatedEvent event);
    void publishCampaignUpdated(CampaignUpdatedEvent event);
    void publishCampaignDeleted(CampaignDeletedEvent event);
}
