package com.gogidix.management.executive.technology.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command for deleting (soft delete) a technologys
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTechnologyCommand {

    @NotBlank(message = "Technology ID is required")
    private String technologyId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}
