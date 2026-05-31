package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model;

/**
 * Enum representing the status of a MarketBasket.
 */
public enum BasketStatus {
    /**
     * Basket is being created and not yet active.
     */
    DRAFT,

    /**
     * Basket is active and used for targeting.
     */
    ACTIVE,

    /**
     * Basket is temporarily disabled.
     */
    INACTIVE,

    /**
     * Basket is archived and no longer in use.
     */
    ARCHIVED
}
