package com.gogidix.sales.countrydashboard.infrastructure.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.sales.countrydashboard.domain.event.*;
import com.gogidix.sales.countrydashboard.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher Implementation
 * Publishes domain events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.dashboard-events:country-sales-dashboard-service.events}")
    private String dashboardEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.country.domain-events}")
    private String domainEventsTopic;

    @Value("${spring.kafka.topics.metrics-updated:sales.country.metrics-updated}")
    private String metricsUpdatedTopic;

    @Value("${spring.kafka.topics.quota-updated:sales.country.quota-updated}")
    private String quotaUpdatedTopic;

    @Override
    public void publishDashboardCreatedEvent(CountryDashboardCreatedEvent event) {
        log.debug("Publishing dashboard created event: {}", event.getDashboardId());
        publishEvent(dashboardEventsTopic, "DASHBOARD_CREATED", event);
    }

    @Override
    public void publishMetricUpdatedEvent(CountryMetricUpdatedEvent event) {
        log.debug("Publishing metric updated event: {} for country: {}",
                event.getDashboardId(), event.getCountryCode());
        publishEvent(metricsUpdatedTopic, "METRIC_UPDATED", event);
    }

    @Override
    public void publishTerritoryPerformanceUpdatedEvent(TerritoryPerformanceUpdatedEvent event) {
        log.debug("Publishing territory performance updated event: {} for territory: {}",
                event.getDashboardId(), event.getTerritoryId());
        publishEvent(metricsUpdatedTopic, "TERRITORY_PERFORMANCE_UPDATED", event);
    }

    @Override
    public void publishQuotaAdjustedEvent(CountryQuotaAdjustedEvent event) {
        log.debug("Publishing quota adjusted event: {} for country: {}",
                event.getDashboardId(), event.getCountryCode());
        publishEvent(quotaUpdatedTopic, "QUOTA_ADJUSTED", event);
    }

    @Override
    public void publishComparisonGeneratedEvent(CountryComparisonGeneratedEvent event) {
        log.debug("Publishing comparison generated event: {}", event.getDashboardId());
        publishEvent(dashboardEventsTopic, "COMPARISON_GENERATED", event);
    }

    @Override
    public void publishCurrencyConversionEvent(CurrencyConversionAppliedEvent event) {
        log.debug("Publishing currency conversion event: {} from {} to {}",
                event.getDashboardId(), event.getFromCurrency(), event.getToCurrency());
        publishEvent(dashboardEventsTopic, "CURRENCY_CONVERSION", event);
    }

    @Override
    public boolean isReady() {
        return kafkaTemplate != null;
    }

    private void publishEvent(String topic, String eventType, Object event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            log.trace("Event payload: {}", eventJson);

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, eventType, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Successfully published event {} to topic {}",
                            eventType, topic);
                } else {
                    log.error("Failed to publish event {} to topic {}: {}",
                            eventType, topic, ex.getMessage(), ex);
                }
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}: {}", eventType, e.getMessage());
        }
    }
}
