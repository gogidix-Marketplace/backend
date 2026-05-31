package com.gogidix.shared.warehousing.access.application.dto;

import com.gogidix.shared.warehousing.access.domain.entity.AccessLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Access Log
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String accessRequestId;
    private String userId;
    private String userName;
    private String userType;
    private AccessLog.AccessType accessType;
    private LocalDateTime accessedAt;
    private LocalDateTime exitAt;
    private Long durationSeconds;
    private AccessLog.AccessResult result;
    private String failureReason;
    private String badgeId;
    private String gateId;
    private Map<String, Object> metadata;
    private String referenceType;
    private String referenceId;
    private LocalDateTime createdAt;
}
