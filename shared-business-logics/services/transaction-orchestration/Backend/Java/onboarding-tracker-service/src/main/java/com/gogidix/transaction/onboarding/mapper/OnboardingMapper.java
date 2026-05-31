package com.gogidix.transaction.onboarding.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingStageHistory;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import com.gogidix.transaction.onboarding.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class OnboardingMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static OnboardingTracker toEntity(OnboardingCreateRequest request) {
        return OnboardingTracker.builder()
            .transactionId(request.getTransactionId())
            .entityType(request.getEntityType())
            .entityId(request.getEntityId())
            .merchantId(request.getMerchantId())
            .initiatedBy(request.getInitiatedBy())
            .priority(request.getPriority() != null ? request.getPriority() : OnboardingTracker.Priority.NORMAL)
            .idempotencyKey(request.getIdempotencyKey())
            .estimatedCompletionHours(request.getEstimatedCompletionHours())
            .assignedTo(request.getAssignedTo())
            .build();
    }

    public static OnboardingTrackerResponse toResponse(OnboardingTracker entity) {
        return OnboardingTrackerResponse.builder()
            .id(entity.getId())
            .transactionId(entity.getTransactionId())
            .entityType(entity.getEntityType())
            .entityId(entity.getEntityId())
            .merchantId(entity.getMerchantId())
            .currentStatus(entity.getCurrentStatus())
            .currentStage(entity.getCurrentStage())
            .previousStage(entity.getPreviousStage())
            .stageDescription(entity.getStageDescription())
            .progressPercentage(entity.getProgressPercentage())
            .totalSteps(entity.getTotalSteps())
            .completedSteps(entity.getCompletedSteps())
            .onboardingData(parseJson(entity.getOnboardingData()))
            .priority(entity.getPriority())
            .estimatedCompletionHours(entity.getEstimatedCompletionHours())
            .actualCompletionHours(entity.getActualCompletionHours())
            .assignedTo(entity.getAssignedTo())
            .errorMessage(entity.getErrorMessage())
            .retryCount(entity.getRetryCount())
            .maxRetries(entity.getMaxRetries())
            .metadata(parseJson(entity.getMetadata()))
            .idempotencyKey(entity.getIdempotencyKey())
            .initiatedBy(entity.getInitiatedBy())
            .initiatedAt(entity.getInitiatedAt())
            .completedAt(entity.getCompletedAt())
            .lastStateChange(entity.getLastStateChange())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static OnboardingStageHistoryResponse toHistoryResponse(OnboardingStageHistory entity) {
        return OnboardingStageHistoryResponse.builder()
            .id(entity.getId())
            .trackerId(entity.getTrackerId())
            .fromStage(entity.getFromStage())
            .toStage(entity.getToStage())
            .status(entity.getStatus())
            .stageDescription(entity.getStageDescription())
            .errorMessage(entity.getErrorMessage())
            .durationMilliseconds(entity.getDurationMilliseconds())
            .metadata(parseJson(entity.getMetadata()))
            .changedBy(entity.getChangedBy())
            .timestamp(entity.getTimestamp())
            .build();
    }

    private static Map<String, Object> parseJson(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}
