package com.gogidix.management.executive.technology.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for updating an existing technologys
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTechnologyCommand {

    @NotBlank(message = "Technology ID is required")
    private String technologyId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private String name;
    private String description;
    private String layout;
    private TechnologyStatus status;

    public enum TechnologyStatus {
        DRAFT, ACTIVE, ARCHIVED
    }
}
