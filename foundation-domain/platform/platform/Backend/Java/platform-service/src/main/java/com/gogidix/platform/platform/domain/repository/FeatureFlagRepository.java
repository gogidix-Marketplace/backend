package com.gogidix.platform.platform.domain.repository;

import com.gogidix.platform.platform.domain.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for FeatureFlag entity.
 */
@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, String> {

    /**
     * Find flag by key
     */
    Optional<FeatureFlag> findByFeatureKey(String flagKey);

    /**
     * Find all flags for tenant
     */
    List<FeatureFlag> findByTenantId(String tenantId);

    /**
     * Find enabled flags for tenant
     */
    @Query("SELECT f FROM FeatureFlag f WHERE f.tenantId = :tenantId AND f.isEnabled = true AND f.status = 'ACTIVE'")
    List<FeatureFlag> findEnabledFlags(@Param("tenantId") String tenantId);

    /**
     * Find active flags
     */
    List<FeatureFlag> findByStatus(FeatureFlag.FlagStatus status);

    /**
     * Check if key exists
     */
    boolean existsByFeatureKey(String flagKey);
}
