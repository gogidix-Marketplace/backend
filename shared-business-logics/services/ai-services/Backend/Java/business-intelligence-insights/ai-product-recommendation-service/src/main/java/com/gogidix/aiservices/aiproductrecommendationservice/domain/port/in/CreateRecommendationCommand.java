package com.gogidix.aiservices.aiproductrecommendationservice.domain.port.in;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;

/**
 * Input port for creating a new product recommendation.
 */
public interface CreateRecommendationCommand {

    /**
     * Get the segment name.
     *
     * @return the name
     */
    String getName();

    /**
     * Get the segment description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Get the segment type.
     *
     * @return the segment type
     */
    String getRecommendationType();

    /**
     * Get the segment criteria.
     *
     * @return the criteria
     */
    RecommendationCriteria getCriteria();

    /**
     * Get the tenant ID from context.
     *
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * Get the user ID from context.
     *
     * @return the user ID
     */
    String getUserId();
}
