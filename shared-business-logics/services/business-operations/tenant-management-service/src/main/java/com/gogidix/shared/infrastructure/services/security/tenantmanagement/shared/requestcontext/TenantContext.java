package com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Tenant context holder containing tenant and user information
 * for the current request. This context is propagated throughout
 * the request handling chain.
 */
@Data
@Builder
public class TenantContext {
    private String tenantId;
    private String userId;
    private String correlationId;
    private Set<String> roles;
    private List<String> permissions;
    private String userName;
    private String userEmail;

    /**
     * Check if the current user has the specified role.
     *
     * @param role the role to check
     * @return true if the user has the role
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * Check if the current user has the specified permission.
     *
     * @param permission the permission to check
     * @return true if the user has the permission
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }

    /**
     * Check if this is an admin context.
     *
     * @return true if the user has admin role
     */
    public boolean isAdmin() {
        return hasRole("ADMIN") || hasRole("SYSTEM_ADMIN");
    }
}
