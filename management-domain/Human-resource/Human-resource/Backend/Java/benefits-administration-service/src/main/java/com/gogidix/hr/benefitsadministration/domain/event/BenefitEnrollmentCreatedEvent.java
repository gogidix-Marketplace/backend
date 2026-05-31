package com.gogidix.hr.benefitsadministration.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitEnrollmentCreatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant occurredAt;
    private String correlationId;
    private String enrollmentId;
    private String employeeId;
    private String planId;
    private String coverageLevel;
    private LocalDateTime effectiveDate;
    private BigDecimal premium;
    private LocalDateTime timestamp;
}
