package com.gogidix.finance.budgettracking.infrastructure.messaging.kafka;

import com.gogidix.finance.budgettracking.domain.event.BudgetThresholdExceededEvent;
import com.gogidix.finance.budgettracking.domain.event.BudgetTransactionRecordedEvent;
import com.gogidix.finance.budgettracking.domain.event.BudgetVarianceEvent;
import com.gogidix.finance.budgettracking.domain.port.out.EventPublisher;
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
 * Publishes budget tracking events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.budget-tracking-events:budget-tracking-service.events}")
    private String budgetTrackingEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(BudgetTransactionRecordedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getTransactionId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(budgetTrackingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published budget transaction event: {} to topic: {}",
                        event.getEventId(), budgetTrackingEventsTopic);
                } else {
                    log.error("Failed to publish budget transaction event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing budget transaction event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(BudgetThresholdExceededEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getMonitorId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(budgetTrackingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published threshold exceeded event: {} to topic: {}",
                        event.getEventId(), budgetTrackingEventsTopic);
                } else {
                    log.error("Failed to publish threshold exceeded event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing threshold exceeded event: {}", event.getEventId(), e);
        }
    }

    public void publish(BudgetVarianceEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getVarianceId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(budgetTrackingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published budget variance event: {} to topic: {}",
                        event.getEventId(), budgetTrackingEventsTopic);
                } else {
                    log.error("Failed to publish budget variance event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing budget variance event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof BudgetTransactionRecordedEvent transactionEvent) {
                publish(transactionEvent);
            } else if (event instanceof BudgetThresholdExceededEvent thresholdEvent) {
                publish(thresholdEvent);
            } else if (event instanceof BudgetVarianceEvent varianceEvent) {
                publish(varianceEvent);
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
