package com.gogidix.finance.cashflow.infrastructure.messaging.kafka;

import com.gogidix.finance.cashflow.domain.event.CashflowForecastGeneratedEvent;
import com.gogidix.finance.cashflow.domain.event.CashflowItemRecordedEvent;
import com.gogidix.finance.cashflow.domain.port.out.EventPublisher;
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
 * Publishes cashflow events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.cashflow-item-events:cashflow-service.items.events}")
    private String cashflowItemEventsTopic;

    @Value("${spring.kafka.topics.cashflow-forecast-events:cashflow-service.forecasts.events}")
    private String cashflowForecastEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(CashflowItemRecordedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getCashflowItemId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(cashflowItemEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published cashflow item event: {} to topic: {}",
                            event.getEventId(), cashflowItemEventsTopic);
                } else {
                    log.error("Failed to publish cashflow item event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing cashflow item event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(CashflowForecastGeneratedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getForecastId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(cashflowForecastEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published cashflow forecast event: {} to topic: {}",
                            event.getEventId(), cashflowForecastEventsTopic);
                } else {
                    log.error("Failed to publish cashflow forecast event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing cashflow forecast event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof CashflowItemRecordedEvent itemEvent) {
                publish(itemEvent);
            } else if (event instanceof CashflowForecastGeneratedEvent forecastEvent) {
                publish(forecastEvent);
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
