package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import java.time.LocalDateTime;
import java.util.*;

public class ResearchProject {
    private final String projectId;
    private String tenantId;
    private String title;
    private String description;
    private ProjectStatus status;
    private ResearchDomain domain;
    private Priority priority;
    private final List<Researcher> researchers;
    private final List<ResearchFinding> findings;
    private final List<Publication> publications;
    private final Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private String createdBy;
    private double progressPercentage;

    public ResearchProject(String projectId, String tenantId, String title, ResearchDomain domain) {
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId cannot be null");
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.domain = Objects.requireNonNull(domain, "domain cannot be null");
        this.status = ProjectStatus.INITIATED;
        this.priority = Priority.MEDIUM;
        this.researchers = new ArrayList<>();
        this.findings = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.progressPercentage = 0.0;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
        this.updatedAt = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        if (status == ProjectStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
            this.progressPercentage = 100.0;
        }
    }

    public ResearchDomain getDomain() {
        return domain;
    }

    public void setDomain(ResearchDomain domain) {
        this.domain = domain;
        this.updatedAt = LocalDateTime.now();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    public List<Researcher> getResearchers() {
        return Collections.unmodifiableList(researchers);
    }

    public void addResearcher(Researcher researcher) {
        if (researcher != null) {
            this.researchers.add(researcher);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public List<ResearchFinding> getFindings() {
        return Collections.unmodifiableList(findings);
    }

    public void addFinding(ResearchFinding finding) {
        if (finding != null) {
            this.findings.add(finding);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public List<Publication> getPublications() {
        return Collections.unmodifiableList(publications);
    }

    public void addPublication(Publication publication) {
        if (publication != null) {
            this.publications.add(publication);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public void addMetadata(String key, Object value) {
        if (key != null && !key.isBlank()) {
            this.metadata.put(key, value);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = Math.min(100.0, Math.max(0.0, progressPercentage));
        this.updatedAt = LocalDateTime.now();
    }

    public void start() {
        if (this.status == ProjectStatus.INITIATED) {
            this.status = ProjectStatus.IN_PROGRESS;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void complete() {
        this.status = ProjectStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.progressPercentage = 100.0;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != ProjectStatus.COMPLETED) {
            this.status = ProjectStatus.CANCELLED;
            this.updatedAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchProject that = (ResearchProject) o;
        return Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }

    @Override
    public String toString() {
        return "ResearchProject{" +
                "projectId='" + projectId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", domain=" + domain +
                ", priority=" + priority +
                ", researchersCount=" + researchers.size() +
                ", findingsCount=" + findings.size() +
                ", publicationsCount=" + publications.size() +
                ", progress=" + progressPercentage + '%' +
                '}';
    }

    public enum ProjectStatus {
        INITIATED,
        IN_PROGRESS,
        ON_HOLD,
        COMPLETED,
        CANCELLED,
        ARCHIVED
    }

    public enum ResearchDomain {
        DATA_SCIENCE,
        MACHINE_LEARNING,
        NATURAL_LANGUAGE_PROCESSING,
        COMPUTER_VISION,
        ROBOTICS,
        BIOTECHNOLOGY,
        QUANTUM_COMPUTING,
        BLOCKCHAIN,
        IOT,
        CYBERSECURITY,
        GENERAL_RESEARCH
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }
}
