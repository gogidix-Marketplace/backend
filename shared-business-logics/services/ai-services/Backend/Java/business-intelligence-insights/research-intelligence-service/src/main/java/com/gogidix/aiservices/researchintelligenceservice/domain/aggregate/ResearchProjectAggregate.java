package com.gogidix.aiservices.researchintelligenceservice.domain.aggregate;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.Publication;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchData;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchProjectAggregate {
    private final ResearchProject project;
    private final List<Researcher> researchers;
    private final List<ResearchFinding> findings;
    private final List<Publication> publications;
    private final List<ResearchData> datasets;

    public ResearchProjectAggregate(ResearchProject project) {
        this.project = Objects.requireNonNull(project, "project cannot be null");
        this.researchers = new ArrayList<>();
        this.findings = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.datasets = new ArrayList<>();
    }

    public ResearchProjectAggregate(ResearchProject project, List<Researcher> researchers,
                                   List<ResearchFinding> findings, List<Publication> publications,
                                   List<ResearchData> datasets) {
        this.project = Objects.requireNonNull(project, "project cannot be null");
        this.researchers = researchers != null ? new ArrayList<>(researchers) : new ArrayList<>();
        this.findings = findings != null ? new ArrayList<>(findings) : new ArrayList<>();
        this.publications = publications != null ? new ArrayList<>(publications) : new ArrayList<>();
        this.datasets = datasets != null ? new ArrayList<>(datasets) : new ArrayList<>();
    }

    public ResearchProject getProject() {
        return project;
    }

    public List<Researcher> getResearchers() {
        return List.copyOf(researchers);
    }

    public List<ResearchFinding> getFindings() {
        return List.copyOf(findings);
    }

    public List<Publication> getPublications() {
        return List.copyOf(publications);
    }

    public List<ResearchData> getDatasets() {
        return List.copyOf(datasets);
    }

    public void addResearcher(Researcher researcher) {
        if (researcher != null) {
            this.researchers.add(researcher);
            this.project.addResearcher(researcher);
        }
    }

    public void addFinding(ResearchFinding finding) {
        if (finding != null && finding.getProjectId().equals(project.getProjectId())) {
            this.findings.add(finding);
            this.project.addFinding(finding);
        }
    }

    public void addPublication(Publication publication) {
        if (publication != null && publication.getProjectId().equals(project.getProjectId())) {
            this.publications.add(publication);
            this.project.addPublication(publication);
        }
    }

    public void addDataset(ResearchData dataset) {
        if (dataset != null && dataset.getProjectId().equals(project.getProjectId())) {
            this.datasets.add(dataset);
        }
    }

    public void startProject() {
        this.project.start();
    }

    public void completeProject() {
        this.project.complete();
    }

    public void cancelProject() {
        this.project.cancel();
    }

    public int getTotalResearchers() {
        return researchers.size();
    }

    public int getTotalFindings() {
        return findings.size();
    }

    public int getTotalPublications() {
        return publications.size();
    }

    public int getTotalDatasets() {
        return datasets.size();
    }

    public boolean isActive() {
        return project.getStatus() == ResearchProject.ProjectStatus.IN_PROGRESS ||
               project.getStatus() == ResearchProject.ProjectStatus.INITIATED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchProjectAggregate that = (ResearchProjectAggregate) o;
        return Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project);
    }

    @Override
    public String toString() {
        return "ResearchProjectAggregate{" +
                "project=" + project +
                ", researchersCount=" + researchers.size() +
                ", findingsCount=" + findings.size() +
                ", publicationsCount=" + publications.size() +
                ", datasetsCount=" + datasets.size() +
                '}';
    }
}
