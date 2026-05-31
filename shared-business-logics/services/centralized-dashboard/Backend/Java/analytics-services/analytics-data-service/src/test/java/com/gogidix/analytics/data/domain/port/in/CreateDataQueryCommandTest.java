package com.gogidix.analytics.data.domain.port.in;

import com.gogidix.analytics.data.domain.model.DataQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreateDataQueryCommand Tests")
class CreateDataQueryCommandTest {

    private CreateDataQueryCommand buildFullCommand() {
        return CreateDataQueryCommand.builder()
            .queryName("Query 1")
            .description("A test query")
            .queryDefinition("SELECT * FROM users WHERE active = true")
            .queryType(DataQuery.QueryType.SQL)
            .dataSource("analytics_db")
            .parametersSchema("{\"startDate\":\"date\"}")
            .category("financial")
            .tags("revenue,monthly")
            .ownerId("user-1")
            .isPublic(true)
            .build();
    }

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        CreateDataQueryCommand cmd = buildFullCommand();
        assertEquals("Query 1", cmd.getQueryName());
        assertEquals("A test query", cmd.getDescription());
        assertEquals("SELECT * FROM users WHERE active = true", cmd.getQueryDefinition());
        assertEquals(DataQuery.QueryType.SQL, cmd.getQueryType());
        assertEquals("analytics_db", cmd.getDataSource());
        assertEquals("{\"startDate\":\"date\"}", cmd.getParametersSchema());
        assertEquals("financial", cmd.getCategory());
        assertEquals("revenue,monthly", cmd.getTags());
        assertEquals("user-1", cmd.getOwnerId());
        assertTrue(cmd.getIsPublic());
    }

    @Test
    @DisplayName("Should use builder defaults")
    void shouldUseBuilderDefaults() {
        CreateDataQueryCommand cmd = CreateDataQueryCommand.builder()
            .queryName("q").queryDefinition("SELECT 1")
            .queryType(DataQuery.QueryType.SQL).ownerId("u1").build();
        assertFalse(cmd.getIsPublic());
        assertNull(cmd.getDescription());
        assertNull(cmd.getDataSource());
        assertNull(cmd.getParametersSchema());
        assertNull(cmd.getCategory());
        assertNull(cmd.getTags());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        CreateDataQueryCommand cmd = new CreateDataQueryCommand();
        assertNull(cmd.getQueryName());
        assertNull(cmd.getQueryDefinition());
        assertNull(cmd.getQueryType());
        assertNull(cmd.getOwnerId());
        assertFalse(cmd.getIsPublic());
    }

    @Test
    @DisplayName("Should use all-args constructor")
    void shouldUseAllArgsConstructor() {
        CreateDataQueryCommand cmd = new CreateDataQueryCommand(
            "name", "desc", "def", DataQuery.QueryType.JOIN,
            "ds", "schema", "cat", "tags", "owner", true
        );
        assertEquals("name", cmd.getQueryName());
        assertEquals("desc", cmd.getDescription());
        assertEquals("def", cmd.getQueryDefinition());
        assertEquals(DataQuery.QueryType.JOIN, cmd.getQueryType());
        assertEquals("ds", cmd.getDataSource());
        assertEquals("schema", cmd.getParametersSchema());
        assertEquals("cat", cmd.getCategory());
        assertEquals("tags", cmd.getTags());
        assertEquals("owner", cmd.getOwnerId());
        assertTrue(cmd.getIsPublic());
    }

    @Nested
    @DisplayName("Setter/Getter Tests")
    class SetterGetterTests {
        private CreateDataQueryCommand cmd = new CreateDataQueryCommand();

        @Test
        void shouldSetAndGetQueryName() { cmd.setQueryName("q2"); assertEquals("q2", cmd.getQueryName()); }
        @Test
        void shouldSetAndGetDescription() { cmd.setDescription("desc"); assertEquals("desc", cmd.getDescription()); }
        @Test
        void shouldSetAndGetQueryDefinition() { cmd.setQueryDefinition("SELECT 1"); assertEquals("SELECT 1", cmd.getQueryDefinition()); }
        @Test
        void shouldSetAndGetQueryType() { cmd.setQueryType(DataQuery.QueryType.AGGREGATION); assertEquals(DataQuery.QueryType.AGGREGATION, cmd.getQueryType()); }
        @Test
        void shouldSetAndGetDataSource() { cmd.setDataSource("ds1"); assertEquals("ds1", cmd.getDataSource()); }
        @Test
        void shouldSetAndGetParametersSchema() { cmd.setParametersSchema("{}"); assertEquals("{}", cmd.getParametersSchema()); }
        @Test
        void shouldSetAndGetCategory() { cmd.setCategory("cat1"); assertEquals("cat1", cmd.getCategory()); }
        @Test
        void shouldSetAndGetTags() { cmd.setTags("t1,t2"); assertEquals("t1,t2", cmd.getTags()); }
        @Test
        void shouldSetAndGetOwnerId() { cmd.setOwnerId("o1"); assertEquals("o1", cmd.getOwnerId()); }
        @Test
        void shouldSetAndGetIsPublic() { cmd.setIsPublic(true); assertTrue(cmd.getIsPublic()); }
    }

    @Nested
    @DisplayName("equals/hashCode/toString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        void equalsSameInstance() {
            CreateDataQueryCommand cmd = new CreateDataQueryCommand();
            assertEquals(cmd, cmd);
        }

        @Test
        void equalsNull() {
            CreateDataQueryCommand cmd = new CreateDataQueryCommand();
            assertNotEquals(null, cmd);
        }

        @Test
        void equalsDifferentType() {
            CreateDataQueryCommand cmd = new CreateDataQueryCommand();
            assertNotEquals("string", cmd);
        }

        @Test
        void equalsEqualFullObjects() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            assertEquals(a, b);
        }

        @Test
        void equalsAllNullFields() {
            CreateDataQueryCommand a = new CreateDataQueryCommand();
            CreateDataQueryCommand b = new CreateDataQueryCommand();
            assertEquals(a, b);
        }

        @Test
        void hashCodeEqual() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCodeConsistent() {
            CreateDataQueryCommand cmd = buildFullCommand();
            assertEquals(cmd.hashCode(), cmd.hashCode());
        }

        @Test
        void toStringNotNull() {
            CreateDataQueryCommand cmd = buildFullCommand();
            assertNotNull(cmd.toString());
        }

        @Test
        void equalsDiffersByQueryName() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setQueryName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByDescription() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryDefinition() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryType() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setQueryType(DataQuery.QueryType.AGGREGATION);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByDataSource() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByParametersSchema() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setParametersSchema("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCategory() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setCategory("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByTags() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setTags("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByOwnerId() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByIsPublic() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setIsPublic(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullQueryName() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setQueryName(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullDescription() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setDescription(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullQueryDefinition() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setQueryDefinition(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullDataSource() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setDataSource(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullOwnerId() {
            CreateDataQueryCommand a = buildFullCommand();
            CreateDataQueryCommand b = buildFullCommand();
            b.setOwnerId(null);
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeWithNullFields() {
            CreateDataQueryCommand cmd = new CreateDataQueryCommand();
            assertDoesNotThrow(() -> cmd.hashCode());
        }

        @Test
        void toStringWithNullFields() {
            CreateDataQueryCommand cmd = new CreateDataQueryCommand();
            assertDoesNotThrow(() -> cmd.toString());
        }
    }
}
