package com.gogidix.corporatecms.domain.enums;

/**
 * Enumeration representing granular permissions for access control.
 */
public enum Permission {
    // Content permissions
    CONTENT_READ,
    CONTENT_WRITE,
    CONTENT_DELETE,
    CONTENT_PUBLISH,
    CONTENT_APPROVE,

    // Media permissions
    MEDIA_MANAGE,

    // User management
    USER_MANAGE,

    // Domain-specific permissions
    PRODUCT_MANAGE,
    CAREER_MANAGE,
    PRESS_RELEASE_MANAGE,
    DEVELOPER_RESOURCE_MANAGE,
    LEAD_MANAGE,

    // System permissions
    SETTINGS_MANAGE,
    ANALYTICS_VIEW
}
