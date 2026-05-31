package com.gogidix.ecommerce.reward.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.reward.domain.event.RewardDomainEvent;
import com.gogidix.ecommerce.reward.domain.port.out.RewardEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaRewardEventPublisher implements RewardEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaRewardEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaRewardEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(RewardDomainEvent event) {
        try {
            streamBridge.send("rewardEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
