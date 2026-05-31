package com.gogidix.aiservices.researchintelligenceservice.domain.aggregate;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.Publication;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchData;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResearchProjectAggregate Tests")
class ResearchProjectAggregateTest {

    @Test
    @DisplayName("Should create aggregate with project")
    void shouldCreateAggregate() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        assertNotNull(aggregate);
        assertEquals(project, aggregate.getProject());
        assertEquals(0, aggregate.getTotalResearchers());
        assertEquals(0, aggregate.getTotalFindings());
        assertEquals(0, aggregate.getTotalPublications());
        assertEquals(0, aggregate.getTotalDatasets());
    }

    @Test
    @DisplayName("Should throw exception when project is null")
    void shouldThrowWhenProjectIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchProjectAggregate(null)
        );
    }

    @Test
    @DisplayName("Should create aggregate with all components")
    void shouldCreateAggregateWithComponents() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Smith",
                "smith@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Discovery",
                "Important finding",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.95,
                "smith@example.com"
        );

        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "AI Paper",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature",
                "smith@example.com"
        );

        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Dataset",
                "Research data",
                ResearchData.DataType.STRUCTURED,
                "/data/dataset1",
                "smith@example.com"
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(
                project,
                List.of(researcher),
                List.of(finding),
                List.of(publication),
                List.of(data)
        );

        assertEquals(1, aggregate.getTotalResearchers());
        assertEquals(1, aggregate.getTotalFindings());
        assertEquals(1, aggregate.getTotalPublications());
        assertEquals(1, aggregate.getTotalDatasets());
    }

    @Test
    @DisplayName("Should create aggregate with null lists")
    void shouldCreateWithNullLists() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(
                project,
                null,
                null,
                null,
                null
        );

        assertEquals(0, aggregate.getTotalResearchers());
        assertEquals(0, aggregate.getTotalFindings());
        assertEquals(0, aggregate.getTotalPublications());
        assertEquals(0, aggregate.getTotalDatasets());
    }

    @Test
    @DisplayName("Should add researcher to aggregate")
    void shouldAddResearcher() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        Researcher researcher = new Researcher(
                "res-001",
                "Dr. Smith",
                "smith@example.com",
                Researcher.ResearchRole.PRINCIPAL_INVESTIGATOR
        );

        aggregate.addResearcher(researcher);

        assertEquals(1, aggregate.getTotalResearchers());
        assertEquals(1, project.getResearchers().size());
    }

    @Test
    @DisplayName("Should not add null researcher")
    void shouldNotAddNullResearcher() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        aggregate.addResearcher(null);

        assertEquals(0, aggregate.getTotalResearchers());
    }

    @Test
    @DisplayName("Should add finding to aggregate")
    void shouldAddFinding() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Discovery",
                "Description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.9,
                "researcher@example.com"
        );

        aggregate.addFinding(finding);

        assertEquals(1, aggregate.getTotalFindings());
        assertEquals(1, project.getFindings().size());
    }

    @Test
    @DisplayName("Should not add finding with different projectId")
    void shouldNotAddFindingWithDifferentProjectId() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-999",
                "Discovery",
                "Description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.9,
                "researcher@example.com"
        );

        aggregate.addFinding(finding);

        assertEquals(0, aggregate.getTotalFindings());
    }

    @Test
    @DisplayName("Should add publication to aggregate")
    void shouldAddPublication() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        Publication publication = new Publication(
                "pub-001",
                "proj-001",
                "AI Paper",
                "Abstract",
                Publication.PublicationType.JOURNAL_ARTICLE,
                "Nature",
                "author@example.com"
        );

        aggregate.addPublication(publication);

        assertEquals(1, aggregate.getTotalPublications());
        assertEquals(1, project.getPublications().size());
    }

    @Test
    @DisplayName("Should add dataset to aggregate")
    void shouldAddDataset() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Dataset",
                "Description",
                ResearchData.DataType.STRUCTURED,
                "/data",
                "uploader@example.com"
        );

        aggregate.addDataset(data);

        assertEquals(1, aggregate.getTotalDatasets());
    }

    @Test
    @DisplayName("Should not add dataset with different projectId")
    void shouldNotAddDatasetWithDifferentProjectId() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        ResearchData data = new ResearchData(
                "data-001",
                "proj-999",
                "Dataset",
                "Description",
                ResearchData.DataType.STRUCTURED,
                "/data",
                "uploader@example.com"
        );

        aggregate.addDataset(data);

        assertEquals(0, aggregate.getTotalDatasets());
    }

    @Test
    @DisplayName("Should start project through aggregate")
    void shouldStartProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        aggregate.startProject();

        assertEquals(ResearchProject.ProjectStatus.IN_PROGRESS, project.getStatus());
    }

    @Test
    @DisplayName("Should complete project through aggregate")
    void shouldCompleteProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        aggregate.completeProject();

        assertEquals(ResearchProject.ProjectStatus.COMPLETED, project.getStatus());
        assertEquals(100.0, project.getProgressPercentage());
    }

    @Test
    @DisplayName("Should cancel project through aggregate")
    void shouldCancelProject() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        aggregate.cancelProject();

        assertEquals(ResearchProject.ProjectStatus.CANCELLED, project.getStatus());
    }

    @Test
    @DisplayName("Should return immutable lists")
    void shouldReturnImmutableLists() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        assertThrows(UnsupportedOperationException.class, () ->
                aggregate.getResearchers().add(new Researcher("r", "n", "e", Researcher.ResearchRole.INTERN))
        );

        assertThrows(UnsupportedOperationException.class, () ->
                aggregate.getFindings().add(new ResearchFinding("f", "p", "t", "d",
                        ResearchFinding.FindingType.DATA_INSIGHT, 0.5, "r"))
        );

        assertThrows(UnsupportedOperationException.class, () ->
                aggregate.getPublications().add(new Publication("p", "pr", "t", "a",
                        Publication.PublicationType.JOURNAL_ARTICLE, "j", "a"))
        );

        assertThrows(UnsupportedOperationException.class, () ->
                aggregate.getDatasets().add(new ResearchData("d", "p", "n", "d",
                        ResearchData.DataType.STRUCTURED, "/loc", "u"))
        );
    }

    @Test
    @DisplayName("Should check if project is active")
    void shouldCheckIfActive() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        assertTrue(aggregate.isActive());

        project.setStatus(ResearchProject.ProjectStatus.IN_PROGRESS);
        assertTrue(aggregate.isActive());

        project.setStatus(ResearchProject.ProjectStatus.COMPLETED);
        assertFalse(aggregate.isActive());

        project.setStatus(ResearchProject.ProjectStatus.CANCELLED);
        assertFalse(aggregate.isActive());

        project.setStatus(ResearchProject.ProjectStatus.ON_HOLD);
        assertFalse(aggregate.isActive());
    }

    @Test
    @DisplayName("Should verify equals based on project")
    void shouldVerifyEquals() {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        ResearchProjectAggregate aggregate1 = new ResearchProjectAggregate(project);
        ResearchProjectAggregate aggregate2 = new ResearchProjectAggregate(project);

        assertEquals(aggregate1, aggregate2);
        assertEquals(aggregate1.hashCode(), aggregate2.hashCode());
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

        ResearchProjectAggregate aggregate = new ResearchProjectAggregate(project);

        String result = aggregate.toString();

        assertTrue(result.contains("proj-001"));
        assertTrue(result.contains("researchersCount=0"));
        assertTrue(result.contains("findingsCount=0"));
        assertTrue(result.contains("publicationsCount=0"));
        assertTrue(result.contains("datasetsCount=0"));
    }
}
