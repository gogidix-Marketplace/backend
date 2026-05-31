package com.gogidix.transaction.onboarding.dto;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingSearchRequest {

    private UUID transactionId;
    private OnboardingTracker.EntityType entityType;
    private String entityId;
    private String merchantId;
    private OnboardingTracker.OnboardingStatus status;
    private OnboardingTracker.OnboardingStage stage;
    private String assignedTo;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
