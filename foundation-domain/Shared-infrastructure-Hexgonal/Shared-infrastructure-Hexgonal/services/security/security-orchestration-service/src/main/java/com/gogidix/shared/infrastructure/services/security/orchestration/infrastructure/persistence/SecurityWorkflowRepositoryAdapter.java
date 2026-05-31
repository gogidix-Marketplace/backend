package com.gogidix.shared.infrastructure.services.security.orchestration.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow.WorkflowStatus;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.out.SecurityWorkflowRepositoryPort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * MongoDB adapter implementation of SecurityWorkflowRepositoryPort.
 */
@Repository
public class SecurityWorkflowRepositoryAdapter implements SecurityWorkflowRepositoryPort {
    private final SecurityWorkflowRepository securityWorkflowRepository;
    public SecurityWorkflowRepositoryAdapter(SecurityWorkflowRepository securityWorkflowRepository) {
        this.securityWorkflowRepository = securityWorkflowRepository;
    }
    @Override
    public SecurityWorkflow save(SecurityWorkflow entity) {
        return securityWorkflowRepository.save(entity);
    }
    @Override
    public Optional<SecurityWorkflow> findById(String id) {
        return securityWorkflowRepository.findById(id);
    }
    @Override
    public List<SecurityWorkflow> findAllByTenantId(String tenantId) {
        return securityWorkflowRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<SecurityWorkflow> findByTenantIdAndId(String tenantId, String id) {
        return securityWorkflowRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public List<SecurityWorkflow> findByTenantIdAndStatus(String tenantId, WorkflowStatus status) {
        return securityWorkflowRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return securityWorkflowRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        securityWorkflowRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByTenantIdAndWorkflowName(String tenantId, String workflowName) {
        return securityWorkflowRepository.findByTenantId_Value(tenantId).stream()
            .anyMatch(workflow -> workflow.getWorkflowName().equals(workflowName));
    }
}
