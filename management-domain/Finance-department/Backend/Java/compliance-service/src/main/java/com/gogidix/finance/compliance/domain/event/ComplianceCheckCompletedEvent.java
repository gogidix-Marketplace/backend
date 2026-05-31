package com.gogidix.finance.compliance.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceCheckCompletedEvent {

    private String checkId;
    private String tenantId;
    private String ruleId;
    private String entityId;
    private String result;
    private String severity;
    private String violationDescription;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
