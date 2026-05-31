package com.gogidix.management.executive.alert.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for deleting (soft delete) a alerts
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAlertCommand {

    @NotBlank(message = "Alert ID is required")
    private String alertId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
