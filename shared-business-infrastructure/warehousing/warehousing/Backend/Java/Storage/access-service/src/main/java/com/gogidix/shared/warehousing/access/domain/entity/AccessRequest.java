package com.gogidix.shared.warehousing.access.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Access Request Entity
 *
 * Represents a request for storage access
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "access_requests")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'createdAt': -1}", name = "idx_access_request_tenant")
public class AccessRequest {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String zoneId;

    private String requestId;

    private AccessType accessType;

    private RequestPurpose purpose;

    private String requestedBy;

    private String requestedFor;

    private LocalDateTime requestedStartTime;

    private LocalDateTime requestedEndTime;

    private Integer estimatedDurationMinutes;

    private RequestStatus status;

    private String approvedBy;

    private LocalDateTime approvedAt;

    private String rejectionReason;

    private LocalDateTime completedAt;

    private String notes;

    private String referenceType;

    private String referenceId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum AccessType {
        ENTRY,
        EXIT,
        PICKUP,
        DROP_OFF,
        INSPECTION,
        MAINTENANCE,
        AUDIT
    }

    public enum RequestPurpose {
        INBOUND_RECEIVING,
        OUTBOUND_SHIPPING,
        INVENTORY_COUNT,
        STOCK_PUTAWAY,
        STOCK_RETRIEVAL,
        QUALITY_CHECK,
        DAMAGE_ASSESSMENT,
        GENERAL_ACCESS,
        EMERGENCY_ACCESS
    }

    public enum RequestStatus {
        PENDING,
        APPROVED,
        DENIED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        EXPIRED
    }

    /**
     * Approve the access request
     */
    public void approve(String approvedBy) {
        this.status = RequestStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * Deny the access request
     */
    public void deny(String reason) {
        this.status = RequestStatus.DENIED;
        this.rejectionReason = reason;
    }

    /**
     * Start the access
     */
    public void start() {
        this.status = RequestStatus.IN_PROGRESS;
    }

    /**
     * Complete the access
     */
    public void complete() {
        this.status = RequestStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Cancel the access request
     */
    public void cancel() {
        this.status = RequestStatus.CANCELLED;
    }

    /**
     * Check if request has expired
     */
    public boolean isExpired() {
        return requestedEndTime != null && LocalDateTime.now().isAfter(requestedEndTime) &&
               (status == RequestStatus.PENDING || status == RequestStatus.APPROVED);
    }
}
