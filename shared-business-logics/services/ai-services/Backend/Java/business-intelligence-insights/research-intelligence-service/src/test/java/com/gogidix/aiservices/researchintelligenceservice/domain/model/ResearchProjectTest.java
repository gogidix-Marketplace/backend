package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResearchProject Domain Model Tests")
class ResearchProjectTest {

    @Test
    @DisplayName("Should create research project successfully")
    void shouldCreateResearchProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research Project",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertNotNull(project);
        assertEquals("proj-001", project.getProjectId());
        assertEquals("tenant-001", project.getTenantId());
        assertEquals("AI Research Project", project.getTitle());
        assertEquals(ResearchProject.ResearchDomain.MACHINE_LEARNING, project.getDomain());
        assertEquals(ResearchProject.ProjectStatus.INITIATED, project.getStatus());
        assertEquals(ResearchProject.Priority.MEDIUM, project.getPriority());
        assertEquals(0.0, project.getProgressPercentage());
    }

    @Test
    @DisplayName("Should throw exception when projectId is null")
    void shouldThrowWhenProjectIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchProject(null, "tenant-001", "Title", ResearchProject.ResearchDomain.DATA_SCIENCE)
        );
    }

    @Test
    @DisplayName("Should throw exception when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchProject("proj-001", null, "Title", ResearchProject.ResearchDomain.DATA_SCIENCE)
        );
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowWhenTitleIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchProject("proj-001", "tenant-001", null, ResearchProject.ResearchDomain.DATA_SCIENCE)
        );
    }

    @Test
    @DisplayName("Should throw exception when domain is null")
    void shouldThrowWhenDomainIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchProject("proj-001", "tenant-001", "Title", null)
        );
    }

    @Test
    @DisplayName("Should update project title")
    void shouldUpdateTitle() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Original Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setTitle("Updated Title");

        assertEquals("Updated Title", project.getTitle());
    }

    @Test
    @DisplayName("Should update project description")
    void shouldUpdateDescription() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setDescription("New description");

        assertEquals("New description", project.getDescription());
    }

    @Test
    @DisplayName("Should update project status")
    void shouldUpdateStatus() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setStatus(ResearchProject.ProjectStatus.IN_PROGRESS);

        assertEquals(ResearchProject.ProjectStatus.IN_PROGRESS, project.getStatus());
    }

    @Test
    @DisplayName("Should update project domain")
    void shouldUpdateDomain() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setDomain(ResearchProject.ResearchDomain.NATURAL_LANGUAGE_PROCESSING);

        assertEquals(ResearchProject.ResearchDomain.NATURAL_LANGUAGE_PROCESSING, project.getDomain());
    }

    @Test
    @DisplayName("Should update project priority")
    void shouldUpdatePriority() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setPriority(ResearchProject.Priority.HIGH);

        assertEquals(ResearchProject.Priority.HIGH, project.getPriority());
    }

    @Test
    @DisplayName("Should add researcher to project")
    void shouldAddResearcher() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        Researcher researcher = new Researcher(
                "res-001",
                "John Doe",
                "john@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        project.addResearcher(researcher);

        assertEquals(1, project.getResearchers().size());
        assertTrue(project.getResearchers().contains(researcher));
    }

    @Test
    @DisplayName("Should not add null researcher")
    void shouldNotAddNullResearcher() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.addResearcher(null);

        assertEquals(0, project.getResearchers().size());
    }

    @Test
    @DisplayName("Should add finding to project")
    void shouldAddFinding() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Important Finding",
                "Description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.95,
                "researcher@example.com"
        );

        project.addFinding(finding);

        assertEquals(1, project.getFindings().size());
        assertTrue(project.getFindings().contains(finding));
    }

    @Test
    @DisplayName("Should add publication to project")
    void shouldAddPublication() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "Research Paper",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature Journal",
                "author@example.com"
        );

        project.addPublication(publication);

        assertEquals(1, project.getPublications().size());
        assertTrue(project.getPublications().contains(publication));
    }

    @Test
    @DisplayName("Should add metadata to project")
    void shouldAddMetadata() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.addMetadata("fundingSource", "NSF");
        project.addMetadata("budget", "100000");

        assertEquals(2, project.getMetadata().size());
        assertEquals("NSF", project.getMetadata().get("fundingSource"));
        assertEquals("100000", project.getMetadata().get("budget"));
    }

    @Test
    @DisplayName("Should not add metadata with blank key")
    void shouldNotAddBlankMetadataKey() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.addMetadata("", "value");

        assertEquals(0, project.getMetadata().size());
    }

    @Test
    @DisplayName("Should not add metadata with null key")
    void shouldNotAddNullMetadataKey() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.addMetadata(null, "value");

        assertEquals(0, project.getMetadata().size());
    }

    @Test
    @DisplayName("Should update progress percentage")
    void shouldUpdateProgressPercentage() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setProgressPercentage(50.0);

        assertEquals(50.0, project.getProgressPercentage());
    }

    @Test
    @DisplayName("Should clamp progress percentage to maximum 100")
    void shouldClampProgressToMax() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setProgressPercentage(150.0);

        assertEquals(100.0, project.getProgressPercentage());
    }

    @Test
    @DisplayName("Should clamp progress percentage to minimum 0")
    void shouldClampProgressToMin() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.setProgressPercentage(-10.0);

        assertEquals(0.0, project.getProgressPercentage());
    }

    @Test
    @DisplayName("Should start project")
    void shouldStartProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.start();

        assertEquals(ResearchProject.ProjectStatus.IN_PROGRESS, project.getStatus());
    }

    @Test
    @DisplayName("Should not start already started project")
    void shouldNotStartStartedProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.start();
        project.start();

        assertEquals(ResearchProject.ProjectStatus.IN_PROGRESS, project.getStatus());
    }

    @Test
    @DisplayName("Should complete project")
    void shouldCompleteProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.complete();

        assertEquals(ResearchProject.ProjectStatus.COMPLETED, project.getStatus());
        assertEquals(100.0, project.getProgressPercentage());
        assertNotNull(project.getCompletedAt());
    }

    @Test
    @DisplayName("Should cancel project")
    void shouldCancelProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.cancel();

        assertEquals(ResearchProject.ProjectStatus.CANCELLED, project.getStatus());
    }

    @Test
    @DisplayName("Should not cancel completed project")
    void shouldNotCancelCompletedProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.complete();
        project.cancel();

        assertEquals(ResearchProject.ProjectStatus.COMPLETED, project.getStatus());
    }

    @Test
    @DisplayName("Should return immutable researchers list")
    void shouldReturnImmutableResearchers() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        List<Researcher> researchers = project.getResearchers();

        assertThrows(UnsupportedOperationException.class, () -> researchers.add(new Researcher(
                "res-002", "Jane", "jane@example.com", Researcher.ResearchRole.RESEARCH_ASSOCIATE
        )));
    }

    @Test
    @DisplayName("Should return immutable findings list")
    void shouldReturnImmutableFindings() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        List<ResearchFinding> findings = project.getFindings();

        assertThrows(UnsupportedOperationException.class, () -> findings.add(new ResearchFinding(
                "f-002", "p-001", "F", "D", ResearchFinding.FindingType.DATA_INSIGHT, 0.5, "r"
        )));
    }

    @Test
    @DisplayName("Should return immutable publications list")
    void shouldReturnImmutablePublications() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        List<Publication> publications = project.getPublications();

        assertThrows(UnsupportedOperationException.class, () -> publications.add(new Publication(
                "p-002", "proj-001", "T", "A", Publication.PublicationType.JOURNAL_ARTICLE, "J", "a"
        )));
    }

    @Test
    @DisplayName("Should return immutable metadata map")
    void shouldReturnImmutableMetadata() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        project.addMetadata("key", "value");
        var metadata = project.getMetadata();

        assertThrows(UnsupportedOperationException.class, () -> metadata.put("newKey", "newValue"));
    }

    @Test
    @DisplayName("Should verify equals based on projectId")
    void shouldVerifyEquals() {
        ResearchProject project1 = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title 1",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        ResearchProject project2 = new ResearchProject(
                "proj-001",
                "tenant-002",
                "Title 2",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        assertEquals(project1, project2);
        assertEquals(project1.hashCode(), project2.hashCode());
    }

    @Test
    @DisplayName("Should not equal different projects")
    void shouldNotEqualDifferentProjects() {
        ResearchProject project1 = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        ResearchProject project2 = new ResearchProject(
                "proj-002",
                "tenant-001",
                "Title",
                ResearchProject.ResearchDomain.DATA_SCIENCE
        );

        assertNotEquals(project1, project2);
    }

    @Test
    @DisplayName("Should verify toString contains key fields")
    void shouldVerifyToString() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        String result = project.toString();

        assertTrue(result.contains("proj-001"));
        assertTrue(result.contains("tenant-001"));
        assertTrue(result.contains("AI Research"));
        assertTrue(result.contains("MACHINE_LEARNING"));
    }
}
