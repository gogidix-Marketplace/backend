package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.in;

/**
 * Input port for deleting a market basket.
 */
public interface DeleteBasketCommand {

    /**
     * Get the segment ID to delete.
     *
     * @return the segment ID
     */
    String getBasketId();

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
