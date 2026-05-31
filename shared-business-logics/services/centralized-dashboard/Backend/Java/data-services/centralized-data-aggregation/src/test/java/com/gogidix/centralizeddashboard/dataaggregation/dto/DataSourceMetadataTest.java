package com.gogidix.centralizeddashboard.dataaggregation.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceMetadataTest {

    @Test
    void noArgsConstructor_createsInstance() {
        assertNotNull(new DataSourceMetadata());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        DataSourceMetadata dsm = new DataSourceMetadata("svc", "/ep", true, "metric", "NUMERIC");
        assertEquals("svc", dsm.getServiceName());
        assertEquals("/ep", dsm.getEndpoint());
        assertTrue(dsm.isTimeRangeRequired());
        assertEquals("metric", dsm.getMetricName());
        assertEquals("NUMERIC", dsm.getMetricType());
    }

    @Test
    void setters_workCorrectly() {
        DataSourceMetadata dsm = new DataSourceMetadata();
        dsm.setServiceName("s");
        dsm.setEndpoint("/e");
        dsm.setTimeRangeRequired(false);
        dsm.setMetricName("m");
        dsm.setMetricType("t");
        assertEquals("s", dsm.getServiceName());
        assertFalse(dsm.isTimeRangeRequired());
    }

    @Test
    void equalsAndHashCode_work() {
        DataSourceMetadata d1 = new DataSourceMetadata("s", "/e", true, "m", "t");
        DataSourceMetadata d2 = new DataSourceMetadata("s", "/e", true, "m", "t");
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void toString_containsServiceName() {
        DataSourceMetadata d = new DataSourceMetadata("myService", "/ep", true, "m", "t");
        assertTrue(d.toString().contains("myService"));
    }
}
