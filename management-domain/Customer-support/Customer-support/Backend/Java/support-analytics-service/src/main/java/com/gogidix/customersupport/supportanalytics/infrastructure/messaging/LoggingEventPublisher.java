package com.gogidix.customersupport.supportanalytics.infrastructure.messaging;

import com.gogidix.customersupport.supportanalytics.domain.event.*;
import com.gogidix.customersupport.supportanalytics.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAnalyticsReportCreated(AnalyticsReportCreatedEvent event) {
        log.info("AnalyticsReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsReportUpdated(AnalyticsReportUpdatedEvent event) {
        log.info("AnalyticsReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsReportDeleted(AnalyticsReportDeletedEvent event) {
        log.info("AnalyticsReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelPerformanceCreated(ChannelPerformanceCreatedEvent event) {
        log.info("ChannelPerformance created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelPerformanceUpdated(ChannelPerformanceUpdatedEvent event) {
        log.info("ChannelPerformance updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelPerformanceDeleted(ChannelPerformanceDeletedEvent event) {
        log.info("ChannelPerformance deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketTrendCreated(TicketTrendCreatedEvent event) {
        log.info("TicketTrend created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketTrendUpdated(TicketTrendUpdatedEvent event) {
        log.info("TicketTrend updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTicketTrendDeleted(TicketTrendDeletedEvent event) {
        log.info("TicketTrend deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
