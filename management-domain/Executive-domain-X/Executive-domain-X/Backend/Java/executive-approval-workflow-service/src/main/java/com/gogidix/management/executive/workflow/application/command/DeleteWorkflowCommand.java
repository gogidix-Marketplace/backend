package com.gogidix.management.executive.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for deleting (soft delete) a workflows
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteWorkflowCommand {

    @NotBlank(message = "Workflow ID is required")
    private String workflowId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
