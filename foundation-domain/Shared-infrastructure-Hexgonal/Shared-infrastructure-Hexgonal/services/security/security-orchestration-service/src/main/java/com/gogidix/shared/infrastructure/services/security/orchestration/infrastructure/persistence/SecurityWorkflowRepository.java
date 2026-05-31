package com.gogidix.shared.infrastructure.services.security.orchestration.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow.WorkflowStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface SecurityWorkflowRepository extends MongoRepository<SecurityWorkflow, String> {
    List<SecurityWorkflow> findByTenantId_Value(String tenantId);
    Optional<SecurityWorkflow> findByTenantId_ValueAndId(String tenantId, String id);
    List<SecurityWorkflow> findByTenantId_ValueAndStatus(String tenantId, WorkflowStatus status);
    long countByTenantId_Value(String tenantId);
    void deleteByTenantId_ValueAndId(String tenantId, String id);
}
