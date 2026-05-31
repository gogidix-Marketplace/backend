package com.gogidix.transaction.onboarding.dto;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingCreateRequest {

    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotNull(message = "Entity type is required")
    private OnboardingTracker.EntityType entityType;

    @NotBlank(message = "Entity ID is required")
    private String entityId;

    private String merchantId;

    private String initiatedBy;

    private OnboardingTracker.Priority priority;

    private String idempotencyKey;

    private Integer estimatedCompletionHours;

    private String assignedTo;

    private Map<String, Object> onboardingData;

    private Map<String, Object> metadata;
}
