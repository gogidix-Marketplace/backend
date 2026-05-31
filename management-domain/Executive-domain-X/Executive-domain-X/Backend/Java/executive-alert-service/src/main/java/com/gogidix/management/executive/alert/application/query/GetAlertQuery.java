package com.gogidix.management.executive.alert.application.query;

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
public class GetAlertQuery {

    @NotBlank(message = "Alert ID is required")
    private String alertId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
