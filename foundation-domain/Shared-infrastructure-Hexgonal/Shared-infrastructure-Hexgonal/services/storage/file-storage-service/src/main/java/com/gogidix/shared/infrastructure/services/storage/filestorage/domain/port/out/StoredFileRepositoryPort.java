package com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.out;

import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.model.StoredFile;

import java.util.List;
import java.util.Optional;

/**
 * Stored File repository output port.
 */
public interface StoredFileRepositoryPort {

    StoredFile save(StoredFile storedFile);

    Optional<StoredFile> findByIdAndTenantId(String fileId, String tenantId);

    List<StoredFile> findByUserIdAndTenantId(String userId, String tenantId);

    void delete(StoredFile storedFile);
}
