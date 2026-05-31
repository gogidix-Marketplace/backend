package com.gogidix.finance.tax.infrastructure.messaging.kafka;

import com.gogidix.finance.tax.domain.event.TaxCalculationCompletedEvent;
import com.gogidix.finance.tax.domain.event.TaxFilingSubmittedEvent;
import com.gogidix.finance.tax.domain.event.TaxRateUpdatedEvent;
import com.gogidix.finance.tax.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher
 * Publishes tax events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.tax-events:tax-service.events}")
    private String taxEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(TaxRateUpdatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getTaxRateId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(taxEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published tax rate event: {} to topic: {}",
                        event.getEventId(), taxEventsTopic);
                } else {
                    log.error("Failed to publish tax rate event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing tax rate event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(TaxCalculationCompletedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getCalculationId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(taxEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published tax calculation event: {} to topic: {}",
                        event.getEventId(), taxEventsTopic);
                } else {
                    log.error("Failed to publish tax calculation event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing tax calculation event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(TaxFilingSubmittedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getFilingId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(taxEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published tax filing event: {} to topic: {}",
                        event.getEventId(), taxEventsTopic);
                } else {
                    log.error("Failed to publish tax filing event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing tax filing event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof TaxRateUpdatedEvent rateEvent) {
                publish(rateEvent);
            } else if (event instanceof TaxCalculationCompletedEvent calcEvent) {
                publish(calcEvent);
            } else if (event instanceof TaxFilingSubmittedEvent filingEvent) {
                publish(filingEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            // Kafka health check would go here
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
