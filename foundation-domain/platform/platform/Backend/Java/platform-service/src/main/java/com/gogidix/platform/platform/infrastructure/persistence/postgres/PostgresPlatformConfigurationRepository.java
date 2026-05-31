package com.gogidix.platform.platform.infrastructure.persistence.postgres;

import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import com.gogidix.platform.platform.domain.repository.PlatformConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of PlatformConfigurationRepository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresPlatformConfigurationRepository {

    private final PlatformConfigurationJpaRepository jpaRepository;

    public PlatformConfiguration save(PlatformConfiguration configuration) {
        log.debug("Saving platform configuration: key={}", configuration.getConfigKey());
        return jpaRepository.save(configuration);
    }

    public Optional<PlatformConfiguration> findById(String id) {
        return jpaRepository.findById(id);
    }

    public Optional<PlatformConfiguration> findByConfigKey(String configKey) {
        return jpaRepository.findByConfigKey(configKey);
    }

    public List<PlatformConfiguration> findByTenantId(String tenantId) {
        return jpaRepository.findByTenantId(tenantId);
    }

    public List<PlatformConfiguration> findByTenantIdAndStatus(String tenantId, PlatformConfiguration.ConfigStatus status) {
        return jpaRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<PlatformConfiguration> findByEnvironment(PlatformConfiguration.Environment environment) {
        return jpaRepository.findByEnvironment(environment);
    }

    public List<PlatformConfiguration> findEffectiveConfigurations(String tenantId) {
        return jpaRepository.findEffectiveConfigurations(tenantId);
    }

    public Optional<PlatformConfiguration> findByTenantIdAndConfigKey(String tenantId, String configKey) {
        return jpaRepository.findByTenantIdAndConfigKey(tenantId, configKey);
    }

    public boolean existsByConfigKey(String configKey) {
        return jpaRepository.existsByConfigKey(configKey);
    }

    public void delete(PlatformConfiguration configuration) {
        jpaRepository.delete(configuration);
    }

    public List<PlatformConfiguration> findAll() {
        return jpaRepository.findAll();
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
