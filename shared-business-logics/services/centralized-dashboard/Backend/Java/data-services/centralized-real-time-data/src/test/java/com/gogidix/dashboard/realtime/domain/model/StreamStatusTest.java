package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamStatusTest {

    @Test
    void allValuesHaveDescriptions() {
        for (StreamStatus status : StreamStatus.values()) {
            assertNotNull(status.getDescription());
        }
    }

    @Test
    void isActive_onlyForActiveStatus() {
        assertTrue(StreamStatus.ACTIVE.isActive());
        assertFalse(StreamStatus.CREATED.isActive());
        assertFalse(StreamStatus.STOPPED.isActive());
    }

    @Test
    void canStart_forValidTransitions() {
        assertTrue(StreamStatus.CREATED.canStart());
        assertTrue(StreamStatus.STOPPED.canStart());
        assertTrue(StreamStatus.PAUSED.canStart());
        assertFalse(StreamStatus.ACTIVE.canStart());
    }

    @Test
    void canStop_forValidTransitions() {
        assertTrue(StreamStatus.ACTIVE.canStop());
        assertTrue(StreamStatus.PAUSED.canStop());
        assertFalse(StreamStatus.STOPPED.canStop());
        assertFalse(StreamStatus.CREATED.canStop());
    }
}
