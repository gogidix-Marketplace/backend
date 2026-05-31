package com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EventBridge domain model.
 */
@DisplayName("EventBridge Domain Model Tests")
class EventBridgeTest {

    @Test
    @DisplayName("Should create EventBridge with constructor")
    void shouldCreateEventBridgeWithConstructor() {
        TenantId tenantId = new TenantId("tenant123");
        EventBridge bridge = new EventBridge(tenantId, "bridge1", "KAFKA", "RABBITMQ");

        assertEquals(tenantId, bridge.getTenantId());
        assertEquals("bridge1", bridge.getName());
        assertEquals("KAFKA", bridge.getSourceType());
        assertEquals("RABBITMQ", bridge.getTargetType());
        assertTrue(bridge.isEnabled());
        assertEquals(0, bridge.getRetryCount());
        assertEquals(3, bridge.getMaxRetries());
        assertEquals(0, bridge.getMessageCount());
        assertEquals(0, bridge.getErrorCount());
        assertEquals("ACTIVE", bridge.getStatus());
    }

    @Test
    @DisplayName("Should create EventBridge with no-args constructor")
    void shouldCreateEventBridgeWithNoArgsConstructor() {
        EventBridge bridge = new EventBridge();

        assertNotNull(bridge);
        assertNull(bridge.getTenantId());
        assertNull(bridge.getName());
        assertNull(bridge.getSourceType());
        assertNull(bridge.getTargetType());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> transformationRules = Map.of("key", "value");

        EventBridge bridge = new EventBridge();
        bridge.setTenantId(tenantId);
        bridge.setId("bridge123");
        bridge.setName("test-bridge");
        bridge.setSourceType("KAFKA");
        bridge.setTargetType("RABBITMQ");
        bridge.setSourceTopic("source-topic");
        bridge.setTargetTopic("target-topic");
        bridge.setSourceExchange("source-exchange");
        bridge.setTargetExchange("target-exchange");
        bridge.setSourceQueue("source-queue");
        bridge.setTargetQueue("target-queue");
        bridge.setEnabled(true);
        bridge.setFilterExpression("eventType = 'TEST'");
        bridge.setTransformationRules(transformationRules);
        bridge.setRetryCount(1);
        bridge.setMaxRetries(5);
        bridge.setMessageCount(100);
        bridge.setErrorCount(2);
        bridge.setStatus("ACTIVE");
        bridge.setCreatedAt(now);
        bridge.setUpdatedAt(now);

        assertEquals(tenantId, bridge.getTenantId());
        assertEquals("bridge123", bridge.getId());
        assertEquals("test-bridge", bridge.getName());
        assertEquals("KAFKA", bridge.getSourceType());
        assertEquals("RABBITMQ", bridge.getTargetType());
        assertEquals("source-topic", bridge.getSourceTopic());
        assertEquals("target-topic", bridge.getTargetTopic());
        assertEquals("source-exchange", bridge.getSourceExchange());
        assertEquals("target-exchange", bridge.getTargetExchange());
        assertEquals("source-queue", bridge.getSourceQueue());
        assertEquals("target-queue", bridge.getTargetQueue());
        assertTrue(bridge.isEnabled());
        assertEquals("eventType = 'TEST'", bridge.getFilterExpression());
        assertEquals(transformationRules, bridge.getTransformationRules());
        assertEquals(1, bridge.getRetryCount());
        assertEquals(5, bridge.getMaxRetries());
        assertEquals(100, bridge.getMessageCount());
        assertEquals(2, bridge.getErrorCount());
        assertEquals("ACTIVE", bridge.getStatus());
        assertEquals(now, bridge.getCreatedAt());
        assertEquals(now, bridge.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle enabled status")
    void shouldHandleEnabledStatus() {
        EventBridge bridge = new EventBridge();
        bridge.setEnabled(true);

        assertTrue(bridge.isEnabled());

        bridge.setEnabled(false);
        assertFalse(bridge.isEnabled());
    }

    @Test
    @DisplayName("Should update status correctly")
    void shouldUpdateStatusCorrectly() {
        EventBridge bridge = new EventBridge();
        bridge.setStatus("ACTIVE");

        assertEquals("ACTIVE", bridge.getStatus());

        bridge.setStatus("INACTIVE");
        assertEquals("INACTIVE", bridge.getStatus());

        bridge.setStatus("ERROR");
        assertEquals("ERROR", bridge.getStatus());
    }

    @Test
    @DisplayName("Should handle retry count")
    void shouldHandleRetryCount() {
        EventBridge bridge = new EventBridge();
        bridge.setRetryCount(0);
        assertEquals(0, bridge.getRetryCount());

        bridge.setRetryCount(5);
        assertEquals(5, bridge.getRetryCount());
    }

    @Test
    @DisplayName("Should handle message and error counts")
    void shouldHandleMessageAndErrorCounts() {
        EventBridge bridge = new EventBridge();
        bridge.setMessageCount(1000);
        bridge.setErrorCount(10);

        assertEquals(1000, bridge.getMessageCount());
        assertEquals(10, bridge.getErrorCount());
    }

    @Test
    @DisplayName("Should handle filter expression")
    void shouldHandleFilterExpression() {
        EventBridge bridge = new EventBridge();
        bridge.setFilterExpression("eventType = 'TEST' AND source = 'API'");

        assertEquals("eventType = 'TEST' AND source = 'API'", bridge.getFilterExpression());
    }

    @Test
    @DisplayName("Should handle transformation rules")
    void shouldHandleTransformationRules() {
        EventBridge bridge = new EventBridge();
        Map<String, Object> rules = Map.of(
                "addTimestamp", true,
                "transformFields", List.of("field1", "field2")
        );

        bridge.setTransformationRules(rules);

        assertEquals(rules, bridge.getTransformationRules());
    }

    @Test
    @DisplayName("Should handle all topic and exchange fields")
    void shouldHandleAllTopicAndExchangeFields() {
        EventBridge bridge = new EventBridge();
        bridge.setSourceTopic("input-topic");
        bridge.setTargetTopic("output-topic");
        bridge.setSourceExchange("input-exchange");
        bridge.setTargetExchange("output-exchange");
        bridge.setSourceQueue("input-queue");
        bridge.setTargetQueue("output-queue");

        assertEquals("input-topic", bridge.getSourceTopic());
        assertEquals("output-topic", bridge.getTargetTopic());
        assertEquals("input-exchange", bridge.getSourceExchange());
        assertEquals("output-exchange", bridge.getTargetExchange());
        assertEquals("input-queue", bridge.getSourceQueue());
        assertEquals("output-queue", bridge.getTargetQueue());
    }
}
