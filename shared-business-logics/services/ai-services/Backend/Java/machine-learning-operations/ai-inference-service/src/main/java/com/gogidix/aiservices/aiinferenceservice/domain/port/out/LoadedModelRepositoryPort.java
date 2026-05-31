package com.gogidix.aiservices.aiinferenceservice.domain.port.out;

import com.gogidix.aiservices.aiinferenceservice.domain.model.LoadedModel;

import java.util.Optional;

/**
 * Output port for loaded model repository operations.
 */
public interface LoadedModelRepositoryPort {

    LoadedModel save(LoadedModel model);

    Optional<LoadedModel> findByModelIdAndTenantId(String modelId, String tenantId);

    void deleteByModelIdAndTenantId(String modelId, String tenantId);
}
