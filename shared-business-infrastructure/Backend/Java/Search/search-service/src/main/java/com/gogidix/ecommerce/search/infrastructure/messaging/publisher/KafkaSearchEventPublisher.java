package com.gogidix.ecommerce.search.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.search.domain.event.SearchDomainEvent;
import com.gogidix.ecommerce.search.domain.port.out.SearchEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchEventPublisher implements SearchEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaSearchEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(SearchDomainEvent event) {
        try {
            streamBridge.send("searchEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
