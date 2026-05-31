package com.gogidix.aiservices.aigatewayservice.domain.model;

/**
 * Status of a gateway route.
 */
public enum RouteStatus {
    /**
     * Route is active and handling requests.
     */
    ACTIVE,

    /**
     * Route is inactive and not handling requests.
     */
    INACTIVE,

    /**
     * Route is in maintenance mode.
     */
    MAINTENANCE,

    /**
     * Route has been deprecated.
     */
    DEPRECATED
}
