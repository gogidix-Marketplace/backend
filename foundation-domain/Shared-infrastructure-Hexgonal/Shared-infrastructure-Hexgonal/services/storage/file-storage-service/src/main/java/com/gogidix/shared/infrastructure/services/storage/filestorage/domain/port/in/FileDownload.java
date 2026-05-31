package com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in;

import java.io.InputStream;

/**
 * File download record.
 */
public record FileDownload(
        InputStream inputStream,
        FileMetadata metadata
) {}