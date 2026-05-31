package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamConsumerTest {

    @Test
    void constructor_setsFields() {
        StreamConsumer c = new StreamConsumer("c1", "group1");
        assertEquals("c1", c.getConsumerId());
        assertEquals("group1", c.getConsumerGroup());
        assertFalse(c.isActive());
    }

    @Test
    void start_activates() {
        StreamConsumer c = new StreamConsumer("c1", "g1");
        c.start();
        assertTrue(c.isActive());
        assertTrue(c.isHealthy());
    }

    @Test
    void stop_deactivates() {
        StreamConsumer c = new StreamConsumer("c1", "g1");
        c.start();
        c.stop();
        assertFalse(c.isActive());
        assertFalse(c.isHealthy());
    }

    @Test
    void consume_doesNotThrow() {
        StreamConsumer c = new StreamConsumer("c1", "g1");
        StreamMessage msg = StreamMessage.builder()
                .withStreamId(StreamId.generate())
                .withTimestamp(java.time.LocalDateTime.now())
                .build();
        assertDoesNotThrow(() -> c.consume(msg));
    }
}
