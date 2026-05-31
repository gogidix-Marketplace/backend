package com.gogidix.platform.platform.infrastructure.persistence.postgres;

import com.gogidix.platform.platform.domain.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for FeatureFlag entity.
 */
@Repository
public interface FeatureFlagJpaRepository extends JpaRepository<FeatureFlag, String> {

    Optional<FeatureFlag> findByFlagKey(String flagKey);

    List<FeatureFlag> findByTenantId(String tenantId);

    List<FeatureFlag> findByTenantIdAndIsActive(String tenantId, boolean isActive);

    List<FeatureFlag> findByIsGlobal(boolean isGlobal);

    boolean existsByFlagKey(String flagKey);
}
