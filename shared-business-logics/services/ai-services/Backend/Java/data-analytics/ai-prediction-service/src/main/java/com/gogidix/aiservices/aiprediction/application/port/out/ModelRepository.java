package com.gogidix.aiservices.aiprediction.application.port.out;

import com.gogidix.aiservices.aiprediction.domain.ModelMetadata;

import java.util.Optional;

/**
 * Repository port for model metadata.
 */
public interface ModelRepository {

    /**
     * Finds a model by ID.
     */
    Optional<ModelMetadata> findById(String modelId);

    /**
     * Saves a model.
     */
    ModelMetadata save(ModelMetadata model);

    /**
     * Deletes a model by ID.
     */
    void deleteById(String modelId);
}
