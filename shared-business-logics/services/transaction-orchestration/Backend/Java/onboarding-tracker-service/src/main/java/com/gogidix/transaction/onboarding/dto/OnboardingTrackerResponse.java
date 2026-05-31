package com.gogidix.transaction.onboarding.dto;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTrackerResponse {

    private UUID id;
    private UUID transactionId;
    private OnboardingTracker.EntityType entityType;
    private String entityId;
    private String merchantId;
    private OnboardingTracker.OnboardingStatus currentStatus;
    private OnboardingTracker.OnboardingStage currentStage;
    private OnboardingTracker.OnboardingStage previousStage;
    private String stageDescription;
    private Integer progressPercentage;
    private Integer totalSteps;
    private Integer completedSteps;
    private Map<String, Object> onboardingData;
    private OnboardingTracker.Priority priority;
    private Integer estimatedCompletionHours;
    private Integer actualCompletionHours;
    private String assignedTo;
    private String errorMessage;
    private Integer retryCount;
    private Integer maxRetries;
    private Map<String, Object> metadata;
    private String idempotencyKey;
    private String initiatedBy;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime lastStateChange;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
