package com.gogidix.dashboard.core.infrastructure.messaging.kafka.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.dashboard.core.domain.model.DashboardKPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka event publisher for KPI events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KPIEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String KPI_EVENTS_TOPIC = "dashboard.kpi.events";

    /**
     * Publish KPI created event
     */
    public void publishKPICreated(DashboardKPI kpi) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "KPI_CREATED");
        event.put("kpiId", kpi.getId().toString());
        event.put("code", kpi.getCode());
        event.put("name", kpi.getName());
        event.put("tenantId", kpi.getTenantId());
        event.put("sourceDomain", kpi.getSourceDomain().name());
        event.put("timestamp", LocalDateTime.now().toString());

        publishEvent(kpi.getCode(), event);
        log.info("Published KPI created event: code={}, tenant={}", kpi.getCode(), kpi.getTenantId());
    }

    /**
     * Publish KPI updated event
     */
    public void publishKPIUpdated(DashboardKPI kpi) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "KPI_UPDATED");
        event.put("kpiId", kpi.getId().toString());
        event.put("code", kpi.getCode());
        event.put("name", kpi.getName());
        event.put("tenantId", kpi.getTenantId());
        event.put("sourceDomain", kpi.getSourceDomain().name());
        event.put("timestamp", LocalDateTime.now().toString());

        publishEvent(kpi.getCode(), event);
        log.info("Published KPI updated event: code={}, tenant={}", kpi.getCode(), kpi.getTenantId());
    }

    /**
     * Publish KPI deleted event
     */
    public void publishKPIDeleted(DashboardKPI kpi) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "KPI_DELETED");
        event.put("kpiId", kpi.getId().toString());
        event.put("code", kpi.getCode());
        event.put("name", kpi.getName());
        event.put("tenantId", kpi.getTenantId());
        event.put("timestamp", LocalDateTime.now().toString());

        publishEvent(kpi.getCode(), event);
        log.info("Published KPI deleted event: code={}, tenant={}", kpi.getCode(), kpi.getTenantId());
    }

    /**
     * Publish KPI value updated event
     */
    public void publishKPIValueUpdated(DashboardKPI kpi, Double value) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "KPI_VALUE_UPDATED");
        event.put("kpiId", kpi.getId().toString());
        event.put("code", kpi.getCode());
        event.put("name", kpi.getName());
        event.put("tenantId", kpi.getTenantId());
        event.put("value", value);
        event.put("previousValue", kpi.getPreviousValue());
        event.put("trend", kpi.getTrend());
        event.put("timestamp", LocalDateTime.now().toString());

        publishEvent(kpi.getCode(), event);
        log.info("Published KPI value updated event: code={}, value={}", kpi.getCode(), value);
    }

    /**
     * Publish KPI calculated event
     */
    public void publishKPICalculated(DashboardKPI kpi, Double value) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "KPI_CALCULATED");
        event.put("kpiId", kpi.getId().toString());
        event.put("code", kpi.getCode());
        event.put("name", kpi.getName());
        event.put("tenantId", kpi.getTenantId());
        event.put("value", value);
        event.put("timestamp", LocalDateTime.now().toString());

        publishEvent(kpi.getCode(), event);
        log.info("Published KPI calculated event: code={}, value={}", kpi.getCode(), value);
    }

    private void publishEvent(String key, Map<String, Object> event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(KPI_EVENTS_TOPIC, key, json);
        } catch (Exception e) {
            log.error("Failed to publish KPI event: {}", event, e);
        }
    }
}
