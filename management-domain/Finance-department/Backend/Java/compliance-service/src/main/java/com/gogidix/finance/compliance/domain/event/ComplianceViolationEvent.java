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
public class ComplianceViolationEvent {

    private String tenantId;
    private String ruleId;
    private String severity;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
