package com.gogidix.aiservices.aifeatureextractionservice.domain.port.out;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;

import java.util.List;

/**
 * Output port for feature store integration.
 * This is the external service abstraction for storing extracted features.
 */
public interface FeatureStoreClientPort {

    /**
     * Store features in the feature store.
     *
     * @param featureSetId the feature set ID
     * @param tenantId the tenant ID
     * @param features the features to store
     * @return true if stored successfully, false otherwise
     */
    boolean storeFeatures(String featureSetId, String tenantId, List<FeatureValue> features);

    /**
     * Retrieve features from the feature store.
     *
     * @param featureSetName the feature set name
     * @param entityIds the entity IDs
     * @return the retrieved features
     */
    List<FeatureValue> retrieveFeatures(String featureSetName, List<String> entityIds);
}
