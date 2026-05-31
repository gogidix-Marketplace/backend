package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enumeration representing user roles for access control.
 */
@Getter
public enum UserRole {
    ADMIN(Set.of(
        Permission.CONTENT_READ,
        Permission.CONTENT_WRITE,
        Permission.CONTENT_DELETE,
        Permission.CONTENT_PUBLISH,
        Permission.CONTENT_APPROVE,
        Permission.MEDIA_MANAGE,
        Permission.USER_MANAGE,
        Permission.SETTINGS_MANAGE,
        Permission.ANALYTICS_VIEW
    )),
    CONTENT_EDITOR(Set.of(
        Permission.CONTENT_READ,
        Permission.CONTENT_WRITE,
        Permission.MEDIA_MANAGE,
        Permission.ANALYTICS_VIEW
    )),
    PRODUCT_MANAGER(Set.of(
        Permission.CONTENT_READ,
        Permission.CONTENT_WRITE,
        Permission.PRODUCT_MANAGE,
        Permission.ANALYTICS_VIEW
    )),
    HR_MANAGER(Set.of(
        Permission.CONTENT_READ,
        Permission.CONTENT_WRITE,
        Permission.CAREER_MANAGE,
        Permission.ANALYTICS_VIEW
    )),
    PR_MANAGER(Set.of(
        Permission.CONTENT_READ,
        Permission.CONTENT_WRITE,
        Permission.PRESS_RELEASE_MANAGE,
        Permission.ANALYTICS_VIEW
    )),
    VIEWER(Set.of(
        Permission.CONTENT_READ
    ));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
