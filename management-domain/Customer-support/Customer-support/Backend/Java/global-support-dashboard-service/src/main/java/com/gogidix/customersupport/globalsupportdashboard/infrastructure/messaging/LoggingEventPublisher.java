package com.gogidix.customersupport.globalsupportdashboard.infrastructure.messaging;

import com.gogidix.customersupport.globalsupportdashboard.domain.event.*;
import com.gogidix.customersupport.globalsupportdashboard.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAgentPerformanceCreated(AgentPerformanceCreatedEvent event) {
        log.info("AgentPerformance created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAgentPerformanceUpdated(AgentPerformanceUpdatedEvent event) {
        log.info("AgentPerformance updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAgentPerformanceDeleted(AgentPerformanceDeletedEvent event) {
        log.info("AgentPerformance deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalMetricsCreated(RegionalMetricsCreatedEvent event) {
        log.info("RegionalMetrics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalMetricsUpdated(RegionalMetricsUpdatedEvent event) {
        log.info("RegionalMetrics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalMetricsDeleted(RegionalMetricsDeletedEvent event) {
        log.info("RegionalMetrics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSupportMetricsCreated(SupportMetricsCreatedEvent event) {
        log.info("SupportMetrics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSupportMetricsUpdated(SupportMetricsUpdatedEvent event) {
        log.info("SupportMetrics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSupportMetricsDeleted(SupportMetricsDeletedEvent event) {
        log.info("SupportMetrics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
