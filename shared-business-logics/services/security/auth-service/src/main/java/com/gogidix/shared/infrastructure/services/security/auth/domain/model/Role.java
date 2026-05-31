package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

import java.util.Set;

/**
 * Domain model representing a user role.
 */
public class Role {

    private final String roleId;
    private final String name;
    private final String description;
    private final Set<Permission> permissions;

    public Role(String roleId, String name, String description, Set<Permission> permissions) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
        this.permissions = permissions != null ? permissions : Set.of();
    }

    public String getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return roleId.equals(role.roleId);
    }

    @Override
    public int hashCode() {
        return roleId.hashCode();
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId='" + roleId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    // Predefined roles
    public static final Role ADMIN = new Role("role-admin", "ADMIN", "Administrator with full access", Set.of(Permission.values()));
    public static final Role USER = new Role("role-user", "USER", "Standard user role", Set.of(
            Permission.READ, Permission.WRITE
    ));
    public static final Role GUEST = new Role("role-guest", "GUEST", "Guest with read-only access", Set.of(
            Permission.READ
    ));
}
