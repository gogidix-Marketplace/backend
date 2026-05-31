package com.gogidix.analytics.data.domain.port.in;

import com.gogidix.analytics.data.domain.model.DataExport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CreateExportCommand Tests")
class CreateExportCommandTest {

    private CreateExportCommand buildFullCommand() {
        return CreateExportCommand.builder()
            .exportName("Export 1")
            .queryId("query-1")
            .queryDefinition("SELECT * FROM users")
            .exportFormat(DataExport.ExportFormat.CSV)
            .compressionType(DataExport.CompressionType.GZIP)
            .includeHeaders(false)
            .filters(Map.of("status", "active"))
            .requestedBy("user-1")
            .expiresInHours(48)
            .build();
    }

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        CreateExportCommand cmd = buildFullCommand();
        assertEquals("Export 1", cmd.getExportName());
        assertEquals("query-1", cmd.getQueryId());
        assertEquals("SELECT * FROM users", cmd.getQueryDefinition());
        assertEquals(DataExport.ExportFormat.CSV, cmd.getExportFormat());
        assertEquals(DataExport.CompressionType.GZIP, cmd.getCompressionType());
        assertFalse(cmd.getIncludeHeaders());
        assertEquals(Map.of("status", "active"), cmd.getFilters());
        assertEquals("user-1", cmd.getRequestedBy());
        assertEquals(48, cmd.getExpiresInHours());
    }

    @Test
    @DisplayName("Should use builder defaults")
    void shouldUseBuilderDefaults() {
        CreateExportCommand cmd = CreateExportCommand.builder()
            .exportFormat(DataExport.ExportFormat.CSV)
            .build();
        assertTrue(cmd.getIncludeHeaders());
        assertEquals(DataExport.CompressionType.NONE, cmd.getCompressionType());
        assertNull(cmd.getExportName());
        assertNull(cmd.getQueryId());
        assertNull(cmd.getQueryDefinition());
        assertNull(cmd.getFilters());
        assertNull(cmd.getRequestedBy());
        assertNull(cmd.getExpiresInHours());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        CreateExportCommand cmd = new CreateExportCommand();
        assertNull(cmd.getExportName());
        assertNull(cmd.getExportFormat());
        assertEquals(DataExport.CompressionType.NONE, cmd.getCompressionType());
        assertTrue(cmd.getIncludeHeaders());
    }

    @Test
    @DisplayName("Should use all-args constructor")
    void shouldUseAllArgsConstructor() {
        CreateExportCommand cmd = new CreateExportCommand(
            "name", "q1", "def", DataExport.ExportFormat.JSON,
            DataExport.CompressionType.ZIP, false, Map.of("k", "v"),
            "user", 24
        );
        assertEquals("name", cmd.getExportName());
        assertEquals("q1", cmd.getQueryId());
        assertEquals("def", cmd.getQueryDefinition());
        assertEquals(DataExport.ExportFormat.JSON, cmd.getExportFormat());
        assertEquals(DataExport.CompressionType.ZIP, cmd.getCompressionType());
        assertFalse(cmd.getIncludeHeaders());
        assertEquals(Map.of("k", "v"), cmd.getFilters());
        assertEquals("user", cmd.getRequestedBy());
        assertEquals(24, cmd.getExpiresInHours());
    }

    @Nested
    @DisplayName("Setter/Getter Tests")
    class SetterGetterTests {
        private CreateExportCommand cmd = new CreateExportCommand();

        @Test
        void shouldSetAndGetExportName() { cmd.setExportName("exp1"); assertEquals("exp1", cmd.getExportName()); }
        @Test
        void shouldSetAndGetQueryId() { cmd.setQueryId("q1"); assertEquals("q1", cmd.getQueryId()); }
        @Test
        void shouldSetAndGetQueryDefinition() { cmd.setQueryDefinition("SELECT 1"); assertEquals("SELECT 1", cmd.getQueryDefinition()); }
        @Test
        void shouldSetAndGetExportFormat() { cmd.setExportFormat(DataExport.ExportFormat.PDF); assertEquals(DataExport.ExportFormat.PDF, cmd.getExportFormat()); }
        @Test
        void shouldSetAndGetCompressionType() { cmd.setCompressionType(DataExport.CompressionType.SNAPPY); assertEquals(DataExport.CompressionType.SNAPPY, cmd.getCompressionType()); }
        @Test
        void shouldSetAndGetIncludeHeaders() { cmd.setIncludeHeaders(false); assertFalse(cmd.getIncludeHeaders()); }
        @Test
        void shouldSetAndGetFilters() { cmd.setFilters(Map.of("a", 1)); assertEquals(Map.of("a", 1), cmd.getFilters()); }
        @Test
        void shouldSetAndGetRequestedBy() { cmd.setRequestedBy("user1"); assertEquals("user1", cmd.getRequestedBy()); }
        @Test
        void shouldSetAndGetExpiresInHours() { cmd.setExpiresInHours(72); assertEquals(72, cmd.getExpiresInHours()); }
    }

    @Nested
    @DisplayName("equals/hashCode/toString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        void equalsSameInstance() {
            CreateExportCommand cmd = new CreateExportCommand();
            assertEquals(cmd, cmd);
        }

        @Test
        void equalsNull() {
            CreateExportCommand cmd = new CreateExportCommand();
            assertNotEquals(null, cmd);
        }

        @Test
        void equalsDifferentType() {
            CreateExportCommand cmd = new CreateExportCommand();
            assertNotEquals("string", cmd);
        }

        @Test
        void equalsEqualFullObjects() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            assertEquals(a, b);
        }

        @Test
        void equalsAllNullFields() {
            CreateExportCommand a = new CreateExportCommand();
            CreateExportCommand b = new CreateExportCommand();
            assertEquals(a, b);
        }

        @Test
        void hashCodeEqual() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCodeConsistent() {
            CreateExportCommand cmd = buildFullCommand();
            assertEquals(cmd.hashCode(), cmd.hashCode());
        }

        @Test
        void toStringNotNull() {
            CreateExportCommand cmd = buildFullCommand();
            assertNotNull(cmd.toString());
        }

        @Test
        void equalsDiffersByExportName() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setExportName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryId() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setQueryId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryDefinition() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByExportFormat() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setExportFormat(DataExport.ExportFormat.JSON);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCompressionType() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setCompressionType(DataExport.CompressionType.NONE);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByIncludeHeaders() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setIncludeHeaders(true);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByFilters() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setFilters(Map.of("other", "val"));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByRequestedBy() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setRequestedBy("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByExpiresInHours() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setExpiresInHours(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullExportName() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setExportName(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullQueryId() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setQueryId(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullFilters() {
            CreateExportCommand a = buildFullCommand();
            CreateExportCommand b = buildFullCommand();
            b.setFilters(null);
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeWithNullFields() {
            CreateExportCommand cmd = new CreateExportCommand();
            assertDoesNotThrow(() -> cmd.hashCode());
        }

        @Test
        void toStringWithNullFields() {
            CreateExportCommand cmd = new CreateExportCommand();
            assertDoesNotThrow(() -> cmd.toString());
        }
    }
}
