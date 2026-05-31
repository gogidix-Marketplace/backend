package com.gogidix.hr.globalworkforceanalytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Metric Created Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricCreatedEvent {
    private String metricId;
    private String tenantId;
    private String metricCode;
    private String metricName;
    private String eventType;
    private Instant occurredAt;
}
