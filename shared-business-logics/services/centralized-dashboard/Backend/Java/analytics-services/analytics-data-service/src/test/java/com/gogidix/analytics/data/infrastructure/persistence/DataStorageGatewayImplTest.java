package com.gogidix.analytics.data.infrastructure.persistence;

import com.gogidix.analytics.data.domain.model.DataExport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DataStorageGatewayImpl Tests")
class DataStorageGatewayImplTest {

    private DataStorageGatewayImpl gateway;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        gateway = new DataStorageGatewayImpl();
    }

    private DataExport buildExport() {
        return DataExport.builder()
            .id("export-1")
            .exportName("Test Export")
            .exportFormat(DataExport.ExportFormat.CSV)
            .tenantId("tenant-1")
            .status(DataExport.ExportStatus.PENDING)
            .build();
    }

    @Nested
    @DisplayName("storeExportFile Tests")
    class StoreExportFileTests {

        @Test
        @DisplayName("Should store file and return path")
        void shouldStoreFile() {
            DataExport export = buildExport();
            byte[] data = "id,name\n1,test\n".getBytes();

            String path = gateway.storeExportFile(export, data);

            assertNotNull(path);
            assertTrue(path.contains("export-1.csv"));
        }

        @Test
        @DisplayName("Should store different formats")
        void shouldStoreDifferentFormats() {
            DataExport export = buildExport();
            export.setExportFormat(DataExport.ExportFormat.JSON);
            byte[] data = "{}".getBytes();

            String path = gateway.storeExportFile(export, data);

            assertTrue(path.contains("export-1.json"));
        }
    }

    @Nested
    @DisplayName("retrieveExportFile Tests")
    class RetrieveExportFileTests {

        @Test
        @DisplayName("Should retrieve stored file")
        void shouldRetrieveStoredFile() throws IOException {
            Path testFile = tempDir.resolve("test-file.csv");
            byte[] originalData = "hello,world".getBytes();
            Files.write(testFile, originalData);

            byte[] data = gateway.retrieveExportFile(testFile.toString());

            assertArrayEquals(originalData, data);
        }

        @Test
        @DisplayName("Should throw for non-existent file")
        void shouldThrowForNonExistentFile() {
            assertThrows(RuntimeException.class,
                () -> gateway.retrieveExportFile("/non/existent/path.csv"));
        }
    }

    @Nested
    @DisplayName("getDownloadUrl Tests")
    class GetDownloadUrlTests {

        @Test
        @DisplayName("Should return download URL")
        void shouldReturnDownloadUrl() {
            String url = gateway.getDownloadUrl("/exports/file.csv", 3600);

            assertNotNull(url);
            assertTrue(url.contains("file"));
        }

        @Test
        @DisplayName("Should handle different expiry times")
        void shouldHandleDifferentExpiry() {
            String url1 = gateway.getDownloadUrl("/exports/file.csv", 3600);
            String url2 = gateway.getDownloadUrl("/exports/file.csv", 86400);

            assertNotNull(url1);
            assertNotNull(url2);
        }
    }

    @Nested
    @DisplayName("deleteExportFile Tests")
    class DeleteExportFileTests {

        @Test
        @DisplayName("Should delete existing file")
        void shouldDeleteExistingFile() throws IOException {
            Path testFile = tempDir.resolve("delete-test.csv");
            Files.write(testFile, "data".getBytes());
            assertTrue(Files.exists(testFile));

            gateway.deleteExportFile(testFile.toString());

            assertFalse(Files.exists(testFile));
        }

        @Test
        @DisplayName("Should handle non-existent file gracefully")
        void shouldHandleNonExistentFile() {
            assertDoesNotThrow(() -> gateway.deleteExportFile("/non/existent/file.csv"));
        }
    }

    @Nested
    @DisplayName("getExportMetadata Tests")
    class GetExportMetadataTests {

        @Test
        @DisplayName("Should return metadata for existing file")
        void shouldReturnMetadataForExistingFile() throws IOException {
            Path testFile = tempDir.resolve("metadata-test.csv");
            byte[] data = "test data content".getBytes();
            Files.write(testFile, data);

            Map<String, Object> metadata = gateway.getExportMetadata(testFile.toString());

            assertEquals(testFile.toString(), metadata.get("filePath"));
            assertEquals(true, metadata.get("exists"));
            assertEquals((long) data.length, metadata.get("size"));
            assertNotNull(metadata.get("lastModified"));
        }

        @Test
        @DisplayName("Should return metadata for non-existent file")
        void shouldReturnMetadataForNonExistentFile() {
            Map<String, Object> metadata = gateway.getExportMetadata("/non/existent/file.csv");

            assertEquals("/non/existent/file.csv", metadata.get("filePath"));
            assertEquals(false, metadata.get("exists"));
            assertEquals(0L, metadata.get("size"));
            assertNull(metadata.get("lastModified"));
        }
    }

    @Nested
    @DisplayName("storeExportFile failure Tests")
    class StoreExportFileFailureTests {

        @Test
        @DisplayName("Should throw RuntimeException on IOException")
        void shouldThrowRuntimeExceptionOnIOException() {
            DataExport export = buildExport();
            export.setTenantId("../../\u0000invalid");
            byte[] data = "data".getBytes();

            assertThrows(RuntimeException.class, () -> gateway.storeExportFile(export, data));
        }
    }

    @Nested
    @DisplayName("retrieveExportFile failure Tests")
    class RetrieveExportFileFailureTests {

        @Test
        @DisplayName("Should throw RuntimeException with cause")
        void shouldThrowRuntimeExceptionWithCause() {
            RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gateway.retrieveExportFile("/non/existent/path.csv"));
            assertNotNull(ex.getCause());
        }
    }

    @Nested
    @DisplayName("deleteExportFile IOException Tests")
    class DeleteExportFileIOExceptionTests {

        @Test
        @DisplayName("Should handle IOException on delete gracefully")
        void shouldHandleIOExceptionOnDelete() {
            assertDoesNotThrow(() -> gateway.deleteExportFile("/non/existent/deeply/nested/path/file.csv"));
        }
    }
}
