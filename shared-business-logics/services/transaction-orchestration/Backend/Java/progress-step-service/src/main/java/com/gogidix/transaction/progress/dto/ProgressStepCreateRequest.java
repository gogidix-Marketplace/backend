package com.gogidix.transaction.progress.dto;

import com.gogidix.transaction.progress.domain.entity.ProgressStep;
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
public class ProgressStepCreateRequest {

    @NotNull(message = "Transaction ID is required")
    private UUID transactionId;

    @NotNull(message = "Step order is required")
    private Integer stepOrder;

    @NotBlank(message = "Step name is required")
    private String stepName;

    @NotNull(message = "Step type is required")
    private ProgressStep.StepType stepType;

    private String stepDescription;

    private UUID parentStepId;

    private Boolean canExecuteParallel;

    private String dependsOn;

    private Integer executionTimeoutSeconds;

    private Integer maxRetries;

    private Integer retryDelaySeconds;

    private String compensationAction;

    private Map<String, Object> inputData;

    private Map<String, Object> metadata;
}
