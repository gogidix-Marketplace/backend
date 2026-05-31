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
public class NonComplianceIssueCreatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;
    private String correlationId;
    private String issueId;
    private String issueNumber;
    private String requirementId;
    private String checkId;
    private String severity;
    private String countryCode;
    private String identifiedBy;
}
