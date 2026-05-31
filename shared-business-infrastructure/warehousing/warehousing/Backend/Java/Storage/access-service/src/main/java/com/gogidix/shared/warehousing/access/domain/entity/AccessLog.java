package com.gogidix.shared.warehousing.access.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Access Log Entity
 *
 * Records actual access events to storage areas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "access_logs")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'accessedAt': -1}", name = "idx_access_log_tenant_time")
public class AccessLog {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String zoneId;

    private String accessRequestId;

    @Indexed
    private String userId;

    private String userName;

    private String userType;

    @Indexed
    private AccessType accessType;

    private LocalDateTime accessedAt;

    private LocalDateTime exitAt;

    private Long durationSeconds;

    private AccessResult result;

    private String failureReason;

    private String badgeId;

    private String gateId;

    private Map<String, Object> metadata;

    private String referenceType;

    private String referenceId;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum AccessType {
        ENTRY,
        EXIT,
        PICKUP,
        DROP_OFF,
        INSPECTION,
        MAINTENANCE,
        AUDIT,
        EMERGENCY
    }

    public enum AccessResult {
        SUCCESS,
        DENIED,
        PARTIAL,
        TIMEOUT,
        ERROR
    }

    /**
     * Complete the access log with exit time
     */
    public void complete() {
        this.exitAt = LocalDateTime.now();
        if (this.accessedAt != null) {
            this.durationSeconds = java.time.Duration.between(this.accessedAt, this.exitAt).getSeconds();
        }
    }
}
