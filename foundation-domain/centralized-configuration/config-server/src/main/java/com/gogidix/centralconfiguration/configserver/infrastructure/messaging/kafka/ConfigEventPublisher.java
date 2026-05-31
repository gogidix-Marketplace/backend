package com.gogidix.centralconfiguration.configserver.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka event publisher for configuration changes.
 * Publishes configuration lifecycle events to the messaging system.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String CONFIG_EVENTS_TOPIC = "config-events";

    public void publishConfigCreated(Configuration config) {
        publishEvent("CONFIG_CREATED", config, null, config.getConfigValue());
    }

    public void publishConfigUpdated(Configuration config, String oldValue) {
        publishEvent("CONFIG_UPDATED", config, oldValue, config.getConfigValue());
    }

    public void publishConfigDeleted(Configuration config) {
        publishEvent("CONFIG_DELETED", config, config.getConfigValue(), null);
    }

    private void publishEvent(String eventType, Configuration config, String oldValue, String newValue) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("eventType", eventType);
            payload.put("tenantId", config.getTenantId());
            payload.put("applicationName", config.getApplicationName());
            payload.put("profile", config.getProfile());
            payload.put("configKey", config.getConfigKey());
            payload.put("oldValue", oldValue);
            payload.put("newValue", newValue);
            payload.put("version", config.getVersion());
            payload.put("isEncrypted", config.getIsEncrypted());
            payload.put("timestamp", LocalDateTime.now().toString());

            String jsonPayload = objectMapper.writeValueAsString(payload);

            kafkaTemplate.send(CONFIG_EVENTS_TOPIC, config.getTenantId() + "-" + config.getConfigKey(), jsonPayload);

            log.debug("Published {} event for config key: {}", eventType, config.getConfigKey());
        } catch (Exception e) {
            log.error("Failed to publish {} event for config: {}", eventType, config.getConfigKey(), e);
        }
    }
}
