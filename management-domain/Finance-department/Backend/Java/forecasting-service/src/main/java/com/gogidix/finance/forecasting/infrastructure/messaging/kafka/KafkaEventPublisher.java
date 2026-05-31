package com.gogidix.finance.forecasting.infrastructure.messaging.kafka;

import com.gogidix.finance.forecasting.domain.event.ForecastApprovedEvent;
import com.gogidix.finance.forecasting.domain.event.ForecastGeneratedEvent;
import com.gogidix.finance.forecasting.domain.port.out.EventPublisher;
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
 * Publishes forecast events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.forecast-events:forecasting-service.events}")
    private String forecastEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Value("${spring.kafka.topics.forecast-approved:forecasting-service.forecast-approved}")
    private String forecastApprovedTopic;

    @Value("${spring.kafka.topics.forecast-generated:forecasting-service.forecast-generated}")
    private String forecastGeneratedTopic;

    @Override
    public void publish(ForecastGeneratedEvent event) {
        try {
            String key = event.getEventKey();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(forecastGeneratedTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published forecast generated event: {} to topic: {}",
                            event.getEventId(), forecastGeneratedTopic);
                } else {
                    log.error("Failed to publish forecast generated event: {}",
                            event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing forecast generated event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(ForecastApprovedEvent event) {
        try {
            String key = event.getEventKey();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(forecastApprovedTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published forecast approved event: {} to topic: {}",
                            event.getEventId(), forecastApprovedTopic);
                } else {
                    log.error("Failed to publish forecast approved event: {}",
                            event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing forecast approved event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof ForecastGeneratedEvent generatedEvent) {
                publish(generatedEvent);
            } else if (event instanceof ForecastApprovedEvent approvedEvent) {
                publish(approvedEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            // Check Kafka health by attempting to get partition metadata
            kafkaTemplate.getProducerFactory().createProducer().partitionsFor(forecastEventsTopic);
            return true;
        } catch (Exception e) {
            log.warn("Kafka publisher not ready: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void publish(Object event, String topic) {
        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published event to topic: {}", topic);
                } else {
                    log.error("Failed to publish event to topic: {}", topic, ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing event to topic: {}", topic, e);
        }
    }

    @Override
    public String getStatus() {
        if (isReady()) {
            return "READY - Connected to Kafka at " + kafkaTemplate.getProducerFactory().getConfigurationProperties();
        }
        return "NOT_READY - Unable to connect to Kafka";
    }

    /**
     * Publishes event to domain events topic
     *
     * @param event the event to publish
     */
    public void publishToDomainEvents(Object event) {
        publish(event, domainEventsTopic);
    }

    /**
     * Publishes event to forecast events topic
     *
     * @param event the event to publish
     */
    public void publishToForecastEvents(Object event) {
        publish(event, forecastEventsTopic);
    }
}
