package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model;

/**
 * Enum representing the type of MarketBasket.
 */
public enum BasketType {
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
