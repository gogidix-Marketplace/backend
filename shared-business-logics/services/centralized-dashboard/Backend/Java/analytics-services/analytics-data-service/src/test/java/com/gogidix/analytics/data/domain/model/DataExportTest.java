package com.gogidix.analytics.data.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataExport Tests")
class DataExportTest {

    private DataExport buildFullExport() {
        return DataExport.builder()
            .id("id-1")
            .exportName("Export 1")
            .queryDefinition("SELECT * FROM users")
            .queryId("query-1")
            .exportFormat(DataExport.ExportFormat.CSV)
            .status(DataExport.ExportStatus.COMPLETED)
            .filePath("/exports/test.csv")
            .fileUrl("https://example.com/test.csv")
            .fileSizeBytes(2048L)
            .rowCount(100)
            .columnCount(5)
            .compressionType(DataExport.CompressionType.GZIP)
            .includeHeaders(true)
            .filters("{\"status\":\"active\"}")
            .requestedBy("user-1")
            .tenantId("tenant-1")
            .startedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .completedAt(LocalDateTime.of(2024, 1, 1, 1, 0))
            .expiresAt(LocalDateTime.of(2024, 1, 2, 0, 0))
            .errorMessage(null)
            .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .updatedAt(LocalDateTime.of(2024, 1, 1, 1, 0))
            .progressPercentage(100)
            .build();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should create with builder and all fields")
        void shouldCreateWithAllFields() {
            DataExport e = buildFullExport();
            assertEquals("id-1", e.getId());
            assertEquals("Export 1", e.getExportName());
            assertEquals("SELECT * FROM users", e.getQueryDefinition());
            assertEquals("query-1", e.getQueryId());
            assertEquals(DataExport.ExportFormat.CSV, e.getExportFormat());
            assertEquals(DataExport.ExportStatus.COMPLETED, e.getStatus());
            assertEquals("/exports/test.csv", e.getFilePath());
            assertEquals("https://example.com/test.csv", e.getFileUrl());
            assertEquals(2048L, e.getFileSizeBytes());
            assertEquals(100, e.getRowCount());
            assertEquals(5, e.getColumnCount());
            assertEquals(DataExport.CompressionType.GZIP, e.getCompressionType());
            assertTrue(e.getIncludeHeaders());
            assertEquals("{\"status\":\"active\"}", e.getFilters());
            assertEquals("user-1", e.getRequestedBy());
            assertEquals("tenant-1", e.getTenantId());
            assertEquals(100, e.getProgressPercentage());
        }

        @Test
        @DisplayName("Should use builder defaults")
        void shouldUseBuilderDefaults() {
            DataExport e = DataExport.builder()
                .exportFormat(DataExport.ExportFormat.CSV)
                .tenantId("t1")
                .build();
            assertEquals(DataExport.ExportStatus.PENDING, e.getStatus());
            assertTrue(e.getIncludeHeaders());
            assertEquals(0, e.getProgressPercentage());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with no-args constructor")
        void shouldCreateWithNoArgsConstructor() {
            DataExport e = new DataExport();
            assertNull(e.getId());
            assertNull(e.getExportName());
            assertNull(e.getExportFormat());
            assertNull(e.getTenantId());
        }

        @Test
        @DisplayName("Should create with all-args constructor")
        void shouldCreateWithAllArgsConstructor() {
            DataExport e = new DataExport(
                "id-1", "name", "queryDef", "queryId",
                DataExport.ExportFormat.JSON, DataExport.ExportStatus.PENDING,
                "/path", "url", 1024L, 50, 10,
                DataExport.CompressionType.ZIP, true, "{}",
                "user-1", "tenant-1",
                LocalDateTime.now(), null, LocalDateTime.now().plusHours(24),
                null, LocalDateTime.now(), LocalDateTime.now(), 0
            );
            assertEquals("id-1", e.getId());
            assertEquals("name", e.getExportName());
            assertEquals(DataExport.ExportFormat.JSON, e.getExportFormat());
        }
    }

    @Nested
    @DisplayName("Setter/Getter Tests")
    class SetterGetterTests {

        private DataExport export;

        @BeforeEach
        void setUp() {
            export = new DataExport();
        }

        @Test
        void shouldSetAndGetId() { export.setId("test-id"); assertEquals("test-id", export.getId()); }

        @Test
        void shouldSetAndGetExportName() { export.setExportName("My Export"); assertEquals("My Export", export.getExportName()); }

        @Test
        void shouldSetAndGetQueryDefinition() { export.setQueryDefinition("SELECT 1"); assertEquals("SELECT 1", export.getQueryDefinition()); }

        @Test
        void shouldSetAndGetQueryId() { export.setQueryId("q-1"); assertEquals("q-1", export.getQueryId()); }

        @Test
        void shouldSetAndGetExportFormat() { export.setExportFormat(DataExport.ExportFormat.EXCEL); assertEquals(DataExport.ExportFormat.EXCEL, export.getExportFormat()); }

        @Test
        void shouldSetAndGetStatus() { export.setStatus(DataExport.ExportStatus.PROCESSING); assertEquals(DataExport.ExportStatus.PROCESSING, export.getStatus()); }

        @Test
        void shouldSetAndGetFilePath() { export.setFilePath("/data/export.csv"); assertEquals("/data/export.csv", export.getFilePath()); }

        @Test
        void shouldSetAndGetFileUrl() { export.setFileUrl("https://cdn.example.com/export.csv"); assertEquals("https://cdn.example.com/export.csv", export.getFileUrl()); }

        @Test
        void shouldSetAndGetFileSizeBytes() { export.setFileSizeBytes(4096L); assertEquals(4096L, export.getFileSizeBytes()); }

        @Test
        void shouldSetAndGetRowCount() { export.setRowCount(500); assertEquals(500, export.getRowCount()); }

        @Test
        void shouldSetAndGetColumnCount() { export.setColumnCount(12); assertEquals(12, export.getColumnCount()); }

        @Test
        void shouldSetAndGetCompressionType() { export.setCompressionType(DataExport.CompressionType.SNAPPY); assertEquals(DataExport.CompressionType.SNAPPY, export.getCompressionType()); }

        @Test
        void shouldSetAndGetIncludeHeaders() { export.setIncludeHeaders(false); assertFalse(export.getIncludeHeaders()); }

        @Test
        void shouldSetAndGetFilters() { export.setFilters("{\"key\":\"value\"}"); assertEquals("{\"key\":\"value\"}", export.getFilters()); }

        @Test
        void shouldSetAndGetRequestedBy() { export.setRequestedBy("admin"); assertEquals("admin", export.getRequestedBy()); }

        @Test
        void shouldSetAndGetTenantId() { export.setTenantId("tenant-42"); assertEquals("tenant-42", export.getTenantId()); }

        @Test
        void shouldSetAndGetStartedAt() { LocalDateTime now = LocalDateTime.now(); export.setStartedAt(now); assertEquals(now, export.getStartedAt()); }

        @Test
        void shouldSetAndGetCompletedAt() { LocalDateTime now = LocalDateTime.now(); export.setCompletedAt(now); assertEquals(now, export.getCompletedAt()); }

        @Test
        void shouldSetAndGetExpiresAt() { LocalDateTime future = LocalDateTime.now().plusDays(7); export.setExpiresAt(future); assertEquals(future, export.getExpiresAt()); }

        @Test
        void shouldSetAndGetErrorMessage() { export.setErrorMessage("Disk full"); assertEquals("Disk full", export.getErrorMessage()); }

        @Test
        void shouldSetAndGetCreatedAt() { LocalDateTime now = LocalDateTime.now(); export.setCreatedAt(now); assertEquals(now, export.getCreatedAt()); }

        @Test
        void shouldSetAndGetUpdatedAt() { LocalDateTime now = LocalDateTime.now(); export.setUpdatedAt(now); assertEquals(now, export.getUpdatedAt()); }

        @Test
        void shouldSetAndGetProgressPercentage() { export.setProgressPercentage(75); assertEquals(75, export.getProgressPercentage()); }
    }

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        void onCreateShouldSetTimestampsAndId() {
            DataExport e = new DataExport();
            e.setTenantId("t1");
            e.setExportFormat(DataExport.ExportFormat.CSV);
            e.onCreate();
            assertNotNull(e.getCreatedAt());
            assertNotNull(e.getUpdatedAt());
            assertNotNull(e.getId());
            assertNotNull(e.getExpiresAt());
        }

        @Test
        void onCreateShouldNotOverwriteExistingId() {
            DataExport e = new DataExport();
            e.setId("my-custom-id");
            e.onCreate();
            assertEquals("my-custom-id", e.getId());
        }

        @Test
        void onCreateShouldSetDefaultExpiration() {
            DataExport e = new DataExport();
            assertNull(e.getExpiresAt());
            e.onCreate();
            assertNotNull(e.getExpiresAt());
            assertTrue(e.getExpiresAt().isAfter(LocalDateTime.now().plusHours(23)));
        }

        @Test
        void onCreateShouldNotOverwriteExistingExpiresAt() {
            DataExport e = new DataExport();
            LocalDateTime customExpiry = LocalDateTime.now().plusDays(7);
            e.setExpiresAt(customExpiry);
            e.onCreate();
            assertEquals(customExpiry, e.getExpiresAt());
        }

        @Test
        void onUpdateShouldSetUpdatedAt() {
            DataExport e = new DataExport();
            e.setUpdatedAt(LocalDateTime.now().minusDays(1));
            e.onUpdate();
            assertNotNull(e.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        void markAsProcessingShouldSetStatusAndStartedAt() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            assertNull(e.getStartedAt());
            e.markAsProcessing();
            assertEquals(DataExport.ExportStatus.PROCESSING, e.getStatus());
            assertNotNull(e.getStartedAt());
        }

        @Test
        void markAsCompletedShouldSetAllCompletionFields() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.markAsCompleted("/exports/file.csv", "https://cdn/file.csv", 4096L, 200, 8);
            assertEquals(DataExport.ExportStatus.COMPLETED, e.getStatus());
            assertNotNull(e.getCompletedAt());
            assertEquals("/exports/file.csv", e.getFilePath());
            assertEquals("https://cdn/file.csv", e.getFileUrl());
            assertEquals(4096L, e.getFileSizeBytes());
            assertEquals(200, e.getRowCount());
            assertEquals(8, e.getColumnCount());
            assertEquals(100, e.getProgressPercentage());
        }

        @Test
        void markAsCompletedWithZeroValues() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.markAsCompleted("/empty.csv", null, 0L, 0, 0);
            assertEquals(DataExport.ExportStatus.COMPLETED, e.getStatus());
            assertEquals(0L, e.getFileSizeBytes());
            assertEquals(0, e.getRowCount());
            assertEquals(100, e.getProgressPercentage());
        }

        @Test
        void markAsFailedShouldSetStatusAndErrorMessage() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.markAsFailed("Connection timeout");
            assertEquals(DataExport.ExportStatus.FAILED, e.getStatus());
            assertEquals("Connection timeout", e.getErrorMessage());
            assertNotNull(e.getCompletedAt());
        }

        @Test
        void markAsFailedWithNullMessage() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.markAsFailed(null);
            assertEquals(DataExport.ExportStatus.FAILED, e.getStatus());
            assertNull(e.getErrorMessage());
        }

        @Test
        void updateProgressShouldClampToRange() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.updateProgress(50);
            assertEquals(50, e.getProgressPercentage());
            e.updateProgress(150);
            assertEquals(100, e.getProgressPercentage());
            e.updateProgress(-10);
            assertEquals(0, e.getProgressPercentage());
        }

        @Test
        void updateProgressAtBoundaries() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.updateProgress(0);
            assertEquals(0, e.getProgressPercentage());
            e.updateProgress(100);
            assertEquals(100, e.getProgressPercentage());
        }
    }

    @Nested
    @DisplayName("Expiration Tests")
    class ExpirationTests {

        @Test
        void isExpiredShouldReturnTrueForPastDate() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.setExpiresAt(LocalDateTime.now().minusHours(1));
            assertTrue(e.isExpired());
        }

        @Test
        void isExpiredShouldReturnFalseForFutureDate() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            e.setExpiresAt(LocalDateTime.now().plusHours(24));
            assertFalse(e.isExpired());
        }

        @Test
        void isExpiredShouldReturnFalseForNullDate() {
            DataExport e = DataExport.builder().exportFormat(DataExport.ExportFormat.CSV).tenantId("t1").build();
            assertFalse(e.isExpired());
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    class EnumTests {

        @Test
        void exportFormatShouldHaveAllValues() {
            assertEquals(6, DataExport.ExportFormat.values().length);
            assertNotNull(DataExport.ExportFormat.valueOf("CSV"));
            assertNotNull(DataExport.ExportFormat.valueOf("EXCEL"));
            assertNotNull(DataExport.ExportFormat.valueOf("JSON"));
            assertNotNull(DataExport.ExportFormat.valueOf("PDF"));
            assertNotNull(DataExport.ExportFormat.valueOf("PARQUET"));
            assertNotNull(DataExport.ExportFormat.valueOf("XML"));
        }

        @Test
        void exportStatusShouldHaveAllValues() {
            assertEquals(6, DataExport.ExportStatus.values().length);
            assertNotNull(DataExport.ExportStatus.valueOf("PENDING"));
            assertNotNull(DataExport.ExportStatus.valueOf("PROCESSING"));
            assertNotNull(DataExport.ExportStatus.valueOf("COMPLETED"));
            assertNotNull(DataExport.ExportStatus.valueOf("FAILED"));
            assertNotNull(DataExport.ExportStatus.valueOf("CANCELLED"));
            assertNotNull(DataExport.ExportStatus.valueOf("EXPIRED"));
        }

        @Test
        void compressionTypeShouldHaveAllValues() {
            assertEquals(4, DataExport.CompressionType.values().length);
            assertNotNull(DataExport.CompressionType.valueOf("NONE"));
            assertNotNull(DataExport.CompressionType.valueOf("ZIP"));
            assertNotNull(DataExport.CompressionType.valueOf("GZIP"));
            assertNotNull(DataExport.CompressionType.valueOf("SNAPPY"));
        }
    }

    @Nested
    @DisplayName("equals/hashCode/toString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        void equalsSameInstance() {
            DataExport e = new DataExport();
            assertEquals(e, e);
        }

        @Test
        void equalsNull() {
            DataExport e = new DataExport();
            assertNotEquals(null, e);
        }

        @Test
        void equalsDifferentType() {
            DataExport e = new DataExport();
            assertNotEquals("string", e);
        }

        @Test
        void equalsEqualFullObjects() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            assertEquals(a, b);
        }

        @Test
        void equalsAllNullFields() {
            DataExport a = new DataExport();
            DataExport b = new DataExport();
            assertEquals(a, b);
        }

        @Test
        void hashCodeEqual() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void hashCodeConsistent() {
            DataExport ds = buildFullExport();
            int h1 = ds.hashCode();
            int h2 = ds.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void toStringNotNull() {
            DataExport e = buildFullExport();
            assertNotNull(e.toString());
            assertTrue(e.toString().contains("Export 1"));
        }

        @Test
        void equalsDiffersById() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByExportName() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setExportName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryDefinition() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByQueryId() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setQueryId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByExportFormat() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setExportFormat(DataExport.ExportFormat.JSON);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByStatus() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setStatus(DataExport.ExportStatus.FAILED);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByFilePath() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setFilePath("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByFileUrl() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setFileUrl("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByFileSizeBytes() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setFileSizeBytes(999L);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByRowCount() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setRowCount(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByColumnCount() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setColumnCount(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCompressionType() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setCompressionType(DataExport.CompressionType.ZIP);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByIncludeHeaders() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setIncludeHeaders(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByFilters() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setFilters("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByRequestedBy() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setRequestedBy("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByTenantId() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setTenantId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByStartedAt() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setStartedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCompletedAt() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setCompletedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByExpiresAt() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setExpiresAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByErrorMessage() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setErrorMessage("error");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByCreatedAt() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByUpdatedAt() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setUpdatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDiffersByProgressPercentage() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setProgressPercentage(50);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullId() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setId(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullExportName() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setExportName(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullFilePath() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setFilePath(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullTenantId() {
            DataExport a = buildFullExport();
            DataExport b = buildFullExport();
            b.setTenantId(null);
            assertNotEquals(a, b);
        }

        @Test
        void equalsNullVsNonNullErrorMessage() {
            DataExport a = buildFullExport();
            a.setErrorMessage(null);
            DataExport b = buildFullExport();
            b.setErrorMessage("error");
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeWithNullFields() {
            DataExport e = new DataExport();
            assertDoesNotThrow(() -> e.hashCode());
        }

        @Test
        void toStringWithNullFields() {
            DataExport e = new DataExport();
            assertDoesNotThrow(() -> e.toString());
        }
    }
}
