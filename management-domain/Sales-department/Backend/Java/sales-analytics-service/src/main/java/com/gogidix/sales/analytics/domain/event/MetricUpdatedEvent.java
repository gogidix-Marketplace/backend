package com.gogidix.sales.analytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricUpdatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String metricId;
    private String metricTypeName;
    private String entityType;
    private String entityId;
    private BigDecimal value;
    private String period;
    private Instant periodStart;
    private Instant periodEnd;
    private Map<String, Object> metadata;
    private Instant occurredAt;
    private String correlationId;

    public static MetricUpdatedEvent create(String metricId, String tenantId, String metricTypeName,
                                             String entityType, String entityId, BigDecimal value,
                                             String period, Instant periodStart, Instant periodEnd,
                                             Map<String, Object> metadata, String eventType) {
        return MetricUpdatedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType(eventType)
                .tenantId(tenantId)
                .metricId(metricId)
                .metricTypeName(metricTypeName)
                .entityType(entityType)
                .entityId(entityId)
                .value(value)
                .period(period)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .metadata(metadata)
                .occurredAt(Instant.now())
                .correlationId(java.util.UUID.randomUUID().toString())
                .build();
    }
}
