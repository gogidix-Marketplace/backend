package com.gogidix.shared.messaging.event;

import com.gogidix.shared.messaging.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Facade for EventPublisher - provides simpler import path.
 * <p>
 * This interface provides backward compatibility for services expecting the simpler
 * package structure. All functionality is delegated to the canonical
 * {@link com.gogidix.shared.messaging.domain.publisher.EventPublisher} interface
 * in the domain.publisher package.
 * </p>
 * <p>
 * For DeliveryOptions and DeliveryMode access, use:
 * <pre>{@code
 * import com.gogidix.shared.messaging.domain.publisher.EventPublisher;
 * EventPublisher.DeliveryOptions options = EventPublisher.DeliveryOptions.reliable();
 * }</pre>
 * </p>
 *
 * @deprecated Use {@link com.gogidix.shared.messaging.domain.publisher.EventPublisher} instead.
 *             This facade exists for backward compatibility with existing service imports.
 */
@Deprecated
public interface EventPublisher extends com.gogidix.shared.messaging.domain.publisher.EventPublisher {

    // Note: DeliveryOptions and DeliveryMode are accessed via the canonical interface:
    // com.gogidix.shared.messaging.domain.publisher.EventPublisher.DeliveryOptions
    // com.gogidix.shared.messaging.domain.publisher.EventPublisher.DeliveryOptions.DeliveryMode
}
