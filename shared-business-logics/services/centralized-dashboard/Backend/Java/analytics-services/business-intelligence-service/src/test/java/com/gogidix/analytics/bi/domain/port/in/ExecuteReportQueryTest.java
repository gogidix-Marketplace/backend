package com.gogidix.analytics.bi.domain.port.in;

import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExecuteReportQueryTest {

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void builderCreatesQueryWithAllFields() {
            Map<String, Object> params = new HashMap<>();
            params.put("region", "US");

            LocalDateTime start = LocalDateTime.now().minusDays(7);
            LocalDateTime end = LocalDateTime.now();

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .startDate(start)
                .endDate(end)
                .parameters(params)
                .outputFormat(ReportDefinition.OutputFormat.CSV)
                .requestedBy("user-1")
                .async(false)
                .build();

            assertEquals("report-1", query.getReportId());
            assertEquals(start, query.getStartDate());
            assertEquals(end, query.getEndDate());
            assertEquals(params, query.getParameters());
            assertEquals(ReportDefinition.OutputFormat.CSV, query.getOutputFormat());
            assertEquals("user-1", query.getRequestedBy());
            assertFalse(query.getAsync());
        }

        @Test
        void builderDefaults() {
            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .build();

            assertTrue(query.getAsync());
        }
    }

    @Test
    void noArgsConstructor() {
        ExecuteReportQuery query = new ExecuteReportQuery();
        assertNotNull(query);
        assertNull(query.getReportId());
    }

    @Test
    void allArgsConstructor() {
        ExecuteReportQuery query = new ExecuteReportQuery("r1", null, null, null, null, "user", true);
        assertEquals("r1", query.getReportId());
        assertEquals("user", query.getRequestedBy());
        assertTrue(query.getAsync());
    }

    @Test
    void gettersAndSetters() {
        ExecuteReportQuery query = new ExecuteReportQuery();

        query.setReportId("report-2");
        assertEquals("report-2", query.getReportId());

        LocalDateTime now = LocalDateTime.now();
        query.setStartDate(now);
        assertEquals(now, query.getStartDate());

        query.setEndDate(now.plusDays(1));
        assertEquals(now.plusDays(1), query.getEndDate());

        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        query.setParameters(params);
        assertEquals(params, query.getParameters());

        query.setOutputFormat(ReportDefinition.OutputFormat.JSON);
        assertEquals(ReportDefinition.OutputFormat.JSON, query.getOutputFormat());

        query.setRequestedBy("user-2");
        assertEquals("user-2", query.getRequestedBy());

        query.setAsync(false);
        assertFalse(query.getAsync());
    }

    @Nested
    @DisplayName("Equals Branch Coverage Tests")
    class EqualsBranchTests {

        private ExecuteReportQuery createFullyPopulated() {
            LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30);
            Map<String, Object> params = new HashMap<>();
            params.put("key", "value");
            return ExecuteReportQuery.builder()
                .reportId("r-1")
                .startDate(now)
                .endDate(now)
                .parameters(params)
                .outputFormat(ReportDefinition.OutputFormat.CSV)
                .requestedBy("user-1")
                .async(false)
                .build();
        }

        @Test
        void equalsSameInstance() {
            ExecuteReportQuery q = createFullyPopulated();
            assertEquals(q, q);
        }

        @Test
        void equalsNull() {
            assertNotEquals(createFullyPopulated(), null);
        }

        @Test
        void equalsDifferentType() {
            assertNotEquals(createFullyPopulated(), "string");
        }

        @Test
        void equalsIdenticalObjects() {
            assertEquals(createFullyPopulated(), createFullyPopulated());
        }

        @Test
        void equalsDifferentReportId() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            b.setReportId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentStartDate() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            b.setStartDate(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentEndDate() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            b.setEndDate(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentParameters() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            Map<String, Object> p = new HashMap<>();
            p.put("other", "val");
            b.setParameters(p);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOutputFormat() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            b.setOutputFormat(ReportDefinition.OutputFormat.PDF);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRequestedBy() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            b.setRequestedBy("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentAsync() {
            ExecuteReportQuery a = createFullyPopulated();
            ExecuteReportQuery b = createFullyPopulated();
            b.setAsync(true);
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            ExecuteReportQuery a = new ExecuteReportQuery();
            ExecuteReportQuery b = new ExecuteReportQuery();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            ExecuteReportQuery q = createFullyPopulated();
            int h1 = q.hashCode();
            int h2 = q.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            ExecuteReportQuery q = new ExecuteReportQuery();
            assertNotNull(q.hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            assertNotNull(createFullyPopulated().toString());
        }

        @Test
        void toStringWithNullFields() {
            assertNotNull(new ExecuteReportQuery().toString());
        }
    }
}
