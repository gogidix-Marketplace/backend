package com.gogidix.shared.infrastructure.services.storage.filestorage.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Stored File domain entity.
 * <p>
 * Represents a file stored in the system (S3, local, etc.).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stored_files")
public class StoredFile {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed
    private String userId;

    private String fileName;

    private String originalFileName;

    private String contentType;

    private Long contentLength;

    private String storageType; // S3, LOCAL, AZURE_BLOB, etc.

    private String storagePath; // S3 key or local file path

    private String bucketName; // For S3

    private String etag;

    private FileStatus status;

    private String errorMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum FileStatus {
        UPLOADING,
        COMPLETED,
        FAILED,
        DELETED
    }

    /**
     * Marks the file as completed.
     *
     * @param storagePath the storage path
     * @param etag the file etag
     */
    public void markAsCompleted(String storagePath, String etag) {
        this.status = FileStatus.COMPLETED;
        this.storagePath = storagePath;
        this.etag = etag;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the file as failed.
     *
     * @param errorMessage the error message
     */
    public void markAsFailed(String errorMessage) {
        this.status = FileStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the file as deleted.
     */
    public void markAsDeleted() {
        this.status = FileStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the file is accessible.
     *
     * @return true if the file can be accessed
     */
    public boolean isAccessible() {
        return status == FileStatus.COMPLETED;
    }
}
