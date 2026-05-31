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
public class OnboardingStageHistoryResponse {

    private UUID id;
    private UUID trackerId;
    private OnboardingTracker.OnboardingStage fromStage;
    private OnboardingTracker.OnboardingStage toStage;
    private String status;
    private String stageDescription;
    private String errorMessage;
    private Long durationMilliseconds;
    private Map<String, Object> metadata;
    private String changedBy;
    private LocalDateTime timestamp;
}
