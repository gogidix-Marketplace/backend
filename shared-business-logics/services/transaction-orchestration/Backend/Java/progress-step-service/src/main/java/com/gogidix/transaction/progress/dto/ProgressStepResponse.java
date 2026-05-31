package com.gogidix.transaction.progress.dto;

import com.gogidix.transaction.progress.domain.entity.ProgressStep;
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
public class ProgressStepResponse {

    private UUID id;
    private UUID transactionId;
    private Integer stepOrder;
    private String stepName;
    private ProgressStep.StepType stepType;
    private String stepDescription;
    private ProgressStep.StepStatus status;
    private UUID parentStepId;
    private Boolean canExecuteParallel;
    private String dependsOn;
    private Integer executionTimeoutSeconds;
    private Integer retryCount;
    private Integer maxRetries;
    private Integer retryDelaySeconds;
    private String compensationAction;
    private Map<String, Object> inputData;
    private Map<String, Object> outputData;
    private String errorMessage;
    private Map<String, Object> metadata;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long durationMilliseconds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
