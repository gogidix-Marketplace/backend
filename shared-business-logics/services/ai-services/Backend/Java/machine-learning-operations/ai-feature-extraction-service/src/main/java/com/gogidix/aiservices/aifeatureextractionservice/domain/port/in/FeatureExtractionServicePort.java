package com.gogidix.aiservices.aifeatureextractionservice.domain.port.in;

import com.gogidix.aiservices.aifeatureextractionservice.application.dto.ExtractFeaturesRequestDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSetResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSchemaResponseDto;

import java.util.List;

/**
 * Input port for feature extraction operations.
 * This interface defines the contract for the application service layer.
 */
public interface FeatureExtractionServicePort {

    /**
     * Extract features from a data source.
     *
     * @param request the extraction request
     * @return the feature set response
     */
    FeatureSetResponseDto extractFeatures(ExtractFeaturesRequestDto request);

    /**
     * Get feature schema by feature set ID.
     *
     * @param featureSetId the feature set ID
     * @param tenantId the tenant ID
     * @return the feature schema response
     */
    FeatureSchemaResponseDto getFeatureSchema(String featureSetId, String tenantId);

    /**
     * Get feature set by ID.
     *
     * @param featureSetId the feature set ID
     * @param tenantId the tenant ID
     * @return the feature set response
     */
    FeatureSetResponseDto getFeatureSet(String featureSetId, String tenantId);

    /**
     * List feature sets for a tenant.
     *
     * @param tenantId the tenant ID
     * @param dataSource optional data source filter
     * @param status optional status filter
     * @param page page number (0-based)
     * @param size page size
     * @return list of feature set responses
     */
    List<FeatureSetResponseDto> listFeatureSets(String tenantId, String dataSource, String status, int page, int size);

    /**
     * Delete a feature set.
     *
     * @param featureSetId the feature set ID
     * @param tenantId the tenant ID
     */
    void deleteFeatureSet(String featureSetId, String tenantId);
}
