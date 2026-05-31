package com.gogidix.aiservices.researchintelligenceservice.application.service;

import com.gogidix.aiservices.researchintelligenceservice.domain.aggregate.ResearchProjectAggregate;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Publication;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchData;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;
import com.gogidix.aiservices.researchintelligenceservice.domain.repository.ResearchProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResearchIntelligenceService {

    private final ResearchProjectRepository projectRepository;

    public ResearchIntelligenceService(ResearchProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ResearchProject createProject(String tenantId, String title, String description,
                                        ResearchProject.ResearchDomain domain, String createdBy) {
        String projectId = "proj-" + UUID.randomUUID().toString().substring(0, 8);
        ResearchProject project = new ResearchProject(projectId, tenantId, title, domain);
        project.setDescription(description);
        project.setCreatedBy(createdBy);
        return projectRepository.save(project);
    }

    public Optional<ResearchProject> getProjectById(String id) {
        return projectRepository.findById(id);
    }

    public Optional<ResearchProject> getProjectByProjectId(String projectId) {
        return projectRepository.findByProjectId(projectId);
    }

    public List<ResearchProject> getProjectsByTenant(String tenantId) {
        return projectRepository.findByTenantId(tenantId);
    }

    public List<ResearchProject> getProjectsByStatus(ResearchProject.ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    public List<ResearchProject> getProjectsByDomain(ResearchProject.ResearchDomain domain) {
        return projectRepository.findByDomain(domain);
    }

    public List<ResearchProject> getActiveProjects() {
        return projectRepository.findActiveProjects();
    }

    public List<ResearchProject> searchProjectsByTitle(String title) {
        return projectRepository.findByTitleContaining(title);
    }

    public ResearchProject updateProject(String projectId, String title, String description,
                                        ResearchProject.ResearchDomain domain,
                                        ResearchProject.Priority priority) {
        Optional<ResearchProject> projectOpt = projectRepository.findByProjectId(projectId);
        if (projectOpt.isPresent()) {
            ResearchProject project = projectOpt.get();
            if (title != null) project.setTitle(title);
            if (description != null) project.setDescription(description);
            if (domain != null) project.setDomain(domain);
            if (priority != null) project.setPriority(priority);
            return projectRepository.save(project);
        }
        return null;
    }

    public ResearchProject startProject(String projectId) {
        Optional<ResearchProject> projectOpt = projectRepository.findByProjectId(projectId);
        if (projectOpt.isPresent()) {
            ResearchProject project = projectOpt.get();
            project.start();
            return projectRepository.save(project);
        }
        return null;
    }

    public ResearchProject completeProject(String projectId) {
        Optional<ResearchProject> projectOpt = projectRepository.findByProjectId(projectId);
        if (projectOpt.isPresent()) {
            ResearchProject project = projectOpt.get();
            project.complete();
            return projectRepository.save(project);
        }
        return null;
    }

    public ResearchProject cancelProject(String projectId) {
        Optional<ResearchProject> projectOpt = projectRepository.findByProjectId(projectId);
        if (projectOpt.isPresent()) {
            ResearchProject project = projectOpt.get();
            project.cancel();
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(String id) {
        projectRepository.deleteById(id);
    }

    public ResearchFinding createFinding(String projectId, String title, String description,
                                        ResearchFinding.FindingType type, double significanceScore,
                                        String discoveredBy) {
        String findingId = "find-" + UUID.randomUUID().toString().substring(0, 8);
        ResearchFinding finding = new ResearchFinding(findingId, projectId, title, description,
                type, significanceScore, discoveredBy);
        return finding;
    }

    public Publication createPublication(String projectId, String title, String abstractText,
                                        Publication.PublicationType type, String journal,
                                        String submittedBy) {
        String publicationId = "pub-" + UUID.randomUUID().toString().substring(0, 8);
        Publication publication = new Publication(publicationId, projectId, title, abstractText,
                type, journal, submittedBy);
        return publication;
    }

    public ResearchData createDataset(String projectId, String name, String description,
                                     ResearchData.DataType dataType, String storageLocation,
                                     String uploadedBy) {
        String datasetId = "data-" + UUID.randomUUID().toString().substring(0, 8);
        ResearchData data = new ResearchData(datasetId, projectId, name, description, dataType,
                storageLocation, uploadedBy);
        return data;
    }

    public Researcher createResearcher(String name, String email, Researcher.ResearchRole role,
                                      String affiliation) {
        String researcherId = "res-" + UUID.randomUUID().toString().substring(0, 8);
        return new Researcher(researcherId, name, email, role, affiliation);
    }

    public ResearchProjectAggregate getProjectAggregate(String projectId) {
        Optional<ResearchProject> projectOpt = projectRepository.findByProjectId(projectId);
        if (projectOpt.isPresent()) {
            return new ResearchProjectAggregate(projectOpt.get());
        }
        return null;
    }

    public int getProjectCountByTenant(String tenantId) {
        return projectRepository.countByTenantId(tenantId);
    }

    public int getProjectCountByStatus(ResearchProject.ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }
}
