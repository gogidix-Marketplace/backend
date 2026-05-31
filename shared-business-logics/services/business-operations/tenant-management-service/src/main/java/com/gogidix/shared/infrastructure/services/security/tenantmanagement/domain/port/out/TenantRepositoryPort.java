package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;

import java.util.List;
import java.util.Optional;

/**
 * Tenant repository output port.
 */
public interface TenantRepositoryPort {

    Tenant save(Tenant tenant);

    Optional<Tenant> findByTenantId(String tenantId);

    Optional<Tenant> findByDomain(String domain);

    List<Tenant> findAll();

    boolean existsByTenantId(String tenantId);

    boolean existsByDomain(String domain);

    void delete(Tenant tenant);

    void deleteByTenantId(String tenantId);

    List<Tenant> findByStatus(Tenant.TenantStatus status);

    List<Tenant> findByPlan(Tenant.TenantPlan plan);
}
