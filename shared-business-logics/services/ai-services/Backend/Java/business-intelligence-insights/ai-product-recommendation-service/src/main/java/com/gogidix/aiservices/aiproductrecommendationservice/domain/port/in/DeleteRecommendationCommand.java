package com.gogidix.aiservices.aiproductrecommendationservice.domain.port.in;

/**
 * Input port for deleting a product recommendation.
 */
public interface DeleteRecommendationCommand {

    /**
     * Get the segment ID to delete.
     *
     * @return the segment ID
     */
    String getRecommendationId();

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
