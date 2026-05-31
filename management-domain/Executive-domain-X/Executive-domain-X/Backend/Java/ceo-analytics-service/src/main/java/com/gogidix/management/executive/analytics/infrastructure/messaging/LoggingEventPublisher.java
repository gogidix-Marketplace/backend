package com.gogidix.management.executive.analytics.infrastructure.messaging;

import com.gogidix.management.executive.analytics.domain.event.*;
import com.gogidix.management.executive.analytics.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAnalyticsCreated(AnalyticsCreatedEvent event) {
        log.info("Analytics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsUpdated(AnalyticsUpdatedEvent event) {
        log.info("Analytics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsDeleted(AnalyticsDeletedEvent event) {
        log.info("Analytics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsDataCreated(AnalyticsDataCreatedEvent event) {
        log.info("AnalyticsData created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsDataUpdated(AnalyticsDataUpdatedEvent event) {
        log.info("AnalyticsData updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsDataDeleted(AnalyticsDataDeletedEvent event) {
        log.info("AnalyticsData deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataFeedCreated(DataFeedCreatedEvent event) {
        log.info("DataFeed created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataFeedUpdated(DataFeedUpdatedEvent event) {
        log.info("DataFeed updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataFeedDeleted(DataFeedDeletedEvent event) {
        log.info("DataFeed deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExecutiveSummaryCreated(ExecutiveSummaryCreatedEvent event) {
        log.info("ExecutiveSummary created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExecutiveSummaryUpdated(ExecutiveSummaryUpdatedEvent event) {
        log.info("ExecutiveSummary updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExecutiveSummaryDeleted(ExecutiveSummaryDeletedEvent event) {
        log.info("ExecutiveSummary deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishKpiWidgetCreated(KpiWidgetCreatedEvent event) {
        log.info("KpiWidget created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishKpiWidgetUpdated(KpiWidgetUpdatedEvent event) {
        log.info("KpiWidget updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishKpiWidgetDeleted(KpiWidgetDeletedEvent event) {
        log.info("KpiWidget deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceBenchmarkCreated(PerformanceBenchmarkCreatedEvent event) {
        log.info("PerformanceBenchmark created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceBenchmarkUpdated(PerformanceBenchmarkUpdatedEvent event) {
        log.info("PerformanceBenchmark updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceBenchmarkDeleted(PerformanceBenchmarkDeletedEvent event) {
        log.info("PerformanceBenchmark deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
