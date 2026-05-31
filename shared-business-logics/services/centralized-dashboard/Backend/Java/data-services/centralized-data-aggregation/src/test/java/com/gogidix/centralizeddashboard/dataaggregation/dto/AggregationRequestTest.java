package com.gogidix.centralizeddashboard.dataaggregation.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AggregationRequestTest {

    @Test
    void noArgsConstructor_createsInstance() {
        AggregationRequest req = new AggregationRequest();
        assertNotNull(req);
        assertNull(req.getCacheKey());
    }

    @Test
    void allArgsConstructor_generatesCacheKey() {
        DataSourceMetadata ds = new DataSourceMetadata("svc", "/ep", true, "m", "NUMERIC");
        AggregationRequest req = new AggregationRequest(
                List.of(ds), "2024-01-01", "2024-01-31", List.of("SUM", "AVG"), true);
        assertNotNull(req.getCacheKey());
        assertTrue(req.isCacheable());
        assertEquals(2, req.getAggregationFunctions().size());
    }

    @Test
    void setDataSources_updatesCacheKey() {
        AggregationRequest req = new AggregationRequest();
        assertNull(req.getCacheKey());
        req.setDataSources(List.of(new DataSourceMetadata("s", "/e", false, "m", "t")));
        assertNotNull(req.getCacheKey());
    }

    @Test
    void setStartDate_updatesCacheKey() {
        AggregationRequest req = new AggregationRequest();
        req.setStartDate("2024-01-01");
        assertNotNull(req.getCacheKey());
    }

    @Test
    void setEndDate_updatesCacheKey() {
        AggregationRequest req = new AggregationRequest();
        req.setEndDate("2024-01-31");
        assertNotNull(req.getCacheKey());
    }

    @Test
    void setAggregationFunctions_updatesCacheKey() {
        AggregationRequest req = new AggregationRequest();
        req.setAggregationFunctions(List.of("SUM"));
        assertNotNull(req.getCacheKey());
    }

    @Test
    void cacheKey_withNullDataSources_works() {
        AggregationRequest req = new AggregationRequest(null, null, null, null, false);
        assertNotNull(req.getCacheKey());
    }

    @Test
    void equalsAndHashCode_work() {
        AggregationRequest r1 = new AggregationRequest(null, "s", "e", null, true);
        AggregationRequest r2 = new AggregationRequest(null, "s", "e", null, true);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void toString_containsFields() {
        AggregationRequest req = new AggregationRequest(null, "s", "e", null, false);
        assertTrue(req.toString().contains("s"));
    }
}
