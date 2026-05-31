package com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.request;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;

public record UpdateProjectRequest(
        String title,
        String description,
        ResearchProject.ResearchDomain domain,
        ResearchProject.Priority priority
) {
}
