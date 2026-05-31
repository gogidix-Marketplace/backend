package com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.response;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(
        String projectId,
        String tenantId,
        String title,
        String description,
        ResearchProject.ProjectStatus status,
        ResearchProject.ResearchDomain domain,
        ResearchProject.Priority priority,
        int researchersCount,
        int findingsCount,
        int publicationsCount,
        double progressPercentage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime completedAt,
        String createdBy
) {
    public static ProjectResponse from(ResearchProject project) {
        return new ProjectResponse(
                project.getProjectId(),
                project.getTenantId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                project.getDomain(),
                project.getPriority(),
                project.getResearchers().size(),
                project.getFindings().size(),
                project.getPublications().size(),
                project.getProgressPercentage(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                project.getCompletedAt(),
                project.getCreatedBy()
        );
    }
}
