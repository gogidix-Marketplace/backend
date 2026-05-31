package com.gogidix.aiservices.aiorchestrationservice.application.dto;

import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateWorkflowRequestDto(
    @NotBlank String name,
    String description,
    @Valid List<WorkflowStep> steps,
    List<String> triggers,
    @Positive Integer maxExecutionTime
) {
    public CreateWorkflowRequestDto {
        if (triggers == null) triggers = List.of();
    }
}
