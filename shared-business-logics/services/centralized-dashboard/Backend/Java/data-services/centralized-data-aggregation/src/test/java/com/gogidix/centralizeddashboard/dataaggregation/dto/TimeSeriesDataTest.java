package com.gogidix.centralizeddashboard.dataaggregation.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TimeSeriesDataTest {

    @Test
    void noArgsConstructor_createsInstance() {
        assertNotNull(new TimeSeriesData());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        List<String> services = List.of("svc1", "svc2");
        Map<String, Object> data = Map.of("k", "v");
        TimeSeriesData tsd = new TimeSeriesData("metric", "2024-01-01", "2024-01-31", "daily", services, data);
        assertEquals("metric", tsd.getMetricName());
        assertEquals("2024-01-01", tsd.getStartDate());
        assertEquals("2024-01-31", tsd.getEndDate());
        assertEquals("daily", tsd.getInterval());
        assertEquals(services, tsd.getServices());
        assertEquals(data, tsd.getData());
    }

    @Test
    void setters_workCorrectly() {
        TimeSeriesData tsd = new TimeSeriesData();
        tsd.setMetricName("m");
        tsd.setStartDate("s");
        tsd.setEndDate("e");
        tsd.setInterval("i");
        tsd.setServices(List.of("s1"));
        tsd.setData(Map.of("k", 1));
        assertEquals("m", tsd.getMetricName());
        assertEquals("s", tsd.getStartDate());
        assertEquals("e", tsd.getEndDate());
        assertEquals("i", tsd.getInterval());
    }

    @Test
    void equals_sameValues_returnsTrue() {
        List<String> s = List.of("a");
        Map<String, Object> d = Map.of("k", "v");
        TimeSeriesData t1 = new TimeSeriesData("m", "s", "e", "i", s, d);
        TimeSeriesData t2 = new TimeSeriesData("m", "s", "e", "i", s, d);
        assertEquals(t1, t2);
    }

    @Test
    void equals_differentValues_returnsFalse() {
        TimeSeriesData t1 = new TimeSeriesData("m1", "s", "e", "i", null, null);
        TimeSeriesData t2 = new TimeSeriesData("m2", "s", "e", "i", null, null);
        assertNotEquals(t1, t2);
    }

    @Test
    void equals_null_returnsFalse() {
        TimeSeriesData t1 = new TimeSeriesData("m", "s", "e", "i", null, null);
        assertNotEquals(null, t1);
    }

    @Test
    void equals_sameInstance_returnsTrue() {
        TimeSeriesData t1 = new TimeSeriesData();
        assertEquals(t1, t1);
    }

    @Test
    void hashCode_sameValues_sameHashCode() {
        TimeSeriesData t1 = new TimeSeriesData("m", "s", "e", "i", null, null);
        TimeSeriesData t2 = new TimeSeriesData("m", "s", "e", "i", null, null);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void toString_containsFields() {
        TimeSeriesData t1 = new TimeSeriesData("metric1", "s", "e", "i", null, null);
        String str = t1.toString();
        assertTrue(str.contains("metric1"));
    }
}
