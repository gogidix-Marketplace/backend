package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tenant domain entity.
 * <p>
 * Represents a tenant in the multi-tenant SaaS application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tenants")
public class Tenant {

    @Id
    private String id;

    @Indexed(unique = true)
    private String tenantId;

    private String name;

    private String domain;

    private String logoUrl;

    private TenantStatus status;

    private TenantPlan plan;

    private LocalDateTime trialEndsAt;

    private Map<String, Object> settings;

    private Map<String, Object> features;

    private String primaryContactEmail;

    private String primaryContactName;

    private long maxUsers;

    private long maxStorageGB;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Tenant status.
     */
    public enum TenantStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        TRIAL,
        PENDING_VERIFICATION
    }

    /**
     * Tenant plan types.
     */
    public enum TenantPlan {
        FREE,
        STARTER,
        PROFESSIONAL,
        ENTERPRISE,
        CUSTOM
    }

    /**
     * Checks if the tenant is active.
     *
     * @return true if the tenant is active
     */
    public boolean isActive() {
        if (status == TenantStatus.SUSPENDED || status == TenantStatus.INACTIVE) {
            return false;
        }

        if (status == TenantStatus.TRIAL && trialEndsAt != null) {
            return LocalDateTime.now().isBefore(trialEndsAt);
        }

        return status == TenantStatus.ACTIVE || status == TenantStatus.TRIAL;
    }

    /**
     * Checks if the tenant can add more users.
     *
     * @param currentUsers the current number of users
     * @return true if the tenant can add more users
     */
    public boolean canAddUsers(long currentUsers) {
        return maxUsers == 0 || currentUsers < maxUsers;
    }
}
