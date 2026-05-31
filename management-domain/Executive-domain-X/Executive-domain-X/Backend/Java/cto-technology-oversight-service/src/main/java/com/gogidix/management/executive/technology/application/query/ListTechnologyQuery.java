package com.gogidix.management.executive.technology.application.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query for listing technology data by tenant
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListTechnologyQuery {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String ownerId;

    private TechnologyStatus status;

    @Min(value = 0, message = "Page must be non-negative")
    private int page = 0;

    @Min(value = 1, message = "Size must be positive")
    private int size = 20;

    public enum TechnologyStatus {
        DRAFT, ACTIVE, ARCHIVED, ALL
    }
}
