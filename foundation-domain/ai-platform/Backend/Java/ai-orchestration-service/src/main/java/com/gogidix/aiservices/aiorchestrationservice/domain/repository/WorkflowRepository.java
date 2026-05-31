package com.gogidix.aiservices.aiorchestrationservice.domain.repository;

import com.gogidix.aiservices.aiorchestrationservice.domain.model.Workflow;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStatus;

import java.util.List;
import java.util.Optional;

public interface WorkflowRepository {
    Workflow save(Workflow workflow);
    Optional<Workflow> findById(String id);
    Optional<Workflow> findByWorkflowIdAndTenantId(String workflowId, String tenantId);
    List<Workflow> findByTenantId(String tenantId);
    List<Workflow> findByTenantIdAndStatus(String tenantId, WorkflowStatus status);
    void deleteById(String id);
    void deleteByWorkflowIdAndTenantId(String workflowId, String tenantId);
}
