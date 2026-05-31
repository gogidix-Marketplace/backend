package com.gogidix.centralconfiguration.featureflagservice.domain.repository;

import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlag;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Feature Flag aggregate.
 */
public interface FeatureFlagRepository {

    FeatureFlag save(FeatureFlag featureFlag);

    Optional<FeatureFlag> findById(Long id);

    Optional<FeatureFlag> findByFlagKey(String flagKey);

    List<FeatureFlag> findByTenantId(String tenantId);

    List<FeatureFlag> findByTenantIdAndIsEnabled(String tenantId, Boolean isEnabled);

    void delete(FeatureFlag featureFlag);

    boolean existsByFlagKey(String flagKey);

    List<FeatureFlag> findAll(int page, int size);
}
