package com.gogidix.aiservices.aifeaturestoreservice.domain.port.in;

import com.gogidix.aiservices.aifeaturestoreservice.application.dto.*;
import java.util.List;

/**
 * Input port for feature store operations.
 */
public interface FeatureStoreServicePort {

    /**
     * Store features for entities.
     */
    StoreFeaturesResponseDto storeFeatures(StoreFeaturesRequestDto request);

    /**
     * Get features for a specific entity.
     */
    List<EntityFeatureDto> getFeatures(String featureName, String entityId, String tenantId);

    /**
     * Get feature definition.
     */
    FeatureDefinitionResponseDto getFeatureDefinition(String featureName, String tenantId);

    /**
     * List feature definitions for a tenant.
     */
    List<FeatureDefinitionResponseDto> listFeatureDefinitions(String tenantId, int page, int size);

    /**
     * Delete feature definition.
     */
    void deleteFeatureDefinition(String featureName, String tenantId);
}
