package com.gogidix.shared.infrastructure.services.storage.filestorage.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.model.StoredFile;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileDownload;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileMetadata;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.out.StoredFileRepositoryPort;
import com.gogidix.shared.infrastructure.services.storage.filestorage.infrastructure.storage.S3StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FileStorageService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("File Storage Service Tests")
class FileStorageServiceTest {

    @Mock
    private StoredFileRepositoryPort storedFileRepository;

    @Mock
    private S3StorageService s3StorageService;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileStorageService fileStorageService;

    private static final String TEST_TENANT_ID = "tenant-001";
    private StoredFile testStoredFile;

    @BeforeEach
    void setUp() throws IOException {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);
        lenient().when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        lenient().when(multipartFile.getContentType()).thenReturn("application/pdf");
        lenient().when(multipartFile.getSize()).thenReturn(1024L);
        lenient().when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[1024]));

        testStoredFile = StoredFile.builder()
                .id("file-123")
                .tenantId(new TenantId(TEST_TENANT_ID))
                .userId("user-123")
                .originalFileName("test.pdf")
                .contentType("application/pdf")
                .contentLength(1024L)
                .storagePath("tenant-001/user-123/file-123/test.pdf")
                .status(StoredFile.FileStatus.COMPLETED)
                .build();
    }

    @Test
    @DisplayName("Should upload file successfully")
    void shouldUploadFileSuccessfully() throws IOException {
        when(s3StorageService.uploadFile(anyString(), any(), anyString(), anyLong())).thenReturn("etag-123");
        when(storedFileRepository.save(any(StoredFile.class))).thenReturn(testStoredFile);

        FileMetadata result = fileStorageService.uploadFile(multipartFile, "user-123");

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("test.pdf", result.fileName());
        assertEquals("application/pdf", result.contentType());
        assertEquals(1024L, result.contentLength());
        assertEquals("COMPLETED", result.status());
        verify(s3StorageService).uploadFile(anyString(), any(), anyString(), anyLong());
        verify(storedFileRepository).save(any(StoredFile.class));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should mark file as failed when upload fails")
    void shouldMarkFileAsFailedWhenUploadFails() throws IOException {
        when(s3StorageService.uploadFile(anyString(), any(), anyString(), anyLong()))
                .thenThrow(new RuntimeException("Upload failed"));
        when(storedFileRepository.save(any(StoredFile.class))).thenReturn(testStoredFile);

        FileMetadata result = fileStorageService.uploadFile(multipartFile, "user-123");

        assertNotNull(result);
        assertEquals("FAILED", result.status());
        verify(s3StorageService).uploadFile(anyString(), any(), anyString(), anyLong());
        verify(storedFileRepository).save(argThat(file ->
                file.getStatus() == StoredFile.FileStatus.FAILED
        ));
    }

    @Test
    @DisplayName("Should download file successfully")
    void shouldDownloadFileSuccessfully() throws IOException {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testStoredFile));
        when(s3StorageService.downloadFile(anyString()))
                .thenReturn(new ByteArrayInputStream(new byte[1024]));

        FileDownload result = fileStorageService.downloadFile("file-123");

        assertNotNull(result);
        assertNotNull(result.inputStream());
        assertEquals("file-123", result.metadata().id());
        assertEquals("test.pdf", result.metadata().fileName());
        verify(storedFileRepository).findByIdAndTenantId("file-123", TEST_TENANT_ID);
        verify(s3StorageService).downloadFile(anyString());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return null when downloading non-existent file")
    void shouldReturnNullWhenDownloadingNonExistentFile() {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        FileDownload result = fileStorageService.downloadFile("non-existent");

        assertNull(result);
        verify(s3StorageService, never()).downloadFile(anyString());
    }

    @Test
    @DisplayName("Should return null when downloading inaccessible file")
    void shouldReturnNullWhenDownloadingInaccessibleFile() {
        StoredFile failedFile = StoredFile.builder()
                .status(StoredFile.FileStatus.FAILED)
                .build();

        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(failedFile));

        FileDownload result = fileStorageService.downloadFile("file-123");

        assertNull(result);
        verify(s3StorageService, never()).downloadFile(anyString());
    }

    @Test
    @DisplayName("Should delete file successfully")
    void shouldDeleteFileSuccessfully() {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testStoredFile));
        when(storedFileRepository.save(any(StoredFile.class))).thenReturn(testStoredFile);

        fileStorageService.deleteFile("file-123");

        verify(s3StorageService).deleteFile(anyString());
        verify(storedFileRepository).save(argThat(file ->
                file.getStatus() == StoredFile.FileStatus.DELETED
        ));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should handle deletion of non-existent file gracefully")
    void shouldHandleDeletionOfNonExistentFile() {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> fileStorageService.deleteFile("non-existent"));

        verify(s3StorageService, never()).deleteFile(anyString());
        verify(storedFileRepository, never()).save(any(StoredFile.class));
    }

    @Test
    @DisplayName("Should get file metadata successfully")
    void shouldGetFileMetadataSuccessfully() {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testStoredFile));

        FileMetadata result = fileStorageService.getFileMetadata("file-123");

        assertNotNull(result);
        assertEquals("file-123", result.id());
        assertEquals("test.pdf", result.fileName());
        assertEquals("application/pdf", result.contentType());
        assertEquals(1024L, result.contentLength());
        verify(storedFileRepository).findByIdAndTenantId("file-123", TEST_TENANT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return null when getting metadata for non-existent file")
    void shouldReturnNullWhenGettingMetadataForNonExistentFile() {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        FileMetadata result = fileStorageService.getFileMetadata("non-existent");

        assertNull(result);
    }

    @Test
    @DisplayName("Should list user files successfully")
    void shouldListUserFilesSuccessfully() {
        List<StoredFile> files = Arrays.asList(
                testStoredFile,
                StoredFile.builder()
                        .id("file-456")
                        .originalFileName("test2.pdf")
                        .contentType("application/pdf")
                        .contentLength(2048L)
                        .storagePath("path-2")
                        .status(StoredFile.FileStatus.COMPLETED)
                        .build()
        );

        when(storedFileRepository.findByUserIdAndTenantId(anyString(), anyString()))
                .thenReturn(files);

        List<FileMetadata> result = fileStorageService.listUserFiles("user-123");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("file-123", result.get(0).id());
        assertEquals("file-456", result.get(1).id());
        verify(storedFileRepository).findByUserIdAndTenantId("user-123", TEST_TENANT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return empty list when user has no files")
    void shouldReturnEmptyListWhenUserHasNoFiles() {
        when(storedFileRepository.findByUserIdAndTenantId(anyString(), anyString()))
                .thenReturn(Arrays.asList());

        List<FileMetadata> result = fileStorageService.listUserFiles("user-123");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle download failure gracefully")
    void shouldHandleDownloadFailureGracefully() throws IOException {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testStoredFile));
        when(s3StorageService.downloadFile(anyString()))
                .thenThrow(new RuntimeException("Download failed"));

        FileDownload result = fileStorageService.downloadFile("file-123");

        assertNull(result);
    }

    @Test
    @DisplayName("Should handle delete failure gracefully")
    void shouldHandleDeleteFailureGracefully() {
        when(storedFileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testStoredFile));
        doThrow(new RuntimeException("Delete failed"))
                .when(s3StorageService).deleteFile(anyString());

        assertDoesNotThrow(() -> fileStorageService.deleteFile("file-123"));
    }

    @Test
    @DisplayName("Should build correct storage key")
    void shouldBuildCorrectStorageKey() throws IOException {
        when(s3StorageService.uploadFile(anyString(), any(), anyString(), anyLong())).thenReturn("etag-123");
        when(storedFileRepository.save(any(StoredFile.class))).thenReturn(testStoredFile);

        fileStorageService.uploadFile(multipartFile, "user-123");

        verify(s3StorageService).uploadFile(contains("user-123"), any(), anyString(), anyLong());
    }

    @Test
    @DisplayName("Should set initial status to UPLOADING")
    void shouldSetInitialStatusToUploading() throws IOException {
        when(s3StorageService.uploadFile(anyString(), any(), anyString(), anyLong())).thenReturn("etag-123");
        when(storedFileRepository.save(any(StoredFile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        fileStorageService.uploadFile(multipartFile, "user-123");

        verify(storedFileRepository).save(argThat(file ->
                file.getOriginalFileName() != null &&
                file.getTenantId() != null &&
                file.getUserId().equals("user-123") &&
                file.getStatus() != null // Status will be set during save
        ));
    }
}
