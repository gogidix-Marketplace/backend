package com.gogidix.hr.documentmanagement.domain.model;

import com.gogidix.hr.documentmanagement.shared.base.BaseEntity;
import com.gogidix.hr.documentmanagement.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Document Retention Policy Domain Entity
 * Defines retention policies for different document types
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "document_retention")
@CompoundIndex(def = "{'tenantId': 1, 'documentType': 1, 'category': 1, 'countryCode': 1}", name = "retention_policy_unique_idx", unique = true)
public class DocumentRetention extends BaseEntity {

    @Indexed(unique = true)
    private String retentionId;

    @Indexed
    private String tenantId;

    @Indexed
    private DocumentType documentType;

    @Indexed
    private DocumentCategory category;

    @Indexed
    private String countryCode;

    private Integer retentionPeriodYears;

    @Indexed
    private RetentionAction action;

    private String legalHold;
    private String complianceRequirement;

    @Indexed
    private LocalDate lastReviewDate;

    private String reviewedBy;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Boolean active;

    private String description;

    /**
     * Creates a new retention policy
     */
    public static DocumentRetention create(String tenantId, DocumentType documentType, DocumentCategory category,
                                          String countryCode, Integer retentionPeriodYears, RetentionAction action,
                                          String complianceRequirement, String createdBy) {
        String retentionId = generateRetentionId(documentType, category, countryCode);

        DocumentRetention retention = new DocumentRetention();
        retention.setTenantId(tenantId);
        retention.setRetentionId(retentionId);
        retention.setDocumentType(documentType);
        retention.setCategory(category);
        retention.setCountryCode(countryCode);
        retention.setRetentionPeriodYears(retentionPeriodYears != null ? retentionPeriodYears : 7);
        retention.setAction(action != null ? action : RetentionAction.ARCHIVE);
        retention.setLegalHold(null);
        retention.setComplianceRequirement(complianceRequirement);
        retention.setLastReviewDate(LocalDate.now());
        retention.setReviewedBy(createdBy);
        retention.setEffectiveFrom(LocalDate.now());
        retention.setActive(true);

        retention.validate();

        return retention;
    }

    /**
     * Calculates retention expiry date for a document
     */
    public LocalDate calculateRetentionExpiryDate(LocalDate documentDate) {
        if (documentDate == null) {
            return null;
        }

        if (this.retentionPeriodYears == null || this.retentionPeriodYears <= 0) {
            return null; // Keep permanently
        }

        return documentDate.plusYears(this.retentionPeriodYears);
    }

    /**
     * Checks if a document has exceeded its retention period
     */
    public boolean isRetentionExceeded(LocalDate documentDate) {
        LocalDate expiryDate = calculateRetentionExpiryDate(documentDate);
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }

    /**
     * Checks if a document is approaching retention expiry (within warning period)
     */
    public boolean isApproachingRetentionExpiry(LocalDate documentDate, int warningDays) {
        LocalDate expiryDate = calculateRetentionExpiryDate(documentDate);
        if (expiryDate == null) {
            return false;
        }

        LocalDate warningDate = expiryDate.minusDays(warningDays);
        LocalDate now = LocalDate.now();

        return now.isAfter(warningDate) && !now.isAfter(expiryDate);
    }

    /**
     * Gets days until retention expiry
     */
    public long getDaysUntilRetentionExpiry(LocalDate documentDate) {
        LocalDate expiryDate = calculateRetentionExpiryDate(documentDate);
        if (expiryDate == null) {
            return Long.MAX_VALUE;
        }

        if (LocalDate.now().isAfter(expiryDate)) {
            return 0;
        }

        return LocalDate.now().until(expiryDate).getDays();
    }

    /**
     * Places a legal hold on the retention policy
     */
    public void placeLegalHold(String reason, String placedBy) {
        this.legalHold = reason;
        this.lastReviewDate = LocalDate.now();
        this.reviewedBy = placedBy;
    }

    /**
     * Releases legal hold
     */
    public void releaseLegalHold(String releasedBy) {
        this.legalHold = null;
        this.lastReviewDate = LocalDate.now();
        this.reviewedBy = releasedBy;
    }

    /**
     * Checks if there is an active legal hold
     */
    public boolean hasLegalHold() {
        return this.legalHold != null && !this.legalHold.isBlank();
    }

    /**
     * Updates retention period
     */
    public void updateRetentionPeriod(Integer years, String updatedBy) {
        if (years != null && years <= 0) {
            throw new ValidationException("retentionPeriodYears", "Retention period must be positive");
        }

        this.retentionPeriodYears = years;
        this.lastReviewDate = LocalDate.now();
        this.reviewedBy = updatedBy;
    }

    /**
     * Updates retention action
     */
    public void updateRetentionAction(RetentionAction action, String updatedBy) {
        if (action == null) {
            throw new ValidationException("action", "Retention action is required");
        }

        this.action = action;
        this.lastReviewDate = LocalDate.now();
        this.reviewedBy = updatedBy;
    }

    /**
     * Marks policy as reviewed
     */
    public void markAsReviewed(String reviewedBy) {
        this.lastReviewDate = LocalDate.now();
        this.reviewedBy = reviewedBy;
    }

    /**
     * Activates the policy
     */
    public void activate() {
        this.active = true;
        if (this.effectiveFrom == null || LocalDate.now().isBefore(this.effectiveFrom)) {
            this.effectiveFrom = LocalDate.now();
        }
    }

    /**
     * Deactivates the policy
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Sets effective period
     */
    public void setEffectivePeriod(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("effectiveFrom", "Effective from date must be before effective to date");
        }

        this.effectiveFrom = from;
        this.effectiveTo = to;
    }

    /**
     * Checks if policy is currently effective
     */
    public boolean isEffective() {
        if (!this.active) {
            return false;
        }

        LocalDate now = LocalDate.now();

        if (effectiveFrom != null && now.isBefore(effectiveFrom)) {
            return false;
        }

        if (effectiveTo != null && now.isAfter(effectiveTo)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if policy matches document criteria
     */
    public boolean matches(DocumentType documentType, DocumentCategory category, String countryCode) {
        return this.documentType == documentType &&
               (this.category == null || this.category == category) &&
               this.countryCode.equals(countryCode) &&
               isEffective();
    }

    /**
     * Gets next review date (default: annually)
     */
    public LocalDate getNextReviewDate() {
        if (this.lastReviewDate == null) {
            return LocalDate.now().plusYears(1);
        }
        return this.lastReviewDate.plusYears(1);
    }

    /**
     * Checks if review is overdue
     */
    public boolean isReviewOverdue() {
        LocalDate nextReview = getNextReviewDate();
        return LocalDate.now().isAfter(nextReview);
    }

    /**
     * Gets days until next review
     */
    public long getDaysUntilReview() {
        LocalDate nextReview = getNextReviewDate();
        if (LocalDate.now().isAfter(nextReview)) {
            return 0;
        }
        return LocalDate.now().until(nextReview).getDays();
    }

    /**
     * Validates retention policy
     */
    public void validate() {
        if (this.documentType == null) {
            throw new ValidationException("documentType", "Document type is required");
        }
        if (this.category == null) {
            throw new ValidationException("category", "Category is required");
        }
        if (this.countryCode == null || this.countryCode.isBlank()) {
            throw new ValidationException("countryCode", "Country code is required");
        }
        if (this.retentionPeriodYears != null && this.retentionPeriodYears <= 0) {
            throw new ValidationException("retentionPeriodYears", "Retention period must be positive");
        }
        if (this.action == null) {
            throw new ValidationException("action", "Retention action is required");
        }

        if (effectiveFrom != null && effectiveTo != null && effectiveFrom.isAfter(effectiveTo)) {
            throw new ValidationException("effectivePeriod", "Effective from date must be before effective to date");
        }
    }

    /**
     * Gets display description
     */
    public String getDisplayDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.documentType).append(" documents");

        if (this.category != null) {
            sb.append(" in ").append(this.category);
        }

        sb.append(" (").append(this.countryCode).append(")");

        if (this.retentionPeriodYears != null) {
            sb.append(" - Retain for ").append(this.retentionPeriodYears).append(" years");
        }

        if (this.action != null) {
            sb.append(", then ").append(this.action.name().toLowerCase().replace("_", " "));
        }

        if (hasLegalHold()) {
            sb.append(" [LEGAL HOLD]");
        }

        return sb.toString();
    }

    private static String generateRetentionId(DocumentType type, DocumentCategory category, String countryCode) {
        String typeCode = type.name().substring(0, 3).toUpperCase();
        String categoryCode = category != null ? category.name().substring(0, 3).toUpperCase() : "ALL";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "RET-" + countryCode.toUpperCase() + "-" + typeCode + "-" + categoryCode + "-" + timestamp.substring(timestamp.length() - 6);
    }
}
