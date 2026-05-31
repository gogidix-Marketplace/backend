package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.in;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;

/**
 * Input port for creating a new market basket.
 */
public interface CreateBasketCommand {

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
    String getBasketType();

    /**
     * Get the segment criteria.
     *
     * @return the criteria
     */
    BasketCriteria getCriteria();

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
