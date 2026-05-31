package com.gogidix.aiservices.aigatewayservice.domain.model;

/**
 * Load balancing strategies for gateway routes.
 */
public enum LoadBalancingStrategy {
    /**
     * Distribute requests evenly across all instances.
     */
    ROUND_ROBIN,

    /**
     * Send requests to the instance with fewest active connections.
     */
    LEAST_CONNECTIONS,

    /**
     * Distribute requests based on client IP hash.
     */
    IP_HASH,

    /**
     * Distribute requests randomly.
     */
    RANDOM
}
