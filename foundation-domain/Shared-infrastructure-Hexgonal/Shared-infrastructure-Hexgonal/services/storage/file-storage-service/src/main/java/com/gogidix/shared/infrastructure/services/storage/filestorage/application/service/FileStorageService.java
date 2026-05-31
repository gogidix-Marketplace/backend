package com.gogidix.shared.infrastructure.services.storage.filestorage.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.model.StoredFile;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileMetadata;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileDownload;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileStoragePort;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.out.StoredFileRepositoryPort;
import com.gogidix.shared.infrastructure.services.storage.filestorage.infrastructure.storage.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * File Storage Service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService implements FileStoragePort {

    private final StoredFileRepositoryPort storedFileRepository;
    private final S3StorageService s3StorageService;
    private final TenantContextHolder tenantContextHolder;

    @Override
    public FileMetadata uploadFile(MultipartFile file, String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Uploading file: name={}, size={}, user={}, tenant={}",
                file.getOriginalFilename(), file.getSize(), userId, tenantId);

        String fileId = UUID.randomUUID().toString();
        String storageKey = buildStorageKey(tenantId, userId, fileId, file.getOriginalFilename());

        StoredFile storedFile = StoredFile.builder()
                .id(fileId)
                .tenantId(new TenantId(tenantId))
                .userId(userId)
                .fileName(fileId)
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .storageType("S3")
                .storagePath(storageKey)
                .status(StoredFile.FileStatus.UPLOADING)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        try {
            String etag = s3StorageService.uploadFile(storageKey, file.getInputStream(), file.getContentType(), file.getSize());
            storedFile.markAsCompleted(storageKey, etag);

            log.info("File uploaded successfully: fileId={}, etag={}", fileId, etag);

        } catch (Exception e) {
            log.error("Failed to upload file: fileId={}", fileId, e);
            storedFile.markAsFailed(e.getMessage());
        }

        storedFileRepository.save(storedFile);

        return new FileMetadata(
                storedFile.getId(),
                storedFile.getOriginalFileName(),
                storedFile.getContentType(),
                storedFile.getContentLength(),
                storedFile.getStoragePath(),
                storedFile.getStatus().name()
        );
    }

    @Override
    public FileDownload downloadFile(String fileId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        StoredFile storedFile = storedFileRepository.findByIdAndTenantId(fileId, tenantId)
                .orElse(null);

        if (storedFile == null || !storedFile.isAccessible()) {
            return null;
        }

        try {
            InputStream inputStream = s3StorageService.downloadFile(storedFile.getStoragePath());

            return new FileDownload(
                    inputStream,
                    new FileMetadata(
                            storedFile.getId(),
                            storedFile.getOriginalFileName(),
                            storedFile.getContentType(),
                            storedFile.getContentLength(),
                            storedFile.getStoragePath(),
                            storedFile.getStatus().name()
                    )
            );

        } catch (Exception e) {
            log.error("Failed to download file: fileId={}", fileId, e);
            return null;
        }
    }

    @Override
    public void deleteFile(String fileId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        StoredFile storedFile = storedFileRepository.findByIdAndTenantId(fileId, tenantId)
                .orElse(null);

        if (storedFile != null) {
            try {
                s3StorageService.deleteFile(storedFile.getStoragePath());
                storedFile.markAsDeleted();
                storedFileRepository.save(storedFile);

                log.info("File deleted: fileId={}", fileId);

            } catch (Exception e) {
                log.error("Failed to delete file: fileId={}", fileId, e);
            }
        }
    }

    @Override
    public FileMetadata getFileMetadata(String fileId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        return storedFileRepository.findByIdAndTenantId(fileId, tenantId)
                .map(f -> new FileMetadata(
                        f.getId(),
                        f.getOriginalFileName(),
                        f.getContentType(),
                        f.getContentLength(),
                        f.getStoragePath(),
                        f.getStatus().name()
                ))
                .orElse(null);
    }

    @Override
    public List<FileMetadata> listUserFiles(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        return storedFileRepository.findByUserIdAndTenantId(userId, tenantId).stream()
                .map(f -> new FileMetadata(
                        f.getId(),
                        f.getOriginalFileName(),
                        f.getContentType(),
                        f.getContentLength(),
                        f.getStoragePath(),
                        f.getStatus().name()
                ))
                .toList();
    }

    /**
     * Builds the S3 storage key for a file.
     */
    private String buildStorageKey(String tenantId, String userId, String fileId, String fileName) {
        return String.format("%s/%s/%s/%s", tenantId, userId, fileId, fileName);
    }
}
