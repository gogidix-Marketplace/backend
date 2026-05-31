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
public class OnboardingCompletedEvent {

    private String onboardingId;
    private String tenantId;
    private String customerId;
    private String customerName;
    private String customerType;
    private String templateId;
    private Instant startedAt;
    private Instant completedAt;
    private String completedBy;
    private Integer totalSteps;
    private Integer completedSteps;
    private Long durationMinutes;
    private String eventId;
    private String eventType;
    private Instant timestamp;
}
