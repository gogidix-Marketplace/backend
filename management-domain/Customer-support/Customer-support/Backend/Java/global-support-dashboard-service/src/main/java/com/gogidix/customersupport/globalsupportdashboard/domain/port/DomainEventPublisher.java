package com.gogidix.customersupport.globalsupportdashboard.domain.port;

import com.gogidix.customersupport.globalsupportdashboard.domain.event.*;

public interface DomainEventPublisher {
    void publishAgentPerformanceCreated(AgentPerformanceCreatedEvent event);
    void publishAgentPerformanceUpdated(AgentPerformanceUpdatedEvent event);
    void publishAgentPerformanceDeleted(AgentPerformanceDeletedEvent event);
    void publishRegionalMetricsCreated(RegionalMetricsCreatedEvent event);
    void publishRegionalMetricsUpdated(RegionalMetricsUpdatedEvent event);
    void publishRegionalMetricsDeleted(RegionalMetricsDeletedEvent event);
    void publishSupportMetricsCreated(SupportMetricsCreatedEvent event);
    void publishSupportMetricsUpdated(SupportMetricsUpdatedEvent event);
    void publishSupportMetricsDeleted(SupportMetricsDeletedEvent event);
}
