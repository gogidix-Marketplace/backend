package com.gogidix.hr.globalworkforceanalytics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Report Generated Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportGeneratedEvent {
    private String reportId;
    private String tenantId;
    private String reportCode;
    private String reportName;
    private String eventType;
    private Instant occurredAt;
}
