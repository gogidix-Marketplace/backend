package com.gogidix.shared.infrastructure.services.security.dlp.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy.PolicyStatus;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.port.out.DlpPolicyRepositoryPort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * MongoDB adapter implementation of DlpPolicyRepositoryPort.
 */
@Repository
public class DlpPolicyRepositoryAdapter implements DlpPolicyRepositoryPort {
    private final DlpPolicyRepository dlpPolicyRepository;
    public DlpPolicyRepositoryAdapter(DlpPolicyRepository dlpPolicyRepository) {
        this.dlpPolicyRepository = dlpPolicyRepository;
    }
    @Override
    public DlpPolicy save(DlpPolicy entity) {
        return dlpPolicyRepository.save(entity);
    }
    @Override
    public Optional<DlpPolicy> findById(String id) {
        return dlpPolicyRepository.findById(id);
    }
    @Override
    public List<DlpPolicy> findAllByTenantId(String tenantId) {
        return dlpPolicyRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<DlpPolicy> findByTenantIdAndId(String tenantId, String id) {
        return dlpPolicyRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public List<DlpPolicy> findByTenantIdAndStatus(String tenantId, PolicyStatus status) {
        return dlpPolicyRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return dlpPolicyRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        dlpPolicyRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByTenantIdAndPolicyName(String tenantId, String policyName) {
        return dlpPolicyRepository.findByTenantId_Value(tenantId).stream()
            .anyMatch(policy -> policy.getPolicyName().equals(policyName));
    }
}
