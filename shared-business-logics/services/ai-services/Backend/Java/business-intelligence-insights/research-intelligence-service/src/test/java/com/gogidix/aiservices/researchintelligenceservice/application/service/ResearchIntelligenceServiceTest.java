package com.gogidix.aiservices.researchintelligenceservice.application.service;

import com.gogidix.aiservices.researchintelligenceservice.domain.aggregate.ResearchProjectAggregate;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Publication;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchData;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;
import com.gogidix.aiservices.researchintelligenceservice.domain.repository.ResearchProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResearchIntelligenceService Tests")
class ResearchIntelligenceServiceTest {

    @Mock
    private ResearchProjectRepository repository;

    @InjectMocks
    private ResearchIntelligenceService service;

    @Test
    @DisplayName("Should create project successfully")
    void shouldCreateProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.save(any(ResearchProject.class))).thenReturn(project);

        ResearchProject result = service.createProject(
                "tenant-001",
                "AI Research",
                "Research Description",
                ResearchProject.ResearchDomain.MACHINE_LEARNING,
                "user@example.com"
        );

        assertNotNull(result);
        verify(repository).save(any(ResearchProject.class));
    }

    @Test
    @DisplayName("Should get project by id")
    void shouldGetProjectById() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findById("proj-001")).thenReturn(Optional.of(project));

        Optional<ResearchProject> result = service.getProjectById("proj-001");

        assertTrue(result.isPresent());
        assertEquals("proj-001", result.get().getProjectId());
    }

    @Test
    @DisplayName("Should get project by projectId")
    void shouldGetProjectByProjectId() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findByProjectId("proj-001")).thenReturn(Optional.of(project));

        Optional<ResearchProject> result = service.getProjectByProjectId("proj-001");

        assertTrue(result.isPresent());
        assertEquals("proj-001", result.get().getProjectId());
    }

    @Test
    @DisplayName("Should get projects by tenant")
    void shouldGetProjectsByTenant() {
        ResearchProject project1 = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProject project2 = new ResearchProject(
                "proj-002",
                "tenant-001",
                "Data Science Research",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        when(repository.findByTenantId("tenant-001")).thenReturn(List.of(project1, project2));

        List<ResearchProject> result = service.getProjectsByTenant("tenant-001");

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should get projects by status")
    void shouldGetProjectsByStatus() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.setStatus(ResearchProject.ProjectStatus.IN_PROGRESS);

        when(repository.findByStatus(ResearchProject.ProjectStatus.IN_PROGRESS))
                .thenReturn(List.of(project));

        List<ResearchProject> result = service.getProjectsByStatus(ResearchProject.ProjectStatus.IN_PROGRESS);

        assertEquals(1, result.size());
        assertEquals(ResearchProject.ProjectStatus.IN_PROGRESS, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Should get projects by domain")
    void shouldGetProjectsByDomain() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findByDomain(ResearchProject.ResearchDomain.MACHINE_LEARNING))
                .thenReturn(List.of(project));

        List<ResearchProject> result = service.getProjectsByDomain(ResearchProject.ResearchDomain.MACHINE_LEARNING);

        assertEquals(1, result.size());
        assertEquals(ResearchProject.ResearchDomain.MACHINE_LEARNING, result.get(0).getDomain());
    }

    @Test
    @DisplayName("Should get active projects")
    void shouldGetActiveProjects() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        when(repository.findActiveProjects()).thenReturn(List.of(project));

        List<ResearchProject> result = service.getActiveProjects();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should search projects by title")
    void shouldSearchProjectsByTitle() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Machine Learning for AI",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findByTitleContaining("Machine")).thenReturn(List.of(project));

        List<ResearchProject> result = service.searchProjectsByTitle("Machine");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Machine"));
    }

    @Test
    @DisplayName("Should update project")
    void shouldUpdateProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Original Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findByProjectId("proj-001")).thenReturn(Optional.of(project));
        when(repository.save(any(ResearchProject.class))).thenReturn(project);

        ResearchProject result = service.updateProject(
                "proj-001",
                "Updated Title",
                "Updated Description",
                ResearchProject.ResearchDomain.DATA_SCIENCE,
                ResearchProject.Priority.HIGH
        );

        assertNotNull(result);
        verify(repository).save(any(ResearchProject.class));
    }

    @Test
    @DisplayName("Should return null when updating non-existent project")
    void shouldReturnNullWhenUpdatingNonExistentProject() {
        when(repository.findByProjectId("non-existent")).thenReturn(Optional.empty());

        ResearchProject result = service.updateProject(
                "non-existent",
                "Title",
                "Description",
                ResearchProject.ResearchDomain.DATA_SCIENCE,
                null
        );

        assertNull(result);
        verify(repository, never()).save(any(ResearchProject.class));
    }

    @Test
    @DisplayName("Should start project")
    void shouldStartProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findByProjectId("proj-001")).thenReturn(Optional.of(project));
        when(repository.save(any(ResearchProject.class))).thenReturn(project);

        ResearchProject result = service.startProject("proj-001");

        assertNotNull(result);
        verify(repository).save(any(ResearchProject.class));
    }

    @Test
    @DisplayName("Should complete project")
    void shouldCompleteProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        when(repository.findByProjectId("proj-001")).thenReturn(Optional.of(project));
        when(repository.save(any(ResearchProject.class))).thenReturn(project);

        ResearchProject result = service.completeProject("proj-001");

        assertNotNull(result);
        verify(repository).save(any(ResearchProject.class));
    }

    @Test
    @DisplayName("Should cancel project")
    void shouldCancelProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
            );

        when(repository.findByProjectId("proj-001")).thenReturn(Optional.of(project));
        when(repository.save(any(ResearchProject.class))).thenReturn(project);

        ResearchProject result = service.cancelProject("proj-001");

        assertNotNull(result);
        verify(repository).save(any(ResearchProject.class));
    }

    @Test
    @DisplayName("Should delete project")
    void shouldDeleteProject() {
        service.deleteProject("proj-001");

        verify(repository).deleteById("proj-001");
    }

    @Test
    @DisplayName("Should create finding")
    void shouldCreateFinding() {
        ResearchFinding finding = service.createFinding(
                "proj-001",
                "Important Discovery",
                "Detailed description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.95,
                "researcher@example.com"
        );

        assertNotNull(finding);
        assertEquals("proj-001", finding.getProjectId());
        assertEquals("Important Discovery", finding.getTitle());
        assertEquals(ResearchFinding.FindingType.NOVEL_DISCOVERY, finding.getType());
        assertEquals(0.95, finding.getSignificanceScore());
    }

    @Test
    @DisplayName("Should create publication")
    void shouldCreatePublication() {
        Publication publication = service.createPublication(
                "proj-001",
                "AI Research Paper",
                "Paper abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature Journal",
                "author@example.com"
        );

        assertNotNull(publication);
        assertEquals("proj-001", publication.getProjectId());
        assertEquals("AI Research Paper", publication.getTitle());
        assertEquals(Publication.PublicationType.JOURNAL_ARTICLE, publication.getType());
        assertEquals("Nature Journal", publication.getJournal());
    }

    @Test
    @DisplayName("Should create dataset")
    void shouldCreateDataset() {
        ResearchData data = service.createDataset(
                "proj-001",
                "Training Dataset",
                "Dataset for ML model",
                ResearchData.DataType.STRUCTURED,
                "/data/datasets/training",
                "uploader@example.com"
        );

        assertNotNull(data);
        assertEquals("proj-001", data.getProjectId());
        assertEquals("Training Dataset", data.getName());
        assertEquals(ResearchData.DataType.STRUCTURED, data.getDataType());
        assertEquals("/data/datasets/training", data.getStorageLocation());
    }

    @Test
    @DisplayName("Should create researcher")
    void shouldCreateResearcher() {
        Researcher researcher = service.createResearcher(
                "Dr. Jane Smith",
                "jane.smith@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR,
                "MIT"
        );

        assertNotNull(researcher);
        assertEquals("Dr. Jane Smith", researcher.getName());
        assertEquals("jane.smith@example.com", researcher.getEmail());
        assertEquals(Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR, researcher.getRole());
        assertEquals("MIT", researcher.getAffiliation());
    }

    @Test
    @DisplayName("Should get project aggregate")
    void shouldGetProjectAggregate() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(repository.findByProjectId("proj-001")).thenReturn(Optional.of(project));

        ResearchProjectAggregate aggregate = service.getProjectAggregate("proj-001");

        assertNotNull(aggregate);
        assertEquals(project, aggregate.getProject());
    }

    @Test
    @DisplayName("Should return null for non-existent project aggregate")
    void shouldReturnNullForNonExistentAggregate() {
        when(repository.findByProjectId("non-existent")).thenReturn(Optional.empty());

        ResearchProjectAggregate aggregate = service.getProjectAggregate("non-existent");

        assertNull(aggregate);
    }

    @Test
    @DisplayName("Should get project count by tenant")
    void shouldGetProjectCountByTenant() {
        when(repository.countByTenantId("tenant-001")).thenReturn(5);

        int count = service.getProjectCountByTenant("tenant-001");

        assertEquals(5, count);
    }

    @Test
    @DisplayName("Should get project count by status")
    void shouldGetProjectCountByStatus() {
        when(repository.countByStatus(ResearchProject.ProjectStatus.IN_PROGRESS)).thenReturn(3);

        int count = service.getProjectCountByStatus(ResearchProject.ProjectStatus.IN_PROGRESS);

        assertEquals(3, count);
    }
}
