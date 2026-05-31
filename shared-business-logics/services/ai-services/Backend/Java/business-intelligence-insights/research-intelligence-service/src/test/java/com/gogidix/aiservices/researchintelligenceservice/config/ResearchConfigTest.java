package com.gogidix.aiservices.researchintelligenceservice.config;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResearchConfig Tests")
class ResearchConfigTest {

    private final ResearchConfig.ResearchProjectPolicy policy = new ResearchConfig.DefaultResearchProjectPolicy();

    @Test
    @DisplayName("Should allow creating project with valid tenant")
    void shouldAllowCreateWithValidTenant() {
        assertTrue(policy.canCreateProject("tenant-001"));
        assertTrue(policy.canCreateProject("tenant_ABC"));
    }

    @Test
    @DisplayName("Should not allow creating project with null tenant")
    void shouldNotAllowCreateWithNullTenant() {
        assertFalse(policy.canCreateProject(null));
    }

    @Test
    @DisplayName("Should not allow creating project with blank tenant")
    void shouldNotAllowCreateWithBlankTenant() {
        assertFalse(policy.canCreateProject(""));
        assertFalse(policy.canCreateProject("   "));
    }

    @Test
    @DisplayName("Should allow updating initiated project")
    void shouldAllowUpdateInitiated() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertTrue(policy.canUpdateProject(project));
    }

    @Test
    @DisplayName("Should allow updating in-progress project")
    void shouldAllowUpdateInProgress() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        assertTrue(policy.canUpdateProject(project));
    }

    @Test
    @DisplayName("Should not allow updating completed project")
    void shouldNotAllowUpdateCompleted() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.complete();

        assertFalse(policy.canUpdateProject(project));
    }

    @Test
    @DisplayName("Should not allow updating cancelled project")
    void shouldNotAllowUpdateCancelled() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.cancel();

        assertFalse(policy.canUpdateProject(project));
    }

    @Test
    @DisplayName("Should not allow updating archived project")
    void shouldNotAllowUpdateArchived() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.setStatus(ResearchProject.ProjectStatus.ARCHIVED);

        assertFalse(policy.canUpdateProject(project));
    }

    @Test
    @DisplayName("Should not allow updating null project")
    void shouldNotAllowUpdateNull() {
        assertFalse(policy.canUpdateProject(null));
    }

    @Test
    @DisplayName("Should allow deleting initiated project")
    void shouldAllowDeleteInitiated() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertTrue(policy.canDeleteProject(project));
    }

    @Test
    @DisplayName("Should not allow deleting in-progress project")
    void shouldNotAllowDeleteInProgress() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        assertFalse(policy.canDeleteProject(project));
    }

    @Test
    @DisplayName("Should not allow deleting null project")
    void shouldNotAllowDeleteNull() {
        assertFalse(policy.canDeleteProject(null));
    }

    @Test
    @DisplayName("Should allow starting initiated project")
    void shouldAllowStartInitiated() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertTrue(policy.canStartProject(project));
    }

    @Test
    @DisplayName("Should not allow starting in-progress project")
    void shouldNotAllowStartInProgress() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        assertFalse(policy.canStartProject(project));
    }

    @Test
    @DisplayName("Should allow completing in-progress project")
    void shouldAllowCompleteInProgress() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        assertTrue(policy.canCompleteProject(project));
    }

    @Test
    @DisplayName("Should not allow completing initiated project")
    void shouldNotAllowCompleteInitiated() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertFalse(policy.canCompleteProject(project));
    }

    @Test
    @DisplayName("Should allow cancelling initiated project")
    void shouldAllowCancelInitiated() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertTrue(policy.canCancelProject(project));
    }

    @Test
    @DisplayName("Should allow cancelling in-progress project")
    void shouldAllowCancelInProgress() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        assertTrue(policy.canCancelProject(project));
    }

    @Test
    @DisplayName("Should not allow cancelling completed project")
    void shouldNotAllowCancelCompleted() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.complete();

        assertFalse(policy.canCancelProject(project));
    }

    @Test
    @DisplayName("Should not allow cancelling null project")
    void shouldNotAllowCancelNull() {
        assertFalse(policy.canCancelProject(null));
    }

    @Test
    @DisplayName("Should not allow starting null project")
    void shouldNotAllowStartNull() {
        assertFalse(policy.canStartProject(null));
    }

    @Test
    @DisplayName("Should not allow completing null project")
    void shouldNotAllowCompleteNull() {
        assertFalse(policy.canCompleteProject(null));
    }
}
