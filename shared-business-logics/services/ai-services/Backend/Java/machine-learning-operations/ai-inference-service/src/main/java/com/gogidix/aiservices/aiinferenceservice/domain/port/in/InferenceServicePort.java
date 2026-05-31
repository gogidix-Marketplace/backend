package com.gogidix.aiservices.aiinferenceservice.domain.port.in;

import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceRequestDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceResponseDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.ModelStatusResponseDto;

/**
 * Input port for inference operations.
 */
public interface InferenceServicePort {

    /**
     * Run inference on input data.
     */
    InferenceResponseDto runInference(InferenceRequestDto request);

    /**
     * Get model status.
     */
    ModelStatusResponseDto getModelStatus(String modelId, String tenantId);

    /**
     * Unload model from cache.
     */
    void unloadModel(String modelId, String tenantId);
}
