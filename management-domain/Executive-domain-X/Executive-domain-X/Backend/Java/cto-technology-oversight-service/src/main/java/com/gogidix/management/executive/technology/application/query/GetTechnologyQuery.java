package com.gogidix.management.executive.technology.application.query;

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
public class GetTechnologyQuery {

    @NotBlank(message = "Technology ID is required")
    private String technologyId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
