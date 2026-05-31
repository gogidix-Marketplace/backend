package com.gogidix.transaction.progress.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.progress.domain.entity.ProgressStep;
import com.gogidix.transaction.progress.dto.ProgressStepCreateRequest;
import com.gogidix.transaction.progress.dto.ProgressStepResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ProgressStepMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProgressStep toEntity(ProgressStepCreateRequest request) {
        return ProgressStep.builder()
            .transactionId(request.getTransactionId())
            .stepOrder(request.getStepOrder())
            .stepName(request.getStepName())
            .stepType(request.getStepType())
            .stepDescription(request.getStepDescription())
            .parentStepId(request.getParentStepId())
            .canExecuteParallel(request.getCanExecuteParallel() != null ? request.getCanExecuteParallel() : false)
            .dependsOn(request.getDependsOn())
            .executionTimeoutSeconds(request.getExecutionTimeoutSeconds())
            .maxRetries(request.getMaxRetries() != null ? request.getMaxRetries() : 3)
            .retryDelaySeconds(request.getRetryDelaySeconds() != null ? request.getRetryDelaySeconds() : 30)
            .compensationAction(request.getCompensationAction())
            .build();
    }

    public static ProgressStepResponse toResponse(ProgressStep entity) {
        return ProgressStepResponse.builder()
            .id(entity.getId())
            .transactionId(entity.getTransactionId())
            .stepOrder(entity.getStepOrder())
            .stepName(entity.getStepName())
            .stepType(entity.getStepType())
            .stepDescription(entity.getStepDescription())
            .status(entity.getStatus())
            .parentStepId(entity.getParentStepId())
            .canExecuteParallel(entity.getCanExecuteParallel())
            .dependsOn(entity.getDependsOn())
            .executionTimeoutSeconds(entity.getExecutionTimeoutSeconds())
            .retryCount(entity.getRetryCount())
            .maxRetries(entity.getMaxRetries())
            .retryDelaySeconds(entity.getRetryDelaySeconds())
            .compensationAction(entity.getCompensationAction())
            .inputData(parseJson(entity.getInputData()))
            .outputData(parseJson(entity.getOutputData()))
            .errorMessage(entity.getErrorMessage())
            .metadata(parseJson(entity.getMetadata()))
            .startedAt(entity.getStartedAt())
            .completedAt(entity.getCompletedAt())
            .durationMilliseconds(entity.getDurationMilliseconds())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .version(entity.getVersion())
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
