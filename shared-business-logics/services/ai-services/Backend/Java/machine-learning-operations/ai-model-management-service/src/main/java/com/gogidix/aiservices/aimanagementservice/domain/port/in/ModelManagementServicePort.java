package com.gogidix.aiservices.aimanagementservice.domain.port.in;

import com.gogidix.aiservices.aimanagementservice.application.dto.*;
import java.util.List;

/**
 * Input port for model management operations.
 */
public interface ModelManagementServicePort {

    /**
     * Register a new model.
     */
    RegisterModelResponseDto registerModel(RegisterModelRequestDto request);

    /**
     * Deploy a model.
     */
    void deployModel(String modelId, String tenantId);

    /**
     * Undeploy a model.
     */
    void undeployModel(String modelId, String tenantId);

    /**
     * Get model by ID.
     */
    ModelResponseDto getModel(String modelId, String tenantId);

    /**
     * List models for tenant.
     */
    List<ModelResponseDto> listModels(String tenantId, int page, int size);

    /**
     * Archive a model.
     */
    void archiveModel(String modelId, String tenantId);
}
