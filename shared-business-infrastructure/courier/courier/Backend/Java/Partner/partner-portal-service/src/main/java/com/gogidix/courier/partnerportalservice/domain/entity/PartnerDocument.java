package com.gogidix.courier.partnerportalservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

@Document(collection = "partner_documents")
public class PartnerDocument {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("partner_id")
    private String partnerId;

    @Field("document_type")
    private DocumentType documentType;

    @Field("file_name")
    private String fileName;

    @Field("file_url")
    private String fileUrl;

    @Field("file_size")
    private Long fileSize;

    @Field("mime_type")
    private String mimeType;

    @Field("status")
    private DocumentStatus status;

    @Field("verified_by")
    private String verifiedBy;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("uploaded_at")
    private Instant uploadedAt;

    @Field("verified_at")
    private Instant verifiedAt;

    @Field("expiry_date")
    private Instant expiryDate;

    protected PartnerDocument() {
    }

    public PartnerDocument(String tenantId, String partnerId, DocumentType documentType,
                          String fileName, String fileUrl, Long fileSize, String mimeType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.partnerId = Objects.requireNonNull(partnerId);
        this.documentType = documentType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.status = DocumentStatus.PENDING;
        this.uploadedAt = Instant.now();
    }

    public void approve(String verifiedBy) {
        this.status = DocumentStatus.APPROVED;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = Instant.now();
    }

    public void reject(String verifiedBy, String reason) {
        this.status = DocumentStatus.REJECTED;
        this.verifiedBy = verifiedBy;
        this.rejectionReason = reason;
        this.verifiedAt = Instant.now();
    }

    public boolean isExpired() {
        return expiryDate != null && Instant.now().isAfter(expiryDate);
    }

    public enum DocumentType {
        ID_DOCUMENT,
        TAX_CERTIFICATE,
        BANK_STATEMENT,
        INSURANCE,
        BUSINESS_LICENSE,
        AGREEMENT,
        OTHER
    }

    public enum DocumentStatus {
        PENDING,
        APPROVED,
        REJECTED,
        EXPIRED
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getPartnerId() { return partnerId; }
    public DocumentType getDocumentType() { return documentType; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public Long getFileSize() { return fileSize; }
    public String getMimeType() { return mimeType; }
    public DocumentStatus getStatus() { return status; }
    public String getVerifiedBy() { return verifiedBy; }
    public String getRejectionReason() { return rejectionReason; }
    public Instant getUploadedAt() { return uploadedAt; }
    public Instant getVerifiedAt() { return verifiedAt; }
    public Instant getExpiryDate() { return expiryDate; }

    // Setters
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    protected void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    protected void setFileName(String fileName) { this.fileName = fileName; }
    protected void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    protected void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    protected void setMimeType(String mimeType) { this.mimeType = mimeType; }
    protected void setStatus(DocumentStatus status) { this.status = status; }
    protected void setVerifiedBy(String verifiedBy) { this.verifiedBy = verifiedBy; }
    protected void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    protected void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }
    protected void setVerifiedAt(Instant verifiedAt) { this.verifiedAt = verifiedAt; }
}
