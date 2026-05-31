package com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * File Storage use case port.
 */
public interface FileStoragePort {

    /**
     * Uploads a file.
     *
     * @param file the file to upload
     * @param userId the user ID
     * @return the stored file metadata
     */
    FileMetadata uploadFile(MultipartFile file, String userId);

    /**
     * Downloads a file.
     *
     * @param fileId the file ID
     * @return the file input stream and metadata
     */
    FileDownload downloadFile(String fileId);

    /**
     * Deletes a file.
     *
     * @param fileId the file ID
     */
    void deleteFile(String fileId);

    /**
     * Gets file metadata.
     *
     * @param fileId the file ID
     * @return the file metadata
     */
    FileMetadata getFileMetadata(String fileId);

    /**
     * Lists files for a user.
     *
     * @param userId the user ID
     * @return list of file metadata
     */
    List<FileMetadata> listUserFiles(String userId);
}

