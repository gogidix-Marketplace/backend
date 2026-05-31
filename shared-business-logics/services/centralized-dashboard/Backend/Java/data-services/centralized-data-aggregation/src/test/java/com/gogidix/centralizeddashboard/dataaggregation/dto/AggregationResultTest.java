package com.gogidix.centralizeddashboard.dataaggregation.dto;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AggregationResultTest {

    @Test
    void noArgsConstructor_createsInstance() {
        assertNotNull(new AggregationResult());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        List<DataSourceMetadata> ds = List.of(new DataSourceMetadata("svc", "/ep", true, "m", "NUMERIC"));
        Map<String, Object> data = Map.of("k", "v");
        AggregationResult ar = new AggregationResult("id1", "ts", ds, "2024-01-01", "2024-01-31", data);
        assertEquals("id1", ar.getId());
        assertEquals("ts", ar.getTimestamp());
        assertEquals(ds, ar.getDataSources());
        assertEquals("2024-01-01", ar.getStartDate());
        assertEquals("2024-01-31", ar.getEndDate());
        assertEquals(data, ar.getData());
    }

    @Test
    void setters_workCorrectly() {
        AggregationResult ar = new AggregationResult();
        ar.setId("id");
        ar.setTimestamp("ts");
        ar.setDataSources(Collections.emptyList());
        ar.setStartDate("s");
        ar.setEndDate("e");
        ar.setData(Map.of());
        assertEquals("id", ar.getId());
        assertEquals("ts", ar.getTimestamp());
    }

    @Test
    void equalsAndHashCode_work() {
        AggregationResult a1 = new AggregationResult("id1", "ts", null, "s", "e", null);
        AggregationResult a2 = new AggregationResult("id1", "ts", null, "s", "e", null);
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void toString_containsId() {
        AggregationResult ar = new AggregationResult("id1", "ts", null, "s", "e", null);
        assertTrue(ar.toString().contains("id1"));
    }
}
