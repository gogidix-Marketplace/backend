package com.gogidix.customersupport.supportanalytics.domain.port;

import com.gogidix.customersupport.supportanalytics.domain.event.*;

public interface DomainEventPublisher {
    void publishAnalyticsReportCreated(AnalyticsReportCreatedEvent event);
    void publishAnalyticsReportUpdated(AnalyticsReportUpdatedEvent event);
    void publishAnalyticsReportDeleted(AnalyticsReportDeletedEvent event);
    void publishChannelPerformanceCreated(ChannelPerformanceCreatedEvent event);
    void publishChannelPerformanceUpdated(ChannelPerformanceUpdatedEvent event);
    void publishChannelPerformanceDeleted(ChannelPerformanceDeletedEvent event);
    void publishTicketTrendCreated(TicketTrendCreatedEvent event);
    void publishTicketTrendUpdated(TicketTrendUpdatedEvent event);
    void publishTicketTrendDeleted(TicketTrendDeletedEvent event);
}
