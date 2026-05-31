package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

/**
 * Domain model representing a permission for authorization.
 */
public enum Permission {

    READ("read", "Read access to resources"),
    WRITE("write", "Write access to resources"),
    DELETE("delete", "Delete access to resources"),
    UPDATE("update", "Update access to resources"),
    ADMIN("admin", "Administrative privileges"),
    USER_MANAGEMENT("user_management", "User management operations"),
    ROLE_MANAGEMENT("role_management", "Role management operations"),
    TENANT_MANAGEMENT("tenant_management", "Tenant management operations"),
    TOKEN_MANAGEMENT("token_management", "Token management operations");

    private final String code;
    private final String description;

    Permission(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code;
    }
}
