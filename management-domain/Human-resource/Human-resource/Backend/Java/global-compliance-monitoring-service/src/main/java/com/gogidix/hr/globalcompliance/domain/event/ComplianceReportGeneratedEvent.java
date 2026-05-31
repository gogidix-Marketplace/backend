package com.gogidix.hr.globalcompliance.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceReportGeneratedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;
    private String reportId;
    private String reportNumber;
    private String reportType;
    private String countryCode;
    private Double complianceScore;
    private String preparedBy;
}
