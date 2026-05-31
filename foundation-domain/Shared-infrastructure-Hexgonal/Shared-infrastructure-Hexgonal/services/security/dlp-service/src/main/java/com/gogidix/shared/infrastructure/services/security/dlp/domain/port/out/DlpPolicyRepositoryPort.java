package com.gogidix.shared.infrastructure.services.security.dlp.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy.PolicyStatus;

import java.util.List;
import java.util.Optional;

/**
 * Output port for DlpPolicy repository.
 */
public interface DlpPolicyRepositoryPort {

    DlpPolicy save(DlpPolicy entity);

    Optional<DlpPolicy> findById(String id);

    List<DlpPolicy> findAllByTenantId(String tenantId);

    Optional<DlpPolicy> findByTenantIdAndId(String tenantId, String id);

    List<DlpPolicy> findByTenantIdAndStatus(String tenantId, PolicyStatus status);

    long countByTenantId(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByTenantIdAndPolicyName(String tenantId, String policyName);
}
