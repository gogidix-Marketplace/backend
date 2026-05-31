package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

/**
 * Enum representing the status of a UserProfile.
 */
public enum ProfileStatus {
    /**
     * Profile is being created and not yet active.
     */
    DRAFT,

    /**
     * Profile is active and used for targeting.
     */
    ACTIVE,

    /**
     * Profile is temporarily disabled.
     */
    INACTIVE,

    /**
     * Profile is archived and no longer in use.
     */
    ARCHIVED
}
