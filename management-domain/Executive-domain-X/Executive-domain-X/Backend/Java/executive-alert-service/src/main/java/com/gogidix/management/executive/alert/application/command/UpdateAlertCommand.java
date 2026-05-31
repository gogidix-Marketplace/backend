package com.gogidix.management.executive.alert.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing alerts
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAlertCommand {

    @NotBlank(message = "Alert ID is required")
    private String alertId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String name;
    private String description;
    private String layout;
    private AlertStatus status;

    public enum AlertStatus {
        DRAFT, ACTIVE, ARCHIVED
    }
}
