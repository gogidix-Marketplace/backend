package com.gogidix.aiservices.aiinferenceservice.application.service;

import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceRequestDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceResponseDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.ModelStatusResponseDto;
import com.gogidix.aiservices.aiinferenceservice.domain.model.InferenceResult;
import com.gogidix.aiservices.aiinferenceservice.domain.model.LoadedModel;
import com.gogidix.aiservices.aiinferenceservice.domain.model.ModelStatus;
import com.gogidix.aiservices.aiinferenceservice.domain.port.in.InferenceServicePort;
import com.gogidix.aiservices.aiinferenceservice.domain.port.out.InferenceResultRepositoryPort;
import com.gogidix.aiservices.aiinferenceservice.domain.port.out.LoadedModelRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Application service for inference operations.
 */
@Service
@Transactional
public class InferenceApplicationService implements InferenceServicePort {

    private static final Logger log = LoggerFactory.getLogger(InferenceApplicationService.class);

    private final InferenceResultRepositoryPort resultRepository;
    private final LoadedModelRepositoryPort loadedModelRepository;

    public InferenceApplicationService(
            InferenceResultRepositoryPort resultRepository,
            LoadedModelRepositoryPort loadedModelRepository) {
        this.resultRepository = resultRepository;
        this.loadedModelRepository = loadedModelRepository;
    }

    @Override
    public InferenceResponseDto runInference(InferenceRequestDto request) {
        String tenantId = "tenant-" + UUID.randomUUID().toString().substring(0, 8);
        log.info("Running inference for modelId: {}", request.modelId());

        long startTime = System.currentTimeMillis();

        LoadedModel loadedModel = loadedModelRepository
                .findByModelIdAndTenantId(request.modelId(), tenantId)
                .orElseGet(() -> {
                    LoadedModel model = new LoadedModel(tenantId, request.modelId());
                    model.markAsLoaded();
                    return loadedModelRepository.save(model);
                });

        if (loadedModel.isExpired()) {
            loadedModelRepository.deleteByModelIdAndTenantId(request.modelId(), tenantId);
            loadedModel = new LoadedModel(tenantId, request.modelId());
            loadedModel.markAsLoaded();
            loadedModelRepository.save(loadedModel);
        }

        InferenceResult result = new InferenceResult(tenantId, request.modelId(), request.inputData());
        List<InferenceResult.Prediction> predictions = generatePredictions(request.inputData());
        long latency = System.currentTimeMillis() - startTime;

        result.setPredictions(predictions, latency);
        resultRepository.save(result);

        log.info("Inference completed with latency: {}ms", latency);

        return new InferenceResponseDto(
                result.getInferenceId(),
                request.modelId(),
                predictions,
                latency,
                result.getExecutedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ModelStatusResponseDto getModelStatus(String modelId, String tenantId) {
        return loadedModelRepository
                .findByModelIdAndTenantId(modelId, tenantId)
                .map(model -> new ModelStatusResponseDto(
                        model.getModelId(),
                        model.getStatus(),
                        model.getLoadedAt(),
                        model.getCacheExpiry()
                ))
                .orElse(new ModelStatusResponseDto(modelId, ModelStatus.UNLOADED, null, null));
    }

    @Override
    public void unloadModel(String modelId, String tenantId) {
        loadedModelRepository.deleteByModelIdAndTenantId(modelId, tenantId);
        log.info("Unloaded model: {}", modelId);
    }

    private List<InferenceResult.Prediction> generatePredictions(Object inputData) {
        return List.of(
                new InferenceResult.Prediction("class_0", 0.85, "prediction_value"),
                new InferenceResult.Prediction("class_1", 0.15, "other_value")
        );
    }
}
