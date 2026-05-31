package com.gogidix.shared.warehousing.access.application.dto;

import com.gogidix.shared.warehousing.access.domain.entity.AccessRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Access Request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String requestId;
    private AccessRequest.AccessType accessType;
    private AccessRequest.RequestPurpose purpose;
    private String requestedBy;
    private String requestedFor;
    private LocalDateTime requestedStartTime;
    private LocalDateTime requestedEndTime;
    private Integer estimatedDurationMinutes;
    private AccessRequest.RequestStatus status;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private LocalDateTime completedAt;
    private String notes;
    private String referenceType;
    private String referenceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
