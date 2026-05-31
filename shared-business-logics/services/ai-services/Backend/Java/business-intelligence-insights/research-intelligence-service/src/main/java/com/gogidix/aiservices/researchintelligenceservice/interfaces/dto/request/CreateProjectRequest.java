package com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.request;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProjectRequest(
        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Domain is required")
        ResearchProject.ResearchDomain domain,

        ResearchProject.Priority priority,

        @NotBlank(message = "Created by is required")
        String createdBy
) {
}
