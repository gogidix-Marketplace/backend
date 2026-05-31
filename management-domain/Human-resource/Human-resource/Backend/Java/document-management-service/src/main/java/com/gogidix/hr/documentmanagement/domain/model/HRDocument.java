package com.gogidix.hr.documentmanagement.domain.model;

import com.gogidix.hr.documentmanagement.domain.event.DocumentCreatedEvent;
import com.gogidix.hr.documentmanagement.domain.event.DocumentUpdatedEvent;
import com.gogidix.hr.documentmanagement.shared.base.BaseEntity;
import com.gogidix.hr.documentmanagement.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HR Document Domain Entity
 * Represents HR documents with full business logic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "hr_documents")
public class HRDocument extends BaseEntity {

    @Indexed(unique = true)
    private String documentId;

    @Indexed
    private String tenantId;

    private String documentName;
    private String documentNumber;

    @Indexed
    private DocumentType documentType;

    @Indexed
    private DocumentCategory category;

    @Indexed
    private String employeeId;
    private String employeeName;

    @Indexed
    private String countryCode;

    @Indexed
    private String department;

    private String storagePath;
    private StorageProvider storageProvider;

    private Long fileSize;
    private String mimeType;
    private String fileExtension;

    @Indexed
    private DocumentStatus status;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    @Indexed
    private Boolean isConfidential;

    private List<String> tags;
    private String description;

    private String uploadedBy;
    private String approvedBy;
    private LocalDate approvedDate;

    private Integer version = 1;

    private String parentDocumentId;

    private List<DocumentAccessLogEntry> accessLogs = new ArrayList<>();

    private List<DocumentCreatedEvent> domainEvents = new ArrayList<>();

    /**
     * Creates a new HR document
     */
    public static HRDocument create(String tenantId, String documentName, DocumentType documentType,
                                    DocumentCategory category, String employeeId, String employeeName,
                                    String countryCode, String storagePath, StorageProvider storageProvider,
                                    String uploadedBy) {
        String documentId = generateDocumentId(documentType, category, employeeId);

        HRDocument document = new HRDocument();
        document.setTenantId(tenantId);
        document.setDocumentId(documentId);
        document.setDocumentName(documentName);
        document.setDocumentType(documentType);
        document.setCategory(category);
        document.setEmployeeId(employeeId);
        document.setEmployeeName(employeeName);
        document.setCountryCode(countryCode);
        document.setStoragePath(storagePath);
        document.setStorageProvider(storageProvider);
        document.setUploadedBy(uploadedBy);
        document.setStatus(DocumentStatus.DRAFT);
        document.setIsConfidential(false);
        document.setVersion(1);
        document.setTags(new ArrayList<>());
        document.setAccessLogs(new ArrayList<>());
        document.setDomainEvents(new ArrayList<>());

        document.addDomainEvent(DocumentCreatedEvent.builder()
                .documentId(documentId)
                .tenantId(tenantId)
                .documentType(documentType.name())
                .category(category.name())
                .employeeId(employeeId)
                .timestamp(Instant.now())
                .eventType("DOCUMENT_CREATED")
                .build());

        return document;
    }

    /**
     * Submits document for review
     */
    public void submitForReview() {
        if (this.status != DocumentStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft documents for review");
        }

        validateForSubmission();

        this.status = DocumentStatus.PENDING_REVIEW;

        addDomainEvent(DocumentUpdatedEvent.builder()
                .documentId(this.documentId)
                .tenantId(this.tenantId)
                .status(DocumentStatus.PENDING_REVIEW.name())
                .timestamp(Instant.now())
                .eventType("DOCUMENT_SUBMITTED")
                .build());
    }

    /**
     * Approves the document
     */
    public void approve(String approvedBy) {
        if (this.status != DocumentStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Can only approve documents pending review");
        }

        this.status = DocumentStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvedDate = LocalDate.now();

        addDomainEvent(DocumentUpdatedEvent.builder()
                .documentId(this.documentId)
                .tenantId(this.tenantId)
                .status(DocumentStatus.APPROVED.name())
                .approvedBy(approvedBy)
                .timestamp(Instant.now())
                .eventType("DOCUMENT_APPROVED")
                .build());
    }

    /**
     * Rejects the document
     */
    public void reject(String reason) {
        if (this.status != DocumentStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Can only reject documents pending review");
        }

        this.status = DocumentStatus.DRAFT;
        this.description = (this.description != null ? this.description + "\n" : "") + "Rejection: " + reason;

        addDomainEvent(DocumentUpdatedEvent.builder()
                .documentId(this.documentId)
                .tenantId(this.tenantId)
                .status(DocumentStatus.DRAFT.name())
                .timestamp(Instant.now())
                .eventType("DOCUMENT_REJECTED")
                .build());
    }

    /**
     * Archives the document
     */
    public void archive(String archivedBy) {
        if (this.status == DocumentStatus.DELETED) {
            throw new IllegalStateException("Cannot archive deleted documents");
        }

        this.status = DocumentStatus.ARCHIVED;

        addDomainEvent(DocumentUpdatedEvent.builder()
                .documentId(this.documentId)
                .tenantId(this.tenantId)
                .status(DocumentStatus.ARCHIVED.name())
                .timestamp(Instant.now())
                .eventType("DOCUMENT_ARCHIVED")
                .build());
    }

    /**
     * Marks document as expired
     */
    public void markAsExpired() {
        if (this.expiryDate != null && LocalDate.now().isAfter(this.expiryDate)) {
            this.status = DocumentStatus.EXPIRED;

            addDomainEvent(DocumentUpdatedEvent.builder()
                    .documentId(this.documentId)
                    .tenantId(this.tenantId)
                    .status(DocumentStatus.EXPIRED.name())
                    .timestamp(Instant.now())
                    .eventType("DOCUMENT_EXPIRED")
                    .build());
        }
    }

    /**
     * Soft deletes the document
     */
    public void softDelete(String deletedBy) {
        this.status = DocumentStatus.DELETED;

        addDomainEvent(DocumentUpdatedEvent.builder()
                .documentId(this.documentId)
                .tenantId(this.tenantId)
                .status(DocumentStatus.DELETED.name())
                .timestamp(Instant.now())
                .eventType("DOCUMENT_DELETED")
                .build());
    }

    /**
     * Updates document metadata
     */
    public void updateMetadata(String documentName, String description, List<String> tags,
                               String department, Boolean isConfidential) {
        if (this.status != DocumentStatus.DRAFT) {
            throw new IllegalStateException("Can only update draft documents");
        }

        if (documentName != null && !documentName.isBlank()) {
            this.documentName = documentName;
        }
        if (description != null) {
            this.description = description;
        }
        if (tags != null) {
            this.tags = tags;
        }
        if (department != null) {
            this.department = department;
        }
        if (isConfidential != null) {
            this.isConfidential = isConfidential;
        }

        addDomainEvent(DocumentUpdatedEvent.builder()
                .documentId(this.documentId)
                .tenantId(this.tenantId)
                .timestamp(Instant.now())
                .eventType("DOCUMENT_METADATA_UPDATED")
                .build());
    }

    /**
     * Updates storage information
     */
    public void updateStorageInfo(String storagePath, StorageProvider storageProvider,
                                   Long fileSize, String mimeType) {
        this.storagePath = storagePath;
        this.storageProvider = storageProvider;
        this.fileSize = fileSize;
        this.mimeType = mimeType;

        if (storagePath != null && storagePath.contains(".")) {
            this.fileExtension = storagePath.substring(storagePath.lastIndexOf(".") + 1);
        }
    }

    /**
     * Creates a new version of the document
     */
    public HRDocument createNewVersion(String storagePath, String uploadedBy) {
        HRDocument newVersion = new HRDocument();
        newVersion.setTenantId(this.tenantId);
        newVersion.setDocumentId(generateDocumentId(this.documentType, this.category, this.employeeId));
        newVersion.setDocumentName(this.documentName);
        newVersion.setDocumentType(this.documentType);
        newVersion.setCategory(this.category);
        newVersion.setEmployeeId(this.employeeId);
        newVersion.setEmployeeName(this.employeeName);
        newVersion.setCountryCode(this.countryCode);
        newVersion.setDepartment(this.department);
        newVersion.setStoragePath(storagePath);
        newVersion.setStorageProvider(this.storageProvider);
        newVersion.setStatus(DocumentStatus.DRAFT);
        newVersion.setIssueDate(this.issueDate);
        newVersion.setExpiryDate(this.expiryDate);
        newVersion.setIsConfidential(this.isConfidential);
        newVersion.setTags(new ArrayList<>(this.tags));
        newVersion.setDescription(this.description);
        newVersion.setUploadedBy(uploadedBy);
        newVersion.setVersion(this.version + 1);
        newVersion.setParentDocumentId(this.documentId);
        newVersion.setAccessLogs(new ArrayList<>());
        newVersion.setDomainEvents(new ArrayList<>());

        newVersion.addDomainEvent(DocumentCreatedEvent.builder()
                .documentId(newVersion.documentId)
                .tenantId(newVersion.tenantId)
                .documentType(newVersion.documentType.name())
                .category(newVersion.category.name())
                .employeeId(newVersion.employeeId)
                .parentDocumentId(this.documentId)
                .version(newVersion.version)
                .timestamp(Instant.now())
                .eventType("DOCUMENT_VERSION_CREATED")
                .build());

        return newVersion;
    }

    /**
     * Adds a tag to the document
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the document
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Adds an access log entry
     */
    public void addAccessLog(DocumentAccessLogEntry logEntry) {
        if (this.accessLogs == null) {
            this.accessLogs = new ArrayList<>();
        }
        this.accessLogs.add(logEntry);

        // Keep only last 100 access logs
        if (this.accessLogs.size() > 100) {
            this.accessLogs = this.accessLogs.subList(this.accessLogs.size() - 100, this.accessLogs.size());
        }
    }

    /**
     * Checks if document is expired
     */
    public boolean isExpired() {
        return this.expiryDate != null && LocalDate.now().isAfter(this.expiryDate);
    }

    /**
     * Checks if document is expiring within given days
     */
    public boolean isExpiringWithin(int days) {
        if (this.expiryDate == null) {
            return false;
        }
        LocalDate threshold = LocalDate.now().plusDays(days);
        return this.expiryDate.isBefore(threshold) && !this.expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Gets days until expiry
     */
    public long getDaysUntilExpiry() {
        if (this.expiryDate == null) {
            return Long.MAX_VALUE;
        }
        if (this.expiryDate.isBefore(LocalDate.now())) {
            return 0;
        }
        return LocalDate.now().until(this.expiryDate).getDays();
    }

    /**
     * Validates if document can be accessed by user
     */
    public boolean canBeAccessedBy(String userId, String employeeId, boolean isHRStaff, boolean isAdmin) {
        if (this.status == DocumentStatus.DELETED) {
            return false;
        }

        // Admin can access all documents
        if (isAdmin) {
            return true;
        }

        // HR staff can access non-confidential documents
        if (isHRStaff && !this.isConfidential) {
            return true;
        }

        // Employee can access their own documents
        if (userId != null && userId.equals(this.uploadedBy)) {
            return true;
        }

        // Employee can access documents related to them
        if (employeeId != null && employeeId.equals(this.employeeId)) {
            return true;
        }

        return false;
    }

    /**
     * Searches document by keyword
     */
    public boolean matchesKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        return (this.documentName != null && this.documentName.toLowerCase().contains(lowerKeyword)) ||
               (this.documentNumber != null && this.documentNumber.toLowerCase().contains(lowerKeyword)) ||
               (this.description != null && this.description.toLowerCase().contains(lowerKeyword)) ||
               (this.tags != null && this.tags.stream().anyMatch(tag -> tag.toLowerCase().contains(lowerKeyword))) ||
               (this.employeeName != null && this.employeeName.toLowerCase().contains(lowerKeyword));
    }

    private void validateForSubmission() {
        if (this.documentName == null || this.documentName.isBlank()) {
            throw new ValidationException("documentName", "Document name is required");
        }
        if (this.storagePath == null || this.storagePath.isBlank()) {
            throw new ValidationException("storagePath", "Storage path is required");
        }
        if (this.storageProvider == null) {
            throw new ValidationException("storageProvider", "Storage provider is required");
        }
        if (this.employeeId == null || this.employeeId.isBlank()) {
            throw new ValidationException("employeeId", "Employee ID is required");
        }
    }

    private static String generateDocumentId(DocumentType type, DocumentCategory category, String employeeId) {
        String prefix = type.name().substring(0, 3).toUpperCase() +
                       category.name().substring(0, 3).toUpperCase();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String employeeSuffix = employeeId != null ? employeeId.substring(Math.max(0, employeeId.length() - 4)) : "0000";
        return "DOC-" + prefix + "-" + employeeSuffix + "-" + timestamp.substring(timestamp.length() - 6);
    }

    public void addDomainEvent(DocumentCreatedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void addDomainEvent(DocumentUpdatedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(DocumentCreatedEvent.builder()
                .documentId(event.getDocumentId())
                .tenantId(event.getTenantId())
                .eventType(event.getEventType())
                .timestamp(event.getTimestamp())
                .build());
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Inner class for access log entries
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentAccessLogEntry {
        private String logId;
        private String accessedBy;
        private String accessedByName;
        private AccessAction action;
        private Instant accessTimestamp;
        private String ipAddress;
        private String userAgent;
        private String reason;
    }
}
