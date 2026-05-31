package com.gogidix.sysadmin.performancemetrics.infrastructure.messaging;

import com.gogidix.sysadmin.performancemetrics.domain.event.*;
import com.gogidix.sysadmin.performancemetrics.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishMetricThresholdCreated(MetricThresholdCreatedEvent event) {
        log.info("MetricThreshold created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricThresholdUpdated(MetricThresholdUpdatedEvent event) {
        log.info("MetricThreshold updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricThresholdDeleted(MetricThresholdDeletedEvent event) {
        log.info("MetricThreshold deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceMetricCreated(PerformanceMetricCreatedEvent event) {
        log.info("PerformanceMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceMetricUpdated(PerformanceMetricUpdatedEvent event) {
        log.info("PerformanceMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceMetricDeleted(PerformanceMetricDeletedEvent event) {
        log.info("PerformanceMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
