package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Publication {
    private final String publicationId;
    private final String projectId;
    private final String title;
    private final String abstractText;
    private final PublicationType type;
    private final String journal;
    private final String publisher;
    private final LocalDate publicationDate;
    private final String doi;
    private final String submittedBy;
    private final LocalDateTime submittedAt;
    private final PublicationStatus status;
    private final int pageCount;
    private final String citationKey;

    public Publication(String publicationId, String projectId, String title, String abstractText,
                      PublicationType type, String journal, String submittedBy) {
        this.publicationId = Objects.requireNonNull(publicationId, "publicationId cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.abstractText = abstractText;
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.journal = journal;
        this.publisher = "";
        this.publicationDate = null;
        this.doi = "";
        this.submittedBy = submittedBy;
        this.submittedAt = LocalDateTime.now();
        this.status = PublicationStatus.DRAFT;
        this.pageCount = 0;
        this.citationKey = generateCitationKey(title, submittedBy);
    }

    private String generateCitationKey(String title, String author) {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String titlePart = title.length() > 3 ? title.substring(0, 3).toLowerCase() : title.toLowerCase();
        String authorPart = author != null && author.length() > 2 ? author.substring(0, 2).toLowerCase() : "xx";
        return authorPart + titlePart + year;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public PublicationType getType() {
        return type;
    }

    public String getJournal() {
        return journal;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getDoi() {
        return doi;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public PublicationStatus getStatus() {
        return status;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getCitationKey() {
        return citationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return Objects.equals(publicationId, that.publicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicationId);
    }

    @Override
    public String toString() {
        return "Publication{" +
                "publicationId='" + publicationId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", journal='" + journal + '\'' +
                ", status=" + status +
                ", citationKey='" + citationKey + '\'' +
                '}';
    }

    public enum PublicationType {
        JOURNAL_ARTICLE,
        CONFERENCE_PAPER,
        BOOK_CHAPTER,
        TECHNICAL_REPORT,
        PREPRINT,
        THESIS,
        WHITE_PAPER,
        WORKSHOP_PAPER
    }

    public enum PublicationStatus {
        DRAFT,
        UNDER_REVIEW,
        ACCEPTED,
        REJECTED,
        PUBLISHED,
        WITHDRAWN
    }
}
