package com.gogidix.sales.onboarding.domain.model;

import com.gogidix.sales.onboarding.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Document Checklist Domain Entity
 * Manages required documents for customer onboarding
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "document_checklists")
public class DocumentChecklist extends BaseEntity {

    private String checklistId;

    private String onboardingId;

    private String tenantId;

    private String customerId;

    @Builder.Default
    private List<DocumentItem> documents = new ArrayList<>();

    private Boolean requireAllDocuments;

    private Integer totalRequired;

    private Integer totalCompleted;

    private Boolean completed;

    private Instant completedAt;

    private String completedBy;

    /**
     * Creates a new document checklist
     */
    public static DocumentChecklist create(String onboardingId, String tenantId,
                                            String customerId, List<String> requiredDocumentTypes) {
        List<DocumentItem> documents = new ArrayList<>();
        if (requiredDocumentTypes != null) {
            for (String docType : requiredDocumentTypes) {
                documents.add(DocumentItem.builder()
                        .itemId(generateItemId())
                        .documentType(docType)
                        .required(true)
                        .status(DocumentStatus.PENDING)
                        .build());
            }
        }

        return DocumentChecklist.builder()
                .checklistId(generateChecklistId())
                .onboardingId(onboardingId)
                .tenantId(tenantId)
                .customerId(customerId)
                .documents(documents)
                .requireAllDocuments(true)
                .totalRequired(documents.size())
                .totalCompleted(0)
                .completed(false)
                .build();
    }

    /**
     * Adds a document to the checklist
     */
    public void addDocument(String documentType, Boolean required, String addedBy) {
        DocumentItem item = DocumentItem.builder()
                .itemId(generateItemId())
                .documentType(documentType)
                .required(required != null ? required : true)
                .status(DocumentStatus.PENDING)
                .addedBy(addedBy)
                .addedAt(Instant.now())
                .build();

        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }
        this.documents.add(item);

        if (item.getRequired()) {
            this.totalRequired++;
        }
    }

    /**
     * Uploads a document
     */
    public void uploadDocument(String itemId, String fileName, String fileUrl,
                                Long fileSizeBytes, String uploadedBy) {
        DocumentItem item = findItemById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Document item not found: " + itemId));

        item.setStatus(DocumentStatus.UPLOADED);
        item.setFileName(fileName);
        item.setFileUrl(fileUrl);
        item.setFileSizeBytes(fileSizeBytes);
        item.setUploadedBy(uploadedBy);
        item.setUploadedAt(Instant.now());

        updateStatus();
    }

    /**
     * Verifies a document
     */
    public void verifyDocument(String itemId, String verifiedBy, Boolean approved, String notes) {
        DocumentItem item = findItemById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Document item not found: " + itemId));

        if (approved) {
            item.setStatus(DocumentStatus.VERIFIED);
        } else {
            item.setStatus(DocumentStatus.REJECTED);
        }

        item.setVerifiedBy(verifiedBy);
        item.setVerifiedAt(Instant.now());
        item.setVerificationNotes(notes);

        updateStatus();
    }

    /**
     * Marks a document as optional/not required
     */
    public void markAsNotRequired(String itemId, String updatedBy) {
        DocumentItem item = findItemById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Document item not found: " + itemId));

        if (item.getRequired()) {
            item.setRequired(false);
            this.totalRequired--;
        }

        updateStatus();
    }

    /**
     * Removes a document from the checklist
     */
    public void removeDocument(String itemId) {
        if (this.documents != null) {
            DocumentItem item = findItemById(itemId).orElse(null);
            if (item != null) {
                if (item.getRequired()) {
                    this.totalRequired--;
                }
                this.documents.remove(item);
                updateStatus();
            }
        }
    }

    /**
     * Updates the overall status
     */
    public void updateStatus() {
        int completed = 0;
        int required = 0;

        for (DocumentItem item : this.documents) {
            if (item.getRequired()) {
                required++;
                if (item.getStatus() == DocumentStatus.VERIFIED) {
                    completed++;
                }
            }
        }

        this.totalRequired = required;
        this.totalCompleted = completed;

        if (this.requireAllDocuments) {
            this.completed = (this.totalCompleted >= this.totalRequired) && this.totalRequired > 0;
        } else {
            this.completed = this.totalCompleted > 0;
        }

        if (this.completed && this.completedAt == null) {
            this.completedAt = Instant.now();
        }
    }

    /**
     * Gets pending documents
     */
    public List<DocumentItem> getPendingDocuments() {
        if (this.documents == null) {
            return new ArrayList<>();
        }
        return this.documents.stream()
                .filter(d -> d.getStatus() == DocumentStatus.PENDING)
                .toList();
    }

    /**
     * Gets verified documents
     */
    public List<DocumentItem> getVerifiedDocuments() {
        if (this.documents == null) {
            return new ArrayList<>();
        }
        return this.documents.stream()
                .filter(d -> d.getStatus() == DocumentStatus.VERIFIED)
                .toList();
    }

    /**
     * Gets rejected documents
     */
    public List<DocumentItem> getRejectedDocuments() {
        if (this.documents == null) {
            return new ArrayList<>();
        }
        return this.documents.stream()
                .filter(d -> d.getStatus() == DocumentStatus.REJECTED)
                .toList();
    }

    private java.util.Optional<DocumentItem> findItemById(String itemId) {
        if (this.documents == null) {
            return java.util.Optional.empty();
        }
        return this.documents.stream()
                .filter(d -> d.getItemId().equals(itemId))
                .findFirst();
    }

    private static String generateChecklistId() {
        return "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static String generateItemId() {
        return "ITM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Document Status Enum
     */
    public enum DocumentStatus {
        PENDING,
        UPLOADED,
        VERIFIED,
        REJECTED
    }

    /**
     * Document Item Entity
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentItem {
        private String itemId;
        private String documentType;
        private String fileName;
        private String fileUrl;
        private Long fileSizeBytes;
        private Boolean required;
        private DocumentStatus status;
        private String uploadedBy;
        private Instant uploadedAt;
        private String verifiedBy;
        private Instant verifiedAt;
        private String verificationNotes;
        private String addedBy;
        private Instant addedAt;
        private String notes;
    }
}
