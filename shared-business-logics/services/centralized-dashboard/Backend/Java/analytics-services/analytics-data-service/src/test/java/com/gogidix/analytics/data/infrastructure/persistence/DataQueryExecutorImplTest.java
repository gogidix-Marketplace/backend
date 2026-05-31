package com.gogidix.analytics.data.infrastructure.persistence;

import com.gogidix.analytics.data.domain.model.DataQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataQueryExecutorImpl Tests")
class DataQueryExecutorImplTest {

    private DataQueryExecutorImpl executor;

    @BeforeEach
    void setUp() {
        executor = new DataQueryExecutorImpl();
    }

    private DataQuery buildQuery() {
        return DataQuery.builder()
            .id("q-1")
            .queryName("Test")
            .queryDefinition("SELECT * FROM users")
            .queryType(DataQuery.QueryType.SQL)
            .ownerId("u1")
            .tenantId("t1")
            .build();
    }

    @Nested
    @DisplayName("executeQuery Tests")
    class ExecuteQueryTests {

        @Test
        @DisplayName("Should return result with query metadata")
        void shouldReturnResultWithMetadata() {
            DataQuery query = buildQuery();
            Map<String, Object> result = executor.executeQuery(query, null);

            assertNotNull(result);
            assertEquals("q-1", result.get("queryId"));
            assertEquals("Test", result.get("queryName"));
            assertEquals("SQL", result.get("queryType"));
            assertNotNull(result.get("timestamp"));
            assertEquals(100, result.get("rowCount"));
            assertNotNull(result.get("data"));
        }

        @Test
        @DisplayName("Should return result with parameters")
        void shouldReturnResultWithParameters() {
            DataQuery query = buildQuery();
            Map<String, Object> params = Map.of("key", "value");
            Map<String, Object> result = executor.executeQuery(query, params);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should handle null parameters")
        void shouldHandleNullParameters() {
            DataQuery query = buildQuery();
            Map<String, Object> result = executor.executeQuery(query, null);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("executeCountQuery Tests")
    class ExecuteCountQueryTests {

        @Test
        @DisplayName("Should return count")
        void shouldReturnCount() {
            DataQuery query = buildQuery();
            long count = executor.executeCountQuery(query, null);
            assertEquals(100L, count);
        }

        @Test
        @DisplayName("Should return count with parameters")
        void shouldReturnCountWithParameters() {
            DataQuery query = buildQuery();
            long count = executor.executeCountQuery(query, Map.of("p", 1));
            assertEquals(100L, count);
        }
    }

    @Nested
    @DisplayName("validateQuery Tests")
    class ValidateQueryTests {

        @Test
        @DisplayName("Should accept valid query definition")
        void shouldAcceptValidQuery() {
            assertDoesNotThrow(() -> executor.validateQuery("SELECT 1", DataQuery.QueryType.SQL));
        }

        @Test
        @DisplayName("Should reject null query definition")
        void shouldRejectNullQuery() {
            assertThrows(IllegalArgumentException.class,
                () -> executor.validateQuery(null, DataQuery.QueryType.SQL));
        }

        @Test
        @DisplayName("Should reject blank query definition")
        void shouldRejectBlankQuery() {
            assertThrows(IllegalArgumentException.class,
                () -> executor.validateQuery("   ", DataQuery.QueryType.SQL));
        }

        @Test
        @DisplayName("Should reject empty query definition")
        void shouldRejectEmptyQuery() {
            assertThrows(IllegalArgumentException.class,
                () -> executor.validateQuery("", DataQuery.QueryType.SQL));
        }

        @Test
        @DisplayName("Should validate all query types")
        void shouldValidateAllQueryTypes() {
            for (DataQuery.QueryType type : DataQuery.QueryType.values()) {
                assertDoesNotThrow(() -> executor.validateQuery("SELECT 1", type));
            }
        }
    }

    @Nested
    @DisplayName("getQuerySchema Tests")
    class GetQuerySchemaTests {

        @Test
        @DisplayName("Should return schema with columns")
        void shouldReturnSchemaWithColumns() {
            Map<String, Object> schema = executor.getQuerySchema("SELECT * FROM users");

            assertNotNull(schema);
            assertNotNull(schema.get("columns"));
            Object[] columns = (Object[]) schema.get("columns");
            assertEquals(4, columns.length);
        }

        @Test
        @DisplayName("Should return schema with column details")
        void shouldReturnSchemaWithColumnDetails() {
            Map<String, Object> schema = executor.getQuerySchema("SELECT id, name FROM orders");

            Object[] columns = (Object[]) schema.get("columns");
            Map<?, ?> firstCol = (Map<?, ?>) columns[0];
            assertEquals("id", firstCol.get("name"));
            assertEquals("BIGINT", firstCol.get("type"));
            assertEquals(false, firstCol.get("nullable"));
        }
    }
}
