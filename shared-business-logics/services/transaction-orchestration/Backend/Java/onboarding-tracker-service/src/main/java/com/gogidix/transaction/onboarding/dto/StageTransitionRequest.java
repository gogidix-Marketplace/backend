package com.gogidix.transaction.onboarding.dto;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageTransitionRequest {

    @NotNull(message = "Next stage is required")
    private OnboardingTracker.OnboardingStage nextStage;

    private OnboardingTracker.OnboardingStatus status;

    private String description;

    private String errorMessage;

    private Map<String, Object> stageData;
}
