package com.gogidix.management.executive.operations.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOperationsCommand {

    @NotBlank(message = "Operations ID is required")
    private String operationsId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String name;
    private String description;
    private String layout;
    private OperationsStatus status;

    public enum OperationsStatus {
        DRAFT, ACTIVE, ARCHIVED
    }
}
