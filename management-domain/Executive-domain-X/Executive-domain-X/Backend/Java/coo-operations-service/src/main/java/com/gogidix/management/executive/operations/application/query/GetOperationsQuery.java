package com.gogidix.management.executive.operations.application.query;

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
public class GetOperationsQuery {

    @NotBlank(message = "Operations ID is required")
    private String operationsId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
