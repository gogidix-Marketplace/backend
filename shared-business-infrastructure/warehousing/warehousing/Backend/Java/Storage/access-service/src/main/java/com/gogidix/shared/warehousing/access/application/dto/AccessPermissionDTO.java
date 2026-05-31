package com.gogidix.shared.warehousing.access.application.dto;

import com.gogidix.shared.warehousing.access.domain.entity.AccessPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Access Permission
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessPermissionDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String userId;
    private String userName;
    private String role;
    private AccessPermission.PermissionType permissionType;
    private List<String> allowedZones;
    private List<String> deniedZones;
    private List<AccessPermission.AccessType> allowedAccessTypes;
    private AccessPermission.TimeRestriction timeRestriction;
    private Boolean requiresApproval;
    private String approverRole;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Boolean active;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
