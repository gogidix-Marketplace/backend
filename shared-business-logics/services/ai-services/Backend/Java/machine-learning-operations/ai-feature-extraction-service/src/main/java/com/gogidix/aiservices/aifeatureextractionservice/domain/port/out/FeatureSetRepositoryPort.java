package com.gogidix.aiservices.aifeatureextractionservice.domain.port.out;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;

import java.util.List;
import java.util.Optional;

/**
 * Output port for feature set repository operations.
 * This is the persistence abstraction used by the application layer.
 */
public interface FeatureSetRepositoryPort {

    /**
     * Save a feature set.
     *
     * @param featureSet the feature set to save
     * @return the saved feature set
     */
    FeatureSet save(FeatureSet featureSet);

    /**
     * Find a feature set by ID.
     *
     * @param id the feature set ID
     * @return the feature set if found
     */
    Optional<FeatureSet> findById(String id);

    /**
     * Find a feature set by ID and tenant ID.
     *
     * @param id the feature set ID
     * @param tenantId the tenant ID
     * @return the feature set if found
     */
    Optional<FeatureSet> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find feature sets by tenant ID.
     *
     * @param tenantId the tenant ID
     * @param page page number
     * @param size page size
     * @return list of feature sets
     */
    List<FeatureSet> findByTenantId(String tenantId, int page, int size);

    /**
     * Delete a feature set.
     *
     * @param id the feature set ID
     */
    void deleteById(String id);

    /**
     * Check if a feature set exists.
     *
     * @param id the feature set ID
     * @return true if exists, false otherwise
     */
    boolean existsById(String id);
}
