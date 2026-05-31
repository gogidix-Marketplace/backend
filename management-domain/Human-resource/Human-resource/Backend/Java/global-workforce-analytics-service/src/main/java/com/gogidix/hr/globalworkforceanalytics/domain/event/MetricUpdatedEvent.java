package com.gogidix.hr.globalworkforceanalytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Metric Updated Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricUpdatedEvent {
    private String metricId;
    private String tenantId;
    private String metricCode;
    private BigDecimal previousValue;
    private BigDecimal newValue;
    private String eventType;
    private Instant occurredAt;
}
