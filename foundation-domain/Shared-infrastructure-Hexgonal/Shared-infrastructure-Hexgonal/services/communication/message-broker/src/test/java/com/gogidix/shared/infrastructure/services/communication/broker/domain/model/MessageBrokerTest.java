package com.gogidix.shared.infrastructure.services.communication.broker.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MessageBroker domain model.
 */
@DisplayName("MessageBroker Domain Model Tests")
class MessageBrokerTest {

    @Test
    @DisplayName("Should create MessageBroker with constructor")
    void shouldCreateMessageBrokerWithConstructor() {
        MessageBroker broker = new MessageBroker("tenant123", "topic1", "EVENT", "payload");

        assertEquals("tenant123", broker.getTenantId());
        assertEquals("topic1", broker.getTopic());
        assertEquals("EVENT", broker.getMessageType());
        assertEquals("payload", broker.getPayload());
        assertEquals("PENDING", broker.getStatus());
        assertNotNull(broker.getCreatedAt());
        assertEquals(0, broker.getRetryCount());
    }

    @Test
    @DisplayName("Should create MessageBroker with no-args constructor")
    void shouldCreateMessageBrokerWithNoArgsConstructor() {
        MessageBroker broker = new MessageBroker();

        assertNotNull(broker);
        assertNull(broker.getTenantId());
        assertNull(broker.getTopic());
        assertNull(broker.getMessageType());
        assertNull(broker.getPayload());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> headers = Map.of("key", "value");

        MessageBroker broker = new MessageBroker();
        broker.setId("broker123");
        broker.setTenantId("tenant123");
        broker.setTopic("test-topic");
        broker.setMessageType("TEST_EVENT");
        broker.setPayload("test payload");
        broker.setHeaders(headers);
        broker.setStatus("PUBLISHED");
        broker.setCreatedAt(now);
        broker.setPublishedAt(now);
        broker.setErrorMessage("Error occurred");
        broker.setRetryCount(2);
        broker.setCorrelationId("corr123");
        broker.setReplyTo("reply-queue");

        assertEquals("broker123", broker.getId());
        assertEquals("tenant123", broker.getTenantId());
        assertEquals("test-topic", broker.getTopic());
        assertEquals("TEST_EVENT", broker.getMessageType());
        assertEquals("test payload", broker.getPayload());
        assertEquals(headers, broker.getHeaders());
        assertEquals("PUBLISHED", broker.getStatus());
        assertEquals(now, broker.getCreatedAt());
        assertEquals(now, broker.getPublishedAt());
        assertEquals("Error occurred", broker.getErrorMessage());
        assertEquals(2, broker.getRetryCount());
        assertEquals("corr123", broker.getCorrelationId());
        assertEquals("reply-queue", broker.getReplyTo());
    }

    @Test
    @DisplayName("Should mark broker message as published")
    void shouldMarkBrokerMessageAsPublished() {
        MessageBroker broker = new MessageBroker();
        broker.setStatus("PENDING");

        broker.markAsPublished();

        assertEquals("PUBLISHED", broker.getStatus());
        assertNotNull(broker.getPublishedAt());
    }

    @Test
    @DisplayName("Should mark broker message as failed")
    void shouldMarkBrokerMessageAsFailed() {
        MessageBroker broker = new MessageBroker();
        broker.setStatus("PENDING");

        broker.markAsFailed("Connection error");

        assertEquals("FAILED", broker.getStatus());
        assertEquals("Connection error", broker.getErrorMessage());
    }

    @Test
    @DisplayName("Should increment retry count")
    void shouldIncrementRetryCount() {
        MessageBroker broker = new MessageBroker();
        broker.setRetryCount(0);

        broker.incrementRetry();

        assertEquals(1, broker.getRetryCount());
    }

    @Test
    @DisplayName("Should check if message can be retried")
    void shouldCheckIfMessageCanBeRetried() {
        MessageBroker broker = new MessageBroker();
        broker.setStatus("FAILED");
        broker.setRetryCount(2);

        assertTrue(broker.canRetry(3));
        assertFalse(broker.canRetry(2));
        assertFalse(broker.canRetry(1));
    }

    @Test
    @DisplayName("Should not retry non-failed messages")
    void shouldNotRetryNonFailedMessages() {
        MessageBroker broker = new MessageBroker();
        broker.setStatus("PENDING");
        broker.setRetryCount(0);

        assertFalse(broker.canRetry(3));
    }

    @Test
    @DisplayName("Should handle headers")
    void shouldHandleHeaders() {
        MessageBroker broker = new MessageBroker();
        Map<String, Object> headers = Map.of(
                "contentType", "application/json",
                "timestamp", System.currentTimeMillis()
        );

        broker.setHeaders(headers);

        assertEquals(headers, broker.getHeaders());
        assertEquals("application/json", broker.getHeaders().get("contentType"));
    }

    @Test
    @DisplayName("Should handle correlation ID")
    void shouldHandleCorrelationId() {
        MessageBroker broker = new MessageBroker();
        broker.setCorrelationId("corr-abc-123");

        assertEquals("corr-abc-123", broker.getCorrelationId());
    }

    @Test
    @DisplayName("Should handle reply to queue")
    void shouldHandleReplyToQueue() {
        MessageBroker broker = new MessageBroker();
        broker.setReplyTo("reply-queue-name");

        assertEquals("reply-queue-name", broker.getReplyTo());
    }

    @Test
    @DisplayName("Should handle status transitions")
    void shouldHandleStatusTransitions() {
        MessageBroker broker = new MessageBroker();

        broker.setStatus("PENDING");
        assertEquals("PENDING", broker.getStatus());

        broker.markAsPublished();
        assertEquals("PUBLISHED", broker.getStatus());

        broker.markAsFailed("Error");
        assertEquals("FAILED", broker.getStatus());
    }

    @Test
    @DisplayName("Should track published at timestamp")
    void shouldTrackPublishedAtTimestamp() {
        MessageBroker broker = new MessageBroker();
        broker.setStatus("PENDING");

        assertNull(broker.getPublishedAt());

        broker.markAsPublished();

        assertNotNull(broker.getPublishedAt());
    }

    @Test
    @DisplayName("Should handle retry tracking across multiple attempts")
    void shouldHandleRetryTrackingAcrossMultipleAttempts() {
        MessageBroker broker = new MessageBroker();
        broker.setStatus("FAILED");
        broker.setRetryCount(0);

        for (int i = 0; i < 5; i++) {
            assertTrue(broker.canRetry(10), "Should be retryable at attempt " + i);
            broker.incrementRetry();
        }

        assertEquals(5, broker.getRetryCount());
    }
}
