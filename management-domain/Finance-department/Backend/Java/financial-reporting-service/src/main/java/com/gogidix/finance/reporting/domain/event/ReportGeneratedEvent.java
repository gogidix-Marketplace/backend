package com.gogidix.finance.reporting.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportGeneratedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;
    private String reportId;
    private String reportType;
    private String status;

    public static ReportGeneratedEvent create(String reportId, String tenantId, String reportType, String status, String eventType) {
        return ReportGeneratedEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .reportId(reportId)
            .tenantId(tenantId)
            .reportType(reportType)
            .status(status)
            .eventType(eventType)
            .timestamp(Instant.now())
            .build();
    }
}
