package com.gogidix.management.executive.alert.application.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query for listing alerts by tenant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListAlertQuery {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String ownerId;

    private AlertStatus status;

    @Min(value = 0, message = "Page must be non-negative")
    private int page = 0;

    @Min(value = 1, message = "Size must be positive")
    private int size = 20;

    public enum AlertStatus {
        DRAFT, ACTIVE, ARCHIVED, ALL
    }
}
