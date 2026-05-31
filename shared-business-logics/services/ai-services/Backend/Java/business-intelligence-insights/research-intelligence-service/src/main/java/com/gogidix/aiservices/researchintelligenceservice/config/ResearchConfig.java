package com.gogidix.aiservices.researchintelligenceservice.config;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResearchConfig {

    @Bean
    public ResearchProjectPolicy researchProjectPolicy() {
        return new DefaultResearchProjectPolicy();
    }

    public interface ResearchProjectPolicy {
        boolean canCreateProject(String tenantId);
        boolean canUpdateProject(ResearchProject project);
        boolean canDeleteProject(ResearchProject project);
        boolean canStartProject(ResearchProject project);
        boolean canCompleteProject(ResearchProject project);
        boolean canCancelProject(ResearchProject project);
    }

    public static class DefaultResearchProjectPolicy implements ResearchProjectPolicy {

        @Override
        public boolean canCreateProject(String tenantId) {
            return tenantId != null && !tenantId.isBlank();
        }

        @Override
        public boolean canUpdateProject(ResearchProject project) {
            return project != null &&
                   project.getStatus() != ResearchProject.ProjectStatus.COMPLETED &&
                   project.getStatus() != ResearchProject.ProjectStatus.CANCELLED &&
                   project.getStatus() != ResearchProject.ProjectStatus.ARCHIVED;
        }

        @Override
        public boolean canDeleteProject(ResearchProject project) {
            return project != null &&
                   project.getStatus() == ResearchProject.ProjectStatus.INITIATED;
        }

        @Override
        public boolean canStartProject(ResearchProject project) {
            return project != null &&
                   project.getStatus() == ResearchProject.ProjectStatus.INITIATED;
        }

        @Override
        public boolean canCompleteProject(ResearchProject project) {
            return project != null &&
                   project.getStatus() == ResearchProject.ProjectStatus.IN_PROGRESS;
        }

        @Override
        public boolean canCancelProject(ResearchProject project) {
            return project != null &&
                   (project.getStatus() == ResearchProject.ProjectStatus.INITIATED ||
                    project.getStatus() == ResearchProject.ProjectStatus.IN_PROGRESS);
        }
    }
}
