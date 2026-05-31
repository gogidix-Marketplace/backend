package com.gogidix.management.executive.infrastructure.messaging;

import com.gogidix.management.executive.domain.event.*;
import com.gogidix.management.executive.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAuditCreated(AuditCreatedEvent event) {
        log.info("Audit created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAuditUpdated(AuditUpdatedEvent event) {
        log.info("Audit updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAuditDeleted(AuditDeletedEvent event) {
        log.info("Audit deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
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
