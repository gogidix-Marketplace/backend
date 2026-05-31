package com.gogidix.aiservices.aiprediction.infrastructure;

import com.gogidix.aiservices.aiprediction.application.port.out.ModelRepository;
import com.gogidix.aiservices.aiprediction.domain.ModelMetadata;
import com.gogidix.aiservices.aiprediction.domain.PredictionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ModelRepository.
 */
@Repository
@RequiredArgsConstructor
public class PredictionRepositoryImpl implements ModelRepository {

    private final Map<String, PredictionResult> predictions = new ConcurrentHashMap<>();
    private final Map<String, ModelMetadata> models = new ConcurrentHashMap<>();

    // ModelRepository methods
    @Override
    public Optional<ModelMetadata> findById(String modelId) {
        return Optional.ofNullable(models.get(modelId));
    }

    @Override
    public ModelMetadata save(ModelMetadata model) {
        models.put(model.getModelId(), model);
        return model;
    }

    @Override
    public void deleteById(String modelId) {
        models.remove(modelId);
    }

    /**
     * Initialize with default models for testing.
     */
    public void initializeDefaultModels() {
        models.put("model-001", new ModelMetadata(
            "model-001",
            "default_regression_model",
            ModelMetadata.ModelType.REGRESSION,
            "1.0.0",
            LocalDateTime.now()
        ));

        models.put("model-002", new ModelMetadata(
            "model-002",
            "default_classification_model",
            ModelMetadata.ModelType.CLASSIFICATION,
            "1.0.0",
            LocalDateTime.now()
        ));
    }
}
