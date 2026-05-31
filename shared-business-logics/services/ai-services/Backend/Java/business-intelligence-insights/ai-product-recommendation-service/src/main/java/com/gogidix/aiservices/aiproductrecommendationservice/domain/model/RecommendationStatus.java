package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

/**
 * Enum representing the status of a ProductRecommendation.
 */
public enum RecommendationStatus {
    /**
     * Recommendation is being created and not yet active.
     */
    DRAFT,

    /**
     * Recommendation is active and used for targeting.
     */
    ACTIVE,

    /**
     * Recommendation is temporarily disabled.
     */
    INACTIVE,

    /**
     * Recommendation is archived and no longer in use.
     */
    ARCHIVED
}
