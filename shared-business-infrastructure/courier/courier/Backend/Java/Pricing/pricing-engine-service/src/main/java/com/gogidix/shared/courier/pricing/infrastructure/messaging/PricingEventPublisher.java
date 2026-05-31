package com.gogidix.shared.courier.pricing.infrastructure.messaging;

import com.gogidix.shared.courier.pricing.domain.events.PriceCalculatedEvent;
import com.gogidix.shared.courier.pricing.domain.events.PricingRuleCreatedEvent;
import com.gogidix.shared.courier.pricing.domain.events.PricingRuleDeletedEvent;
import com.gogidix.shared.courier.pricing.domain.events.PricingRuleUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publisher for pricing-related domain events
 * Publishes events to Kafka topics for downstream processing
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PricingEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String PRICE_CALCULATED_TOPIC = "pricing.price.calculated";
    private static final String RULE_CREATED_TOPIC = "pricing.rule.created";
    private static final String RULE_UPDATED_TOPIC = "pricing.rule.updated";
    private static final String RULE_DELETED_TOPIC = "pricing.rule.deleted";

    public void publishPriceCalculated(PriceCalculatedEvent event) {
        try {
            kafkaTemplate.send(PRICE_CALCULATED_TOPIC, event.getTenantId(), event);
            log.info("Published PriceCalculatedEvent for quote: {}", event.getQuoteId());
        } catch (Exception e) {
            log.error("Failed to publish PriceCalculatedEvent for quote: {}", event.getQuoteId(), e);
        }
    }

    public void publishRuleCreated(PricingRuleCreatedEvent event) {
        try {
            kafkaTemplate.send(RULE_CREATED_TOPIC, event.getTenantId(), event);
            log.info("Published PricingRuleCreatedEvent for rule: {}", event.getRuleId());
        } catch (Exception e) {
            log.error("Failed to publish PricingRuleCreatedEvent for rule: {}", event.getRuleId(), e);
        }
    }

    public void publishRuleUpdated(PricingRuleUpdatedEvent event) {
        try {
            kafkaTemplate.send(RULE_UPDATED_TOPIC, event.getTenantId(), event);
            log.info("Published PricingRuleUpdatedEvent for rule: {}", event.getRuleId());
        } catch (Exception e) {
            log.error("Failed to publish PricingRuleUpdatedEvent for rule: {}", event.getRuleId(), e);
        }
    }

    public void publishRuleDeleted(PricingRuleDeletedEvent event) {
        try {
            kafkaTemplate.send(RULE_DELETED_TOPIC, event.getTenantId(), event);
            log.info("Published PricingRuleDeletedEvent for rule: {}", event.getRuleId());
        } catch (Exception e) {
            log.error("Failed to publish PricingRuleDeletedEvent for rule: {}", event.getRuleId(), e);
        }
    }
}
