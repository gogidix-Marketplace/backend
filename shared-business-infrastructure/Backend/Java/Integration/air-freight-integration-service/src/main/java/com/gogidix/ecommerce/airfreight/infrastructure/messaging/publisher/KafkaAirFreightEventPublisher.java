package com.gogidix.ecommerce.airfreight.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.airfreight.domain.event.AirFreightDomainEvent;
import com.gogidix.ecommerce.airfreight.domain.port.out.AirFreightEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaAirFreightEventPublisher implements AirFreightEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaAirFreightEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaAirFreightEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(AirFreightDomainEvent event) {
        try {
            streamBridge.send("airfreightEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
