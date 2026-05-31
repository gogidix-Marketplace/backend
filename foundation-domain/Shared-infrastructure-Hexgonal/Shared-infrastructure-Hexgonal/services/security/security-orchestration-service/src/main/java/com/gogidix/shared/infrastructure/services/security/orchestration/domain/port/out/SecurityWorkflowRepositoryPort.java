package com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.out;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow.WorkflowStatus;
import java.util.List;
import java.util.Optional;
/**
 * Output port for SecurityWorkflow repository.
 */
public interface SecurityWorkflowRepositoryPort {
    SecurityWorkflow save(SecurityWorkflow entity);
    Optional<SecurityWorkflow> findById(String id);
    List<SecurityWorkflow> findAllByTenantId(String tenantId);
    Optional<SecurityWorkflow> findByTenantIdAndId(String tenantId, String id);
    List<SecurityWorkflow> findByTenantIdAndStatus(String tenantId, WorkflowStatus status);
    long countByTenantId(String tenantId);
    void deleteByTenantIdAndId(String tenantId, String id);
    boolean existsByTenantIdAndWorkflowName(String tenantId, String workflowName);
}
