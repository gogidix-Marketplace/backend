package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamSubscriberTest {

    @Test
    void constructor_setsFields() {
        StreamSubscriber sub = new StreamSubscriber("s1", "Test");
        assertEquals("s1", sub.getId());
        assertEquals("Test", sub.getName());
        assertFalse(sub.isActive());
        assertEquals(0, sub.getErrorCount());
    }

    @Test
    void constructor_nullId_throws() {
        assertThrows(NullPointerException.class, () -> new StreamSubscriber(null, "name"));
    }

    @Test
    void constructor_nullName_throws() {
        assertThrows(NullPointerException.class, () -> new StreamSubscriber("id", null));
    }

    @Test
    void onSubscribe_activates() {
        StreamSubscriber sub = new StreamSubscriber("s1", "Test");
        sub.onSubscribe(null);
        assertTrue(sub.isActive());
    }

    @Test
    void onUnsubscribe_deactivates() {
        StreamSubscriber sub = new StreamSubscriber("s1", "Test");
        sub.onSubscribe(null);
        sub.onUnsubscribe(null);
        assertFalse(sub.isActive());
    }

    @Test
    void onError_incrementsErrorCount() {
        StreamSubscriber sub = new StreamSubscriber("s1", "Test");
        sub.onError(new RuntimeException("test"));
        assertEquals(1, sub.getErrorCount());
    }

    @Test
    void incrementErrorCount_increments() {
        StreamSubscriber sub = new StreamSubscriber("s1", "Test");
        sub.incrementErrorCount();
        sub.incrementErrorCount();
        assertEquals(2, sub.getErrorCount());
    }

    @Test
    void onStreamStop_deactivates() {
        StreamSubscriber sub = new StreamSubscriber("s1", "Test");
        sub.onSubscribe(null);
        sub.onStreamStop(null);
        assertFalse(sub.isActive());
    }

    @Test
    void equals_sameId_equal() {
        assertEquals(new StreamSubscriber("s1", "A"), new StreamSubscriber("s1", "B"));
    }

    @Test
    void hashCode_sameId_sameHash() {
        assertEquals(new StreamSubscriber("s1", "A").hashCode(), new StreamSubscriber("s1", "B").hashCode());
    }

    @Test
    void toString_containsId() {
        assertTrue(new StreamSubscriber("s1", "Test").toString().contains("s1"));
    }
}
