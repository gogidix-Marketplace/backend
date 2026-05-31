package com.gogidix.analytics.data.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataQuery Tests")
class DataQueryTest {

    private DataQuery buildFullQuery() {
        return DataQuery.builder()
            .id("id-1")
            .queryName("Revenue Query")
            .description("Monthly revenue report")
            .queryDefinition("SELECT SUM(amount) FROM orders GROUP BY month")
            .queryType(DataQuery.QueryType.AGGREGATION)
            .dataSource("analytics_db")
            .parametersSchema("{\"startDate\":\"date\",\"endDate\":\"date\"}")
            .category("financial")
            .tags("revenue,monthly")
            .ownerId("user-1")
            .tenantId("tenant-1")
            .isPublic(true)
            .isFavorite(true)
            .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .lastExecutedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
            .executionCount(5)
            .avgExecutionTimeMs(250L)
            .build();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void shouldCreateWithAllFields() {
            DataQuery q = buildFullQuery();
            assertEquals("id-1", q.getId());
            assertEquals("Revenue Query", q.getQueryName());
            assertEquals("Monthly revenue report", q.getDescription());
            assertEquals("SELECT SUM(amount) FROM orders GROUP BY month", q.getQueryDefinition());
            assertEquals(DataQuery.QueryType.AGGREGATION, q.getQueryType());
            assertEquals("analytics_db", q.getDataSource());
            assertEquals("{\"startDate\":\"date\",\"endDate\":\"date\"}", q.getParametersSchema());
            assertEquals("financial", q.getCategory());
            assertEquals("revenue,monthly", q.getTags());
            assertEquals("user-1", q.getOwnerId());
            assertEquals("tenant-1", q.getTenantId());
            assertTrue(q.getIsPublic());
            assertTrue(q.getIsFavorite());
            assertEquals(5, q.getExecutionCount());
            assertEquals(250L, q.getAvgExecutionTimeMs());
        }

        @Test
        void shouldUseBuilderDefaults() {
            DataQuery q = DataQuery.builder()
                .queryName("Q")
                .queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL)
                .ownerId("u1")
                .tenantId("t1")
                .build();
            assertFalse(q.getIsPublic());
            assertFalse(q.getIsFavorite());
            assertEquals(0, q.getExecutionCount());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        void shouldCreateWithNoArgsConstructor() {
            DataQuery q = new DataQuery();
            assertNull(q.getId());
            assertNull(q.getQueryName());
            assertNull(q.getQueryType());
        }

        @Test
        void shouldCreateWithAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            DataQuery q = new DataQuery(
                "id-1", "name", "desc", "def",
                DataQuery.QueryType.JOIN, "ds", "schema",
                "cat", "tags", "u1", "t1",
                true, false,
                now, now, now, 10, 100L
            );
            assertEquals("id-1", q.getId());
            assertEquals("name", q.getQueryName());
            assertEquals(DataQuery.QueryType.JOIN, q.getQueryType());
        }
    }

    @Nested
    @DisplayName("Setter/Getter Tests")
    class SetterGetterTests {

        private DataQuery query;

        @BeforeEach
        void setUp() {
            query = new DataQuery();
        }

        @Test
        void shouldSetAndGetId() { query.setId("test-id"); assertEquals("test-id", query.getId()); }
        @Test
        void shouldSetAndGetQueryName() { query.setQueryName("My Query"); assertEquals("My Query", query.getQueryName()); }
        @Test
        void shouldSetAndGetDescription() { query.setDescription("A test query"); assertEquals("A test query", query.getDescription()); }
        @Test
        void shouldSetAndGetQueryDefinition() { query.setQueryDefinition("SELECT * FROM users"); assertEquals("SELECT * FROM users", query.getQueryDefinition()); }
        @Test
        void shouldSetAndGetQueryType() { query.setQueryType(DataQuery.QueryType.METRIC); assertEquals(DataQuery.QueryType.METRIC, query.getQueryType()); }
        @Test
        void shouldSetAndGetDataSource() { query.setDataSource("postgres-main"); assertEquals("postgres-main", query.getDataSource()); }
        @Test
        void shouldSetAndGetParametersSchema() { query.setParametersSchema("{}"); assertEquals("{}", query.getParametersSchema()); }
        @Test
        void shouldSetAndGetCategory() { query.setCategory("reporting"); assertEquals("reporting", query.getCategory()); }
        @Test
        void shouldSetAndGetTags() { query.setTags("alpha,beta"); assertEquals("alpha,beta", query.getTags()); }
        @Test
        void shouldSetAndGetOwnerId() { query.setOwnerId("owner-99"); assertEquals("owner-99", query.getOwnerId()); }
        @Test
        void shouldSetAndGetTenantId() { query.setTenantId("tenant-42"); assertEquals("tenant-42", query.getTenantId()); }
        @Test
        void shouldSetAndGetIsPublic() { query.setIsPublic(true); assertTrue(query.getIsPublic()); }
        @Test
        void shouldSetAndGetIsFavorite() { query.setIsFavorite(true); assertTrue(query.getIsFavorite()); }
        @Test
        void shouldSetAndGetCreatedAt() { LocalDateTime now = LocalDateTime.now(); query.setCreatedAt(now); assertEquals(now, query.getCreatedAt()); }
        @Test
        void shouldSetAndGetUpdatedAt() { LocalDateTime now = LocalDateTime.now(); query.setUpdatedAt(now); assertEquals(now, query.getUpdatedAt()); }
        @Test
        void shouldSetAndGetLastExecutedAt() { LocalDateTime now = LocalDateTime.now(); query.setLastExecutedAt(now); assertEquals(now, query.getLastExecutedAt()); }
        @Test
        void shouldSetAndGetExecutionCount() { query.setExecutionCount(42); assertEquals(42, query.getExecutionCount()); }
        @Test
        void shouldSetAndGetAvgExecutionTimeMs() { query.setAvgExecutionTimeMs(500L); assertEquals(500L, query.getAvgExecutionTimeMs()); }
    }

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        void onCreateShouldSetTimestampsAndId() {
            DataQuery q = new DataQuery();
            assertNull(q.getCreatedAt());
            assertNull(q.getId());
            q.onCreate();
            assertNotNull(q.getCreatedAt());
            assertNotNull(q.getUpdatedAt());
            assertNotNull(q.getId());
        }

        @Test
        void onCreateShouldNotOverwriteExistingId() {
            DataQuery q = new DataQuery();
            q.setId("custom-id");
            q.onCreate();
            assertEquals("custom-id", q.getId());
        }

        @Test
        void onUpdateShouldSetUpdatedAt() {
            DataQuery q = new DataQuery();
            q.setUpdatedAt(LocalDateTime.now().minusDays(1));
            q.onUpdate();
            assertNotNull(q.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("recordExecution Tests")
    class RecordExecutionTests {

        @Test
        void shouldSetAvgTimeOnFirstExecution() {
            DataQuery q = DataQuery.builder()
                .queryName("Q").queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL).ownerId("u1").tenantId("t1")
                .build();
            assertNull(q.getAvgExecutionTimeMs());
            q.recordExecution(100L);
            assertEquals(1, q.getExecutionCount());
            assertEquals(100L, q.getAvgExecutionTimeMs());
            assertNotNull(q.getLastExecutedAt());
        }

        @Test
        void shouldAverageExecutionTimeOnSubsequentExecutions() {
            DataQuery q = DataQuery.builder()
                .queryName("Q").queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL).ownerId("u1").tenantId("t1")
                .build();
            q.recordExecution(100L);
            q.recordExecution(200L);
            assertEquals(2, q.getExecutionCount());
            assertEquals(150L, q.getAvgExecutionTimeMs());
        }

        @Test
        void shouldIncrementCountMultipleTimes() {
            DataQuery q = DataQuery.builder()
                .queryName("Q").queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL).ownerId("u1").tenantId("t1")
                .build();
            q.recordExecution(50L);
            q.recordExecution(60L);
            q.recordExecution(70L);
            assertEquals(3, q.getExecutionCount());
            assertEquals(62L, q.getAvgExecutionTimeMs());
        }

        @Test
        void shouldHandleZeroExecutionTime() {
            DataQuery q = DataQuery.builder()
                .queryName("Q").queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL).ownerId("u1").tenantId("t1")
                .build();
            q.recordExecution(0L);
            assertEquals(1, q.getExecutionCount());
            assertEquals(0L, q.getAvgExecutionTimeMs());
        }
    }

    @Nested
    @DisplayName("QueryType Enum Tests")
    class QueryTypeEnumTests {

        @Test
        void shouldHaveAllValues() {
            assertEquals(5, DataQuery.QueryType.values().length);
        }

        @Test
        void shouldResolveEachEnumByName() {
            assertNotNull(DataQuery.QueryType.valueOf("SQL"));
            assertNotNull(DataQuery.QueryType.valueOf("AGGREGATION"));
            assertNotNull(DataQuery.QueryType.valueOf("METRIC"));
            assertNotNull(DataQuery.QueryType.valueOf("CUSTOM"));
            assertNotNull(DataQuery.QueryType.valueOf("JOIN"));
        }
    }

    @Nested
    @DisplayName("equals/hashCode/toString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        void equalsSameInstance() {
            DataQuery q = new DataQuery();
            assertEquals(q, q);
        }

        @Test
        void equalsNull() {
            DataQuery q = new DataQuery();
            assertNotEquals(null, q);
        }

        @Test
        void equalsDifferentType() {
            DataQuery q = new DataQuery();
            assertNotEquals("string", q);
        }

        @Test
        void equalsEqualFullObjects() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            assertEquals(a, b);
        }

        @Test
        void equalsAllNullFields() {
            DataQuery a = new DataQuery();
            DataQuery b = new DataQuery();
            assertEquals(a, b);
        }

        @Test
        void hashCodeEqual() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCodeConsistent() {
            DataQuery q = buildFullQuery();
            int h1 = q.hashCode();
            int h2 = q.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void toStringNotNull() {
            DataQuery q = buildFullQuery();
            assertNotNull(q.toString());
            assertTrue(q.toString().contains("Revenue Query"));
        }

        @Test
        void equalsDiffersById() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryName() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setQueryName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByDescription() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryDefinition() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryType() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setQueryType(DataQuery.QueryType.JOIN);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByDataSource() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByParametersSchema() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setParametersSchema("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCategory() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setCategory("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByTags() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setTags("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByOwnerId() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByTenantId() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setTenantId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByIsPublic() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setIsPublic(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByIsFavorite() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setIsFavorite(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCreatedAt() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByUpdatedAt() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setUpdatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByLastExecutedAt() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setLastExecutedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByExecutionCount() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setExecutionCount(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByAvgExecutionTimeMs() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setAvgExecutionTimeMs(999L);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullId() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setId(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullQueryName() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setQueryName(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullDescription() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setDescription(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullTenantId() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setTenantId(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullAvgExecutionTimeMs() {
            DataQuery a = buildFullQuery();
            DataQuery b = buildFullQuery();
            b.setAvgExecutionTimeMs(null);
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeWithNullFields() {
            DataQuery q = new DataQuery();
            assertDoesNotThrow(() -> q.hashCode());
        }

        @Test
        void toStringWithNullFields() {
            DataQuery q = new DataQuery();
            assertDoesNotThrow(() -> q.toString());
        }
    }
}
