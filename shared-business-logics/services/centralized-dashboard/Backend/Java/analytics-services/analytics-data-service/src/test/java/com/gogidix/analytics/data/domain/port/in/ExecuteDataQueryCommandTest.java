package com.gogidix.analytics.data.domain.port.in;

import com.gogidix.analytics.data.domain.model.DataQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExecuteDataQueryCommand Tests")
class ExecuteDataQueryCommandTest {

    private ExecuteDataQueryCommand buildFullCommand() {
        return ExecuteDataQueryCommand.builder()
            .queryId("q-1")
            .queryDefinition("SELECT * FROM users")
            .queryType(DataQuery.QueryType.SQL)
            .parameters(Map.of("key", "value"))
            .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
            .endDate(LocalDateTime.of(2024, 12, 31, 23, 59))
            .limit(100)
            .offset(0)
            .orderBy("name ASC")
            .async(true)
            .build();
    }

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        ExecuteDataQueryCommand cmd = buildFullCommand();
        assertEquals("q-1", cmd.getQueryId());
        assertEquals("SELECT * FROM users", cmd.getQueryDefinition());
        assertEquals(DataQuery.QueryType.SQL, cmd.getQueryType());
        assertNotNull(cmd.getParameters());
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), cmd.getStartDate());
        assertEquals(LocalDateTime.of(2024, 12, 31, 23, 59), cmd.getEndDate());
        assertEquals(100, cmd.getLimit());
        assertEquals(0, cmd.getOffset());
        assertEquals("name ASC", cmd.getOrderBy());
        assertTrue(cmd.getAsync());
    }

    @Test
    @DisplayName("Should use builder defaults")
    void shouldUseBuilderDefaults() {
        ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
            .queryType(DataQuery.QueryType.SQL)
            .build();
        assertFalse(cmd.getAsync());
        assertNull(cmd.getQueryId());
        assertNull(cmd.getQueryDefinition());
        assertNull(cmd.getParameters());
        assertNull(cmd.getStartDate());
        assertNull(cmd.getEndDate());
        assertNull(cmd.getLimit());
        assertNull(cmd.getOffset());
        assertNull(cmd.getOrderBy());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();
        assertNull(cmd.getQueryId());
        assertNull(cmd.getQueryType());
        assertFalse(cmd.getAsync());
    }

    @Test
    @DisplayName("Should use all-args constructor")
    void shouldUseAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand(
            "q-1", "SELECT 1", DataQuery.QueryType.SQL,
            Map.of("k", "v"), now, now, 10, 0, "id", false
        );
        assertEquals("q-1", cmd.getQueryId());
        assertEquals("SELECT 1", cmd.getQueryDefinition());
        assertEquals(DataQuery.QueryType.SQL, cmd.getQueryType());
        assertEquals(Map.of("k", "v"), cmd.getParameters());
        assertEquals(now, cmd.getStartDate());
        assertEquals(now, cmd.getEndDate());
        assertEquals(10, cmd.getLimit());
        assertEquals(0, cmd.getOffset());
        assertEquals("id", cmd.getOrderBy());
        assertFalse(cmd.getAsync());
    }

    @Nested
    @DisplayName("Setter/Getter Tests")
    class SetterGetterTests {
        private ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();

        @Test
        void shouldSetAndGetQueryId() { cmd.setQueryId("q1"); assertEquals("q1", cmd.getQueryId()); }
        @Test
        void shouldSetAndGetQueryDefinition() { cmd.setQueryDefinition("SELECT 1"); assertEquals("SELECT 1", cmd.getQueryDefinition()); }
        @Test
        void shouldSetAndGetQueryType() { cmd.setQueryType(DataQuery.QueryType.CUSTOM); assertEquals(DataQuery.QueryType.CUSTOM, cmd.getQueryType()); }
        @Test
        void shouldSetAndGetParameters() { cmd.setParameters(Map.of("a", 1)); assertEquals(Map.of("a", 1), cmd.getParameters()); }
        @Test
        void shouldSetAndGetStartDate() { LocalDateTime d = LocalDateTime.now(); cmd.setStartDate(d); assertEquals(d, cmd.getStartDate()); }
        @Test
        void shouldSetAndGetEndDate() { LocalDateTime d = LocalDateTime.now(); cmd.setEndDate(d); assertEquals(d, cmd.getEndDate()); }
        @Test
        void shouldSetAndGetLimit() { cmd.setLimit(50); assertEquals(50, cmd.getLimit()); }
        @Test
        void shouldSetAndGetOffset() { cmd.setOffset(10); assertEquals(10, cmd.getOffset()); }
        @Test
        void shouldSetAndGetOrderBy() { cmd.setOrderBy("date DESC"); assertEquals("date DESC", cmd.getOrderBy()); }
        @Test
        void shouldSetAndGetAsync() { cmd.setAsync(true); assertTrue(cmd.getAsync()); }
    }

    @Nested
    @DisplayName("equals/hashCode/toString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        void equalsSameInstance() {
            ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();
            assertEquals(cmd, cmd);
        }

        @Test
        void equalsNull() {
            ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();
            assertNotEquals(null, cmd);
        }

        @Test
        void equalsDifferentType() {
            ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();
            assertNotEquals("string", cmd);
        }

        @Test
        void equalsEqualFullObjects() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            assertEquals(a, b);
        }

        @Test
        void equalsAllNullFields() {
            ExecuteDataQueryCommand a = new ExecuteDataQueryCommand();
            ExecuteDataQueryCommand b = new ExecuteDataQueryCommand();
            assertEquals(a, b);
        }

        @Test
        void hashCodeEqual() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCodeConsistent() {
            ExecuteDataQueryCommand cmd = buildFullCommand();
            assertEquals(cmd.hashCode(), cmd.hashCode());
        }

        @Test
        void toStringNotNull() {
            ExecuteDataQueryCommand cmd = buildFullCommand();
            assertNotNull(cmd.toString());
        }

        @Test
        void equalsDiffersByQueryId() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setQueryId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryDefinition() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryType() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setQueryType(DataQuery.QueryType.AGGREGATION);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByParameters() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setParameters(Map.of("other", "val"));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByStartDate() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setStartDate(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByEndDate() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setEndDate(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByLimit() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setLimit(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByOffset() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setOffset(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByOrderBy() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setOrderBy("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByAsync() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setAsync(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullQueryId() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setQueryId(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullQueryDefinition() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setQueryDefinition(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullParameters() {
            ExecuteDataQueryCommand a = buildFullCommand();
            ExecuteDataQueryCommand b = buildFullCommand();
            b.setParameters(null);
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeWithNullFields() {
            ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();
            assertDoesNotThrow(() -> cmd.hashCode());
        }

        @Test
        void toStringWithNullFields() {
            ExecuteDataQueryCommand cmd = new ExecuteDataQueryCommand();
            assertDoesNotThrow(() -> cmd.toString());
        }
    }
}
