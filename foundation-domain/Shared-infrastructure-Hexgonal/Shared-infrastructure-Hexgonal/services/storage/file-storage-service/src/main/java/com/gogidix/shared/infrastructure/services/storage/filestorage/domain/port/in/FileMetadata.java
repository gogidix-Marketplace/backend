package com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in;

import java.io.InputStream;

/**
 * File metadata record.
 */
public record FileMetadata(
        String id,
        String fileName,
        String contentType,
        Long contentLength,
        String storagePath,
        String status
) {}

