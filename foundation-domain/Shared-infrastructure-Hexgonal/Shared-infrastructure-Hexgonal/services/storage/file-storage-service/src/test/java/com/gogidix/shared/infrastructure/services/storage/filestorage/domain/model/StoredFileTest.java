package com.gogidix.shared.infrastructure.services.storage.filestorage.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StoredFile domain model.
 */
@DisplayName("StoredFile Domain Model Tests")
class StoredFileTest {

    @Test
    @DisplayName("Should create stored file with builder")
    void shouldCreateStoredFileWithBuilder() {
        StoredFile file = StoredFile.builder()
                .id("file-123")
                .tenantId(TenantId.of("tenant-001"))
                .userId("user-123")
                .fileName("stored-file-123")
                .originalFileName("document.pdf")
                .contentType("application/pdf")
                .contentLength(1024000L)
                .storageType("S3")
                .storagePath("tenant-001/user-123/file-123/document.pdf")
                .bucketName("my-bucket")
                .etag("abc-123")
                .status(StoredFile.FileStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        assertEquals("file-123", file.getId());
        assertEquals("tenant-001", file.getTenantId().getValue());
        assertEquals("user-123", file.getUserId());
        assertEquals("stored-file-123", file.getFileName());
        assertEquals("document.pdf", file.getOriginalFileName());
        assertEquals("application/pdf", file.getContentType());
        assertEquals(1024000L, file.getContentLength());
        assertEquals("S3", file.getStorageType());
        assertEquals("tenant-001/user-123/file-123/document.pdf", file.getStoragePath());
        assertEquals("my-bucket", file.getBucketName());
        assertEquals("abc-123", file.getEtag());
        assertEquals(StoredFile.FileStatus.COMPLETED, file.getStatus());
        assertNotNull(file.getCreatedAt());
    }

    @Test
    @DisplayName("Should create stored file with default values")
    void shouldCreateStoredFileWithDefaults() {
        StoredFile file = new StoredFile();

        assertNull(file.getId());
        assertNull(file.getTenantId());
        assertNull(file.getUserId());
        assertNull(file.getFileName());
        assertNull(file.getOriginalFileName());
        assertNull(file.getContentType());
        assertNull(file.getContentLength());
        assertNull(file.getStorageType());
        assertNull(file.getStoragePath());
        assertNull(file.getBucketName());
        assertNull(file.getEtag());
        assertNull(file.getStatus());
        assertNull(file.getErrorMessage());
        assertNull(file.getCreatedAt());
        assertNull(file.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        StoredFile file = new StoredFile();

        file.setId("id-456");
        file.setTenantId(TenantId.of("tenant-789"));
        file.setUserId("user-456");
        file.setFileName("file-name");
        file.setOriginalFileName("original.pdf");
        file.setContentType("application/pdf");
        file.setContentLength(2048L);
        file.setStorageType("LOCAL");
        file.setStoragePath("/path/to/file");
        file.setBucketName("bucket");
        file.setEtag("etag-123");
        file.setStatus(StoredFile.FileStatus.UPLOADING);
        file.setErrorMessage("Error message");
        file.setCreatedAt(LocalDateTime.now());
        file.setUpdatedAt(LocalDateTime.now());

        assertEquals("id-456", file.getId());
        assertEquals("tenant-789", file.getTenantId().getValue());
        assertEquals("user-456", file.getUserId());
        assertEquals("file-name", file.getFileName());
        assertEquals("original.pdf", file.getOriginalFileName());
        assertEquals("application/pdf", file.getContentType());
        assertEquals(2048L, file.getContentLength());
        assertEquals("LOCAL", file.getStorageType());
        assertEquals("/path/to/file", file.getStoragePath());
        assertEquals("bucket", file.getBucketName());
        assertEquals("etag-123", file.getEtag());
        assertEquals(StoredFile.FileStatus.UPLOADING, file.getStatus());
        assertEquals("Error message", file.getErrorMessage());
        assertNotNull(file.getCreatedAt());
        assertNotNull(file.getUpdatedAt());
    }

    @Test
    @DisplayName("Should mark file as completed")
    void shouldMarkFileAsCompleted() {
        StoredFile file = StoredFile.builder()
                .status(StoredFile.FileStatus.UPLOADING)
                .build();

        file.markAsCompleted("new-path", "etag-456");

        assertEquals(StoredFile.FileStatus.COMPLETED, file.getStatus());
        assertEquals("new-path", file.getStoragePath());
        assertEquals("etag-456", file.getEtag());
        assertNotNull(file.getUpdatedAt());
    }

    @Test
    @DisplayName("Should mark file as failed")
    void shouldMarkFileAsFailed() {
        StoredFile file = StoredFile.builder()
                .status(StoredFile.FileStatus.UPLOADING)
                .build();

        file.markAsFailed("Upload failed");

        assertEquals(StoredFile.FileStatus.FAILED, file.getStatus());
        assertEquals("Upload failed", file.getErrorMessage());
        assertNotNull(file.getUpdatedAt());
    }

    @Test
    @DisplayName("Should mark file as deleted")
    void shouldMarkFileAsDeleted() {
        StoredFile file = StoredFile.builder()
                .status(StoredFile.FileStatus.COMPLETED)
                .build();

        file.markAsDeleted();

        assertEquals(StoredFile.FileStatus.DELETED, file.getStatus());
        assertNotNull(file.getUpdatedAt());
    }

    @Test
    @DisplayName("Should return true for accessible file")
    void shouldReturnTrueForAccessibleFile() {
        StoredFile file = StoredFile.builder()
                .status(StoredFile.FileStatus.COMPLETED)
                .build();

        assertTrue(file.isAccessible());
    }

    @Test
    @DisplayName("Should return false for non-accessible file statuses")
    void shouldReturnFalseForNonAccessibleFileStatuses() {
        StoredFile uploadingFile = StoredFile.builder()
                .status(StoredFile.FileStatus.UPLOADING)
                .build();

        StoredFile failedFile = StoredFile.builder()
                .status(StoredFile.FileStatus.FAILED)
                .build();

        StoredFile deletedFile = StoredFile.builder()
                .status(StoredFile.FileStatus.DELETED)
                .build();

        assertFalse(uploadingFile.isAccessible());
        assertFalse(failedFile.isAccessible());
        assertFalse(deletedFile.isAccessible());
    }

    @Test
    @DisplayName("Should handle all FileStatus enum values")
    void shouldHandleAllFileStatusEnums() {
        assertEquals(4, StoredFile.FileStatus.values().length);
        assertEquals(StoredFile.FileStatus.UPLOADING, StoredFile.FileStatus.valueOf("UPLOADING"));
        assertEquals(StoredFile.FileStatus.COMPLETED, StoredFile.FileStatus.valueOf("COMPLETED"));
        assertEquals(StoredFile.FileStatus.FAILED, StoredFile.FileStatus.valueOf("FAILED"));
        assertEquals(StoredFile.FileStatus.DELETED, StoredFile.FileStatus.valueOf("DELETED"));
    }

    @Test
    @DisplayName("Should create stored file with all args constructor")
    void shouldCreateStoredFileWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        StoredFile file = new StoredFile(
                "id-123",
                TenantId.of("tenant-001"),
                "user-123",
                "file-name",
                "original.pdf",
                "application/pdf",
                1024L,
                "S3",
                "path",
                "bucket",
                "etag",
                StoredFile.FileStatus.COMPLETED,
                null,
                now,
                now
        );

        assertEquals("id-123", file.getId());
        assertEquals("tenant-001", file.getTenantId().getValue());
        assertEquals("user-123", file.getUserId());
        assertEquals("file-name", file.getFileName());
        assertEquals(StoredFile.FileStatus.COMPLETED, file.getStatus());
    }

    @Test
    @DisplayName("Should update storage path when marking as completed")
    void shouldUpdateStoragePathWhenMarkingAsCompleted() {
        StoredFile file = StoredFile.builder()
                .storagePath("old-path")
                .build();

        file.markAsCompleted("new-path", "new-etag");

        assertEquals("new-path", file.getStoragePath());
        assertEquals("new-etag", file.getEtag());
    }
}
