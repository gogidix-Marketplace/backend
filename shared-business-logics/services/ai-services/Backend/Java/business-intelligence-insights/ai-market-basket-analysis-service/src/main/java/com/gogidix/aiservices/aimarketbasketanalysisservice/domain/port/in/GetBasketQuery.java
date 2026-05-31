package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.in;

/**
 * Input port for querying market baskets.
 */
public interface GetBasketQuery {

    /**
     * Get the segment ID to query.
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
}
