package com.gogidix.aiservices.aigatewayservice.infrastructure.messaging.publisher;

import com.gogidix.aiservices.aigatewayservice.domain.event.RouteEvent;
import com.gogidix.aiservices.aigatewayservice.domain.port.out.RouteEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * In-memory event publisher for route events.
 * In production, this would publish to Kafka or similar message broker.
 */
@Component
public class RouteEventPublisher implements RouteEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RouteEventPublisher.class);

    @Override
    public void publish(RouteEvent event) {
        log.info("Publishing event: {} for route: {} in tenant: {}",
                event.getEventType(), event.getRouteId(), event.getTenantId());
        // In production, publish to Kafka/RabbitMQ
    }
}
