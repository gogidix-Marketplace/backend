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
import java.util.List;

/**
 * Access Permission Entity
 *
 * Defines access permissions for users/roles to storage areas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "access_permissions")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'userId': 1}", name = "idx_permission_tenant_user")
public class AccessPermission {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String userId;

    private String userName;

    private String role;

    private PermissionType permissionType;

    private List<String> allowedZones;

    private List<String> deniedZones;

    private List<AccessType> allowedAccessTypes;

    private TimeRestriction timeRestriction;

    private Boolean requiresApproval;

    private String approverRole;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    private Boolean active;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum PermissionType {
        USER,
        ROLE,
        TEMPORARY,
        EMERGENCY
    }

    public enum TimeRestriction {
        NONE,
        BUSINESS_HOURS_ONLY,
        NIGHT_SHIFT_ONLY,
        WEEKDAYS_ONLY,
        WEEKENDS_ONLY,
        CUSTOM
    }

    /**
     * Check if permission is currently valid
     */
    public boolean isValid() {
        if (!active) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validUntil != null && now.isAfter(validUntil)) {
            return false;
        }
        return true;
    }

    /**
     * Check if user has access to zone
     */
    public boolean hasZoneAccess(String zoneId) {
        if (deniedZones != null && deniedZones.contains(zoneId)) {
            return false;
        }
        if (allowedZones == null || allowedZones.isEmpty()) {
            return true; // No restriction
        }
        return allowedZones.contains(zoneId) || allowedZones.contains("*");
    }

    /**
     * Check if access type is allowed
     */
    public boolean isAccessTypeAllowed(AccessType accessType) {
        if (allowedAccessTypes == null || allowedAccessTypes.isEmpty()) {
            return true; // No restriction
        }
        return allowedAccessTypes.contains(accessType);
    }

    public enum AccessType {
        ENTRY,
        EXIT,
        PICKUP,
        DROP_OFF,
        INSPECTION,
        MAINTENANCE,
        AUDIT,
        FULL_ACCESS
    }
}
