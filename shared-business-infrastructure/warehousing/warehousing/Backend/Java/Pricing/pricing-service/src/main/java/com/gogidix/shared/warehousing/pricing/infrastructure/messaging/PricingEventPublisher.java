package com.gogidix.shared.warehousing.pricing.infrastructure.messaging;

import com.gogidix.shared.warehousing.pricing.domain.events.PriceCalculatedEvent;
import com.gogidix.shared.warehousing.pricing.domain.events.PricingRuleCreatedEvent;
import com.gogidix.shared.warehousing.pricing.domain.events.PricingRuleUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Pricing Event Publisher
 *
 * Publishes pricing-related domain events to Kafka
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PricingEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String PRICING_RULE_CREATED_TOPIC = "pricing-rule-created";
    private static final String PRICING_RULE_UPDATED_TOPIC = "pricing-rule-updated";
    private static final String PRICE_CALCULATED_TOPIC = "price-calculated";

    /**
     * Publish pricing rule created event
     */
    public void publishPricingRuleCreated(PricingRuleCreatedEvent event) {
        try {
            kafkaTemplate.send(PRICING_RULE_CREATED_TOPIC, event.getPricingRuleId(), event);
            log.info("Published pricing rule created event: {}", event.getPricingRuleId());
        } catch (Exception e) {
            log.error("Failed to publish pricing rule created event", e);
        }
    }

    /**
     * Publish pricing rule updated event
     */
    public void publishPricingRuleUpdated(PricingRuleUpdatedEvent event) {
        try {
            kafkaTemplate.send(PRICING_RULE_UPDATED_TOPIC, event.getPricingRuleId(), event);
            log.info("Published pricing rule updated event: {}", event.getPricingRuleId());
        } catch (Exception e) {
            log.error("Failed to publish pricing rule updated event", e);
        }
    }

    /**
     * Publish price calculated event
     */
    public void publishPriceCalculated(PriceCalculatedEvent event) {
        try {
            kafkaTemplate.send(PRICE_CALCULATED_TOPIC, event.getQuoteId(), event);
            log.info("Published price calculated event: {}", event.getQuoteNumber());
        } catch (Exception e) {
            log.error("Failed to publish price calculated event", e);
        }
    }
}
