package com.gogidix.transaction.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepExecutionSummary {

    private UUID transactionId;
    private Long totalSteps;
    private Long completedSteps;
    private Long failedSteps;
    private Long inProgressSteps;
    private Long pendingSteps;
    private Long skippedSteps;
    private Integer progressPercentage;
    private Boolean isComplete;
}
