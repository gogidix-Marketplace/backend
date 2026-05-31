package com.gogidix.aiservices.aiinferenceservice.domain.port.out;

import com.gogidix.aiservices.aiinferenceservice.domain.model.InferenceResult;

import java.util.List;
import java.util.Optional;

/**
 * Output port for inference result repository operations.
 */
public interface InferenceResultRepositoryPort {

    InferenceResult save(InferenceResult result);

    Optional<InferenceResult> findById(String id);

    List<InferenceResult> findByTenantId(String tenantId, int page, int size);

    void deleteById(String id);
}
