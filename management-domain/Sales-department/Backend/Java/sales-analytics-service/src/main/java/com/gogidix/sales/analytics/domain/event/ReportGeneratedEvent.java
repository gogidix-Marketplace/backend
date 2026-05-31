package com.gogidix.sales.analytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportGeneratedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private String reportId;
    private String reportTypeName;
    private String generatedBy;
    private Instant startDate;
    private Instant endDate;
    private Map<String, Object> metrics;
    private Instant occurredAt;
    private String correlationId;

    public static ReportGeneratedEvent create(String reportId, String tenantId, String reportTypeName,
                                               String generatedBy, Instant startDate, Instant endDate,
                                               Map<String, Object> metrics, String eventType) {
        return ReportGeneratedEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType(eventType)
                .tenantId(tenantId)
                .reportId(reportId)
                .reportTypeName(reportTypeName)
                .generatedBy(generatedBy)
                .startDate(startDate)
                .endDate(endDate)
                .metrics(metrics)
                .occurredAt(Instant.now())
                .correlationId(java.util.UUID.randomUUID().toString())
                .build();
    }
}
