package com.gogidix.management.executive.operations.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for deleting (soft delete) a operationss
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteOperationsCommand {

    @NotBlank(message = "Operations ID is required")
    private String operationsId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
