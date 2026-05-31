package com.gogidix.digitalmarketing.countrymarketingdashboard.domain.port;

import com.gogidix.digitalmarketing.countrymarketingdashboard.domain.event.*;

public interface DomainEventPublisher {
    void publishCampaignCreated(CampaignCreatedEvent event);
    void publishCampaignUpdated(CampaignUpdatedEvent event);
    void publishCampaignDeleted(CampaignDeletedEvent event);
}
