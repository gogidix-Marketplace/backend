package com.gogidix.aiservices.aimanagementservice.domain.port.out;

import com.gogidix.aiservices.aimanagementservice.domain.model.MLModel;

import java.util.List;
import java.util.Optional;

/**
 * Output port for model repository operations.
 */
public interface ModelRepositoryPort {

    MLModel save(MLModel model);

    Optional<MLModel> findById(String id);

    Optional<MLModel> findByIdAndTenantId(String id, String tenantId);

    List<MLModel> findByTenantId(String tenantId, int page, int size);

    void deleteById(String id);

    boolean existsByIdAndTenantId(String id, String tenantId);
}
