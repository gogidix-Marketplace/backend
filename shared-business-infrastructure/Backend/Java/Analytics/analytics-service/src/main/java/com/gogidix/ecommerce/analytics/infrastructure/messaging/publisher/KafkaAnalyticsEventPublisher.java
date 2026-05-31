package com.gogidix.ecommerce.analytics.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.analytics.domain.event.AnalyticsDomainEvent;
import com.gogidix.ecommerce.analytics.domain.port.out.AnalyticsEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaAnalyticsEventPublisher implements AnalyticsEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaAnalyticsEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaAnalyticsEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(AnalyticsDomainEvent event) {
        try {
            streamBridge.send("analyticsEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
