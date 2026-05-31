package com.gogidix.sysadmin.performancemetrics.domain.port;

import com.gogidix.sysadmin.performancemetrics.domain.event.*;

public interface DomainEventPublisher {
    void publishMetricThresholdCreated(MetricThresholdCreatedEvent event);
    void publishMetricThresholdUpdated(MetricThresholdUpdatedEvent event);
    void publishMetricThresholdDeleted(MetricThresholdDeletedEvent event);
    void publishPerformanceMetricCreated(PerformanceMetricCreatedEvent event);
    void publishPerformanceMetricUpdated(PerformanceMetricUpdatedEvent event);
    void publishPerformanceMetricDeleted(PerformanceMetricDeletedEvent event);
}
