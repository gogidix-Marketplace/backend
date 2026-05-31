package com.gogidix.centralizeddashboard.analytics.aggregation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {

    @Test
    void allValuesAreAccessible() {
        EventType[] values = EventType.values();
        assertTrue(values.length > 0);
        assertNotNull(EventType.valueOf("PAGE_VIEW"));
        assertNotNull(EventType.valueOf("CUSTOM"));
    }

    @Test
    void allValuesHaveNames() {
        for (EventType et : EventType.values()) {
            assertNotNull(et.name());
            assertFalse(et.name().isEmpty());
        }
    }
}
