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
public class ComplianceRequirementCreatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;
    private String correlationId;
    private String requirementId;
    private String requirementCode;
    private String requirementName;
    private String category;
    private String countryCode;
    private String type;
    private String triggeredBy;
}
