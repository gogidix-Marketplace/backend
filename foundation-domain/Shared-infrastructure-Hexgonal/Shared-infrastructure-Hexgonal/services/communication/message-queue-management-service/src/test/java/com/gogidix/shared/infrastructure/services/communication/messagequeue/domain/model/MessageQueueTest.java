package com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MessageQueue domain model.
 */
@DisplayName("MessageQueue Domain Model Tests")
class MessageQueueTest {

    @Test
    @DisplayName("Should create MessageQueue with constructor")
    void shouldCreateMessageQueueWithConstructor() {
        TenantId tenantId = new TenantId("tenant123");
        MessageQueue queue = new MessageQueue(tenantId, "queue1", "STANDARD");

        assertEquals(tenantId, queue.getTenantId());
        assertEquals("queue1", queue.getName());
        assertEquals("STANDARD", queue.getType());
        assertEquals("ACTIVE", queue.getStatus());
        assertEquals(30L, queue.getVisibilityTimeout());
        assertEquals(0, queue.getDeliveryDelay());
        assertEquals(3, queue.getMaxReceiveCount());
    }

    @Test
    @DisplayName("Should create MessageQueue with no-args constructor")
    void shouldCreateMessageQueueWithNoArgsConstructor() {
        MessageQueue queue = new MessageQueue();

        assertNotNull(queue);
        assertNull(queue.getTenantId());
        assertNull(queue.getName());
        assertNull(queue.getType());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        LocalDateTime now = LocalDateTime.now();

        MessageQueue queue = new MessageQueue();
        queue.setTenantId(tenantId);
        queue.setId("queue123");
        queue.setName("test-queue");
        queue.setDescription("Test queue description");
        queue.setType("FIFO");
        queue.setStatus("ACTIVE");
        queue.setRegion("us-east-1");
        queue.setMaxSize(1024L);
        queue.setMessageRetentionPeriod(86400L);
        queue.setMaxReceiveCount(5);
        queue.setVisibilityTimeout(60L);
        queue.setDeliveryDelay(10);
        queue.setCreatedAt(now);
        queue.setUpdatedAt(now);

        assertEquals(tenantId, queue.getTenantId());
        assertEquals("queue123", queue.getId());
        assertEquals("test-queue", queue.getName());
        assertEquals("Test queue description", queue.getDescription());
        assertEquals("FIFO", queue.getType());
        assertEquals("ACTIVE", queue.getStatus());
        assertEquals("us-east-1", queue.getRegion());
        assertEquals(1024L, queue.getMaxSize());
        assertEquals(86400L, queue.getMessageRetentionPeriod());
        assertEquals(5, queue.getMaxReceiveCount());
        assertEquals(60L, queue.getVisibilityTimeout());
        assertEquals(10, queue.getDeliveryDelay());
        assertEquals(now, queue.getCreatedAt());
        assertEquals(now, queue.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle different queue types")
    void shouldHandleDifferentQueueTypes() {
        String[] types = {"FIFO", "STANDARD", "DEAD_LETTER"};

        for (String type : types) {
            TenantId tenantId = new TenantId("tenant123");
            MessageQueue queue = new MessageQueue(tenantId, "queue", type);

            assertEquals(type, queue.getType());
        }
    }

    @Test
    @DisplayName("Should handle different queue statuses")
    void shouldHandleDifferentQueueStatuses() {
        String[] statuses = {"ACTIVE", "INACTIVE", "DELETING"};

        for (String status : statuses) {
            MessageQueue queue = new MessageQueue();
            queue.setStatus(status);

            assertEquals(status, queue.getStatus());
        }
    }

    @Test
    @DisplayName("Should handle visibility timeout")
    void shouldHandleVisibilityTimeout() {
        MessageQueue queue = new MessageQueue();
        queue.setVisibilityTimeout(30L);

        assertEquals(30L, queue.getVisibilityTimeout());

        queue.setVisibilityTimeout(120L);
        assertEquals(120L, queue.getVisibilityTimeout());
    }

    @Test
    @DisplayName("Should handle delivery delay")
    void shouldHandleDeliveryDelay() {
        MessageQueue queue = new MessageQueue();
        queue.setDeliveryDelay(0);

        assertEquals(0, queue.getDeliveryDelay());

        queue.setDeliveryDelay(60);
        assertEquals(60, queue.getDeliveryDelay());
    }

    @Test
    @DisplayName("Should handle max receive count")
    void shouldHandleMaxReceiveCount() {
        MessageQueue queue = new MessageQueue();
        queue.setMaxReceiveCount(3);

        assertEquals(3, queue.getMaxReceiveCount());

        queue.setMaxReceiveCount(10);
        assertEquals(10, queue.getMaxReceiveCount());
    }

    @Test
    @DisplayName("Should handle max size")
    void shouldHandleMaxSize() {
        MessageQueue queue = new MessageQueue();
        queue.setMaxSize(2048L);

        assertEquals(2048L, queue.getMaxSize());
    }

    @Test
    @DisplayName("Should handle message retention period")
    void shouldHandleMessageRetentionPeriod() {
        MessageQueue queue = new MessageQueue();
        queue.setMessageRetentionPeriod(3600L);

        assertEquals(3600L, queue.getMessageRetentionPeriod());
    }

    @Test
    @DisplayName("Should handle region")
    void shouldHandleRegion() {
        MessageQueue queue = new MessageQueue();
        queue.setRegion("us-west-2");

        assertEquals("us-west-2", queue.getRegion());
    }

    @Test
    @DisplayName("Should handle description")
    void shouldHandleDescription() {
        MessageQueue queue = new MessageQueue();
        queue.setDescription("This is a test queue");

        assertEquals("This is a test queue", queue.getDescription());
    }
}
