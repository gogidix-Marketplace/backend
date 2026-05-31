package com.gogidix.management.executive.workflow.application.query;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query for fetching a specific strategy by ID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWorkflowQuery {

    @NotBlank(message = "Workflow ID is required")
    private String workflowId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
