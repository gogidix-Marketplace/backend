package com.gogidix.management.executive.workflow.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing workflows
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkflowCommand {

    @NotBlank(message = "Workflow ID is required")
    private String workflowId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String name;
    private String description;
    private String layout;
    private WorkflowStatus status;

    public enum WorkflowStatus {
        DRAFT, ACTIVE, ARCHIVED
    }
}
