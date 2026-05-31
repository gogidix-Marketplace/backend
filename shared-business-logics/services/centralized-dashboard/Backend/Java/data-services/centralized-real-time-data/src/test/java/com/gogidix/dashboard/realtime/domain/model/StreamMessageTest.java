package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StreamMessageTest {

    @Test
    void builder_createsMessage() {
        StreamId id = StreamId.generate();
        Map<String, Object> data = Map.of("key", "value", "count", 42);
        LocalDateTime ts = LocalDateTime.now();

        StreamMessage msg = StreamMessage.builder()
                .withStreamId(id)
                .withData(data)
                .withTimestamp(ts)
                .withSequenceNumber(1)
                .build();

        assertEquals(id, msg.getStreamId());
        assertEquals(data, msg.getData());
        assertEquals(ts, msg.getTimestamp());
        assertEquals(1, msg.getSequenceNumber());
        assertTrue(msg.getPayloadSize() > 0);
    }

    @Test
    void builder_nullStreamId_throws() {
        assertThrows(NullPointerException.class, () ->
            StreamMessage.builder()
                .withStreamId(null)
                .withTimestamp(LocalDateTime.now())
                .build());
    }

    @Test
    void builder_defaultData_isEmptyMap() {
        StreamMessage msg = StreamMessage.builder()
                .withStreamId(StreamId.generate())
                .withTimestamp(LocalDateTime.now())
                .build();
        assertTrue(msg.getData().isEmpty());
    }
}
