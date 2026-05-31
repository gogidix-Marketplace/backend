package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.port;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.event.*;

public interface DomainEventPublisher {
    void publishDashboardConfigCreated(DashboardConfigCreatedEvent event);
    void publishDashboardConfigUpdated(DashboardConfigUpdatedEvent event);
    void publishDashboardConfigDeleted(DashboardConfigDeletedEvent event);
    void publishRegionalDashboardCreated(RegionalDashboardCreatedEvent event);
    void publishRegionalDashboardUpdated(RegionalDashboardUpdatedEvent event);
    void publishRegionalDashboardDeleted(RegionalDashboardDeletedEvent event);
    void publishWidgetCreated(WidgetCreatedEvent event);
    void publishWidgetUpdated(WidgetUpdatedEvent event);
    void publishWidgetDeleted(WidgetDeletedEvent event);
}
