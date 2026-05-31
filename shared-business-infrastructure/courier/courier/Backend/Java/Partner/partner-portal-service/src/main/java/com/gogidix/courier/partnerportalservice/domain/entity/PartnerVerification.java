package com.gogidix.courier.partnerportalservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "partner_verifications")
public class PartnerVerification {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("partner_id")
    private String partnerId;

    @Field("verification_number")
    private String verificationNumber;

    @Field("status")
    private VerificationStatus status;

    @Field("required_documents")
    private List<String> requiredDocuments;

    @Field("submitted_documents")
    private List<String> submittedDocuments;

    @Field("verified_documents")
    private List<String> verifiedDocuments;

    @Field("notes")
    private String notes;

    @Field("verified_by")
    private String verifiedBy;

    @Field("submitted_at")
    private Instant submittedAt;

    @Field("completed_at")
    private Instant completedAt;

    protected PartnerVerification() {
    }

    public PartnerVerification(String tenantId, String partnerId, List<String> requiredDocuments) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.partnerId = Objects.requireNonNull(partnerId);
        this.verificationNumber = "VER-" + System.currentTimeMillis();
        this.status = VerificationStatus.PENDING;
        this.requiredDocuments = requiredDocuments;
        this.submittedDocuments = new ArrayList<>();
        this.verifiedDocuments = new ArrayList<>();
    }

    public void addDocument(String documentId) {
        this.submittedDocuments.add(documentId);
    }

    public void verifyDocument(String documentId) {
        this.verifiedDocuments.add(documentId);
    }

    public boolean isComplete() {
        return verifiedDocuments.containsAll(requiredDocuments);
    }

    public void complete(String verifiedBy) {
        if (isComplete()) {
            this.status = VerificationStatus.VERIFIED;
            this.verifiedBy = verifiedBy;
            this.completedAt = Instant.now();
        }
    }

    public void reject(String notes) {
        this.status = VerificationStatus.REJECTED;
        this.notes = notes;
        this.completedAt = Instant.now();
    }

    public enum VerificationStatus {
        PENDING,
        IN_REVIEW,
        VERIFIED,
        REJECTED,
        EXPIRED
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getPartnerId() { return partnerId; }
    public String getVerificationNumber() { return verificationNumber; }
    public VerificationStatus getStatus() { return status; }
    public List<String> getRequiredDocuments() { return requiredDocuments; }
    public List<String> getSubmittedDocuments() { return submittedDocuments; }
    public List<String> getVerifiedDocuments() { return verifiedDocuments; }
    public String getNotes() { return notes; }
    public String getVerifiedBy() { return verifiedBy; }
    public Instant getSubmittedAt() { return submittedAt; }
    public Instant getCompletedAt() { return completedAt; }

    // Setters
    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    protected void setVerificationNumber(String verificationNumber) { this.verificationNumber = verificationNumber; }
    protected void setStatus(VerificationStatus status) { this.status = status; }
    protected void setRequiredDocuments(List<String> requiredDocuments) { this.requiredDocuments = requiredDocuments; }
    protected void setSubmittedDocuments(List<String> submittedDocuments) { this.submittedDocuments = submittedDocuments; }
    protected void setVerifiedDocuments(List<String> verifiedDocuments) { this.verifiedDocuments = verifiedDocuments; }
    protected void setNotes(String notes) { this.notes = notes; }
    protected void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }
    protected void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    protected void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
