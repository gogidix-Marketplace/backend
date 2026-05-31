package com.gogidix.aiservices.aigatewayservice.domain.port.out;

import com.gogidix.aiservices.aigatewayservice.domain.event.RouteEvent;

/**
 * Output port for publishing route events.
 */
public interface RouteEventPublisherPort {

    void publish(RouteEvent event);
}
