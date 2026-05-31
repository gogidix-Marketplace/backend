package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

/**
 * Enum representing the type of ProductRecommendation.
 */
public enum RecommendationType {
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
    CUSTOM,

    COLLABORATIVE_FILTERING,

    PERSONALIZED_BASED_ON_HISTORY,

    TREND_BASED,

    CROSS_SELL,

    CONTENT_BASED,

    HYBRID
}
