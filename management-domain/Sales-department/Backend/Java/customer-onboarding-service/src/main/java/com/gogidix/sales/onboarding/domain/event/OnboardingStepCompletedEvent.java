package com.gogidix.sales.onboarding.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStepCompletedEvent {

    private String onboardingId;
    private String tenantId;
    private String customerId;
    private String stepId;
    private String stepName;
    private Integer stepOrder;
    private String stepType;
    private String completedBy;
    private String notes;
    private Instant startedAt;
    private Instant completedAt;
    private Long durationMinutes;
    private String eventId;
    private String eventType;
    private Instant timestamp;
}
