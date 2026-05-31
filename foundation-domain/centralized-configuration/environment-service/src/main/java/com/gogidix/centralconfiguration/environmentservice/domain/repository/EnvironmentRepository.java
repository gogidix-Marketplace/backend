package com.gogidix.centralconfiguration.environmentservice.domain.repository;

import com.gogidix.centralconfiguration.environmentservice.domain.model.Environment;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Environment aggregate.
 */
public interface EnvironmentRepository {

    Environment save(Environment environment);

    Optional<Environment> findById(Long id);

    Optional<Environment> findByEnvironmentName(String environmentName);

    List<Environment> findByTenantId(String tenantId);

    List<Environment> findByTenantIdAndIsActive(String tenantId, Boolean isActive);

    void delete(Environment environment);

    boolean existsByEnvironmentName(String environmentName);
}
