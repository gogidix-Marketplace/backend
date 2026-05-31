package com.gogidix.sysadmin.accessrequest.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "access_requests")
public class AccessRequest {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String requestNumber;
    private String requestedBy;
    private String requestedFor;
    private RequestType requestType;
    private RequestStatus status;
    private String resourceType;
    private List<String> resourceIds;
    private String accessLevel;
    private String justification;
    private Instant startDateTime;
    private Instant endDateTime;
    private String approvedBy;
    private Instant approvedAt;
    private String approvalComments;
    private String rejectedBy;
    private Instant rejectedAt;
    private String rejectionReason;
    private Instant grantedAt;
    private Instant revokedAt;
    private String revokedBy;
    private String revocationReason;
    private List<String> approvers;
    private Map<String, String> metadata;
    private Instant createdAt;
    private Instant updatedAt;

    public enum RequestType {
        GRANT, REVOKE, EXTEND, MODIFY
    }

    public enum RequestStatus {
        PENDING_APPROVAL, APPROVED, REJECTED, GRANTED, ACTIVE,
        EXPIRED, REVOKED, CANCELLED, AWAITING_INFO
    }

    public AccessRequest() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = RequestStatus.PENDING_APPROVAL;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getRequestNumber() { return requestNumber; }
    public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }

    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }

    public String getRequestedFor() { return requestedFor; }
    public void setRequestedFor(String requestedFor) { this.requestedFor = requestedFor; }

    public RequestType getRequestType() { return requestType; }
    public void setRequestType(RequestType requestType) { this.requestType = requestType; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public List<String> getResourceIds() { return resourceIds; }
    public void setResourceIds(List<String> resourceIds) { this.resourceIds = resourceIds; }

    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public Instant getStartDateTime() { return startDateTime; }
    public void setStartDateTime(Instant startDateTime) { this.startDateTime = startDateTime; }

    public Instant getEndDateTime() { return endDateTime; }
    public void setEndDateTime(Instant endDateTime) { this.endDateTime = endDateTime; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public Instant getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }

    public String getApprovalComments() { return approvalComments; }
    public void setApprovalComments(String approvalComments) { this.approvalComments = approvalComments; }

    public String getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(String rejectedBy) { this.rejectedBy = rejectedBy; }

    public Instant getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(Instant rejectedAt) { this.rejectedAt = rejectedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public Instant getGrantedAt() { return grantedAt; }
    public void setGrantedAt(Instant grantedAt) { this.grantedAt = grantedAt; }

    public Instant getRevokedAt() { return revokedAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }

    public String getRevokedBy() { return revokedBy; }
    public void setRevokedBy(String revokedBy) { this.revokedBy = revokedBy; }

    public String getRevocationReason() { return revocationReason; }
    public void setRevocationReason(String revocationReason) { this.revocationReason = revocationReason; }

    public List<String> getApprovers() { return approvers; }
    public void setApprovers(List<String> approvers) { this.approvers = approvers; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void approve(String approvedBy, String comments) {
        this.status = RequestStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.approvalComments = comments;
        this.updatedAt = Instant.now();
    }

    public void reject(String rejectedBy, String reason) {
        this.status = RequestStatus.REJECTED;
        this.rejectedBy = rejectedBy;
        this.rejectedAt = Instant.now();
        this.rejectionReason = reason;
        this.updatedAt = Instant.now();
    }

    public void grant() {
        this.status = RequestStatus.GRANTED;
        this.grantedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void revoke(String revokedBy, String reason) {
        this.status = RequestStatus.REVOKED;
        this.revokedBy = revokedBy;
        this.revokedAt = Instant.now();
        this.revocationReason = reason;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessRequest that = (AccessRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
