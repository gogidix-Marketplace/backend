package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
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
import java.util.HashSet;
import java.util.Set;

/**
 * User domain entity.
 * <p>
 * Represents a user in the system with multi-tenant support.
 * Each user belongs to a specific tenant for data isolation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    @Builder.Default
    private Set<String> roles = new HashSet<>();

    @Builder.Default
    private Set<String> permissions = new HashSet<>();

    private String lastLoginAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Checks if the user has the specified role.
     *
     * @param role the role to check
     * @return true if the user has the role
     */
    public boolean hasRole(String role) {
        return roles != null && role != null && roles.contains(role);
    }

    /**
     * Checks if the user has the specified permission.
     *
     * @param permission the permission to check
     * @return true if the user has the permission
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permission != null && permissions.contains(permission);
    }

    /**
     * Adds a role to the user.
     *
     * @param role the role to add
     */
    public void addRole(String role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    /**
     * Adds a permission to the user.
     *
     * @param permission the permission to add
     */
    public void addPermission(String permission) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.add(permission);
    }
}
