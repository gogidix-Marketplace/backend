package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

/**
 * Enum representing the type of UserProfile.
 */
public enum ProfileType {
    /**
     * Based on customer behavior patterns.
     */
    BEHAVIORAL,

    /**
     * Based on demographic characteristics.
     */
    DEMOGRAPHIC,

    /**
     * Based on transaction history.
     */
    TRANSACTIONAL,

    /**
     * Custom defined segment.
     */
    CUSTOM
}
