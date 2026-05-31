package com.gogidix.platform.platform.infrastructure.persistence.postgres;

import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for PlatformConfiguration entity.
 */
@Repository
public interface PlatformConfigurationJpaRepository extends JpaRepository<PlatformConfiguration, String> {

    Optional<PlatformConfiguration> findByConfigKey(String configKey);

    List<PlatformConfiguration> findByTenantId(String tenantId);

    List<PlatformConfiguration> findByTenantIdAndStatus(String tenantId, PlatformConfiguration.ConfigStatus status);

    List<PlatformConfiguration> findByEnvironment(PlatformConfiguration.Environment environment);

    @Query("SELECT c FROM PlatformConfiguration c WHERE c.tenantId = :tenantId " +
           "AND c.status = 'ACTIVE' AND (" +
           "c.effectiveFrom IS NULL OR c.effectiveFrom <= CURRENT_TIMESTAMP) AND (" +
           "c.effectiveUntil IS NULL OR c.effectiveUntil > CURRENT_TIMESTAMP)")
    List<PlatformConfiguration> findEffectiveConfigurations(@Param("tenantId") String tenantId);

    Optional<PlatformConfiguration> findByTenantIdAndConfigKey(String tenantId, String configKey);

    boolean existsByConfigKey(String configKey);
}
