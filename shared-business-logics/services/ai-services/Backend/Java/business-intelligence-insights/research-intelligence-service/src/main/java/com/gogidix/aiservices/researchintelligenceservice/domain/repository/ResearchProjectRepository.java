package com.gogidix.aiservices.researchintelligenceservice.domain.repository;

import com.gogidix.aiservices.researchintelligenceservice.domain.aggregate.ResearchProjectAggregate;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchData;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResearchProjectRepository {
    ResearchProject save(ResearchProject project);
    ResearchProjectAggregate saveAggregate(ResearchProjectAggregate aggregate);
    Optional<ResearchProject> findById(String id);
    Optional<ResearchProject> findByProjectId(String projectId);
    List<ResearchProject> findByTenantId(String tenantId);
    List<ResearchProject> findByStatus(ResearchProject.ProjectStatus status);
    List<ResearchProject> findByDomain(ResearchProject.ResearchDomain domain);
    List<ResearchProject> findByCreatedBy(String createdBy);
    List<ResearchProject> findActiveProjects();
    List<ResearchProject> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<ResearchProject> findByTitleContaining(String title);
    List<ResearchProject> findAll();
    void deleteById(String id);
    boolean existsByProjectId(String projectId);
    int countByTenantId(String tenantId);
    int countByStatus(ResearchProject.ProjectStatus status);
}
