package com.gogidix.digitalmarketing.globalmarketingdashboard.domain.port;

import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.event.*;

public interface DomainEventPublisher {
    void publishMarketingDashboardCreated(MarketingDashboardCreatedEvent event);
    void publishMarketingDashboardUpdated(MarketingDashboardUpdatedEvent event);
    void publishMarketingDashboardDeleted(MarketingDashboardDeletedEvent event);
}
