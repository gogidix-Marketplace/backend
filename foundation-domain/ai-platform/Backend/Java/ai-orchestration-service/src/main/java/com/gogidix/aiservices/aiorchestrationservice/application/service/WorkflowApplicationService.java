package com.gogidix.aiservices.aiorchestrationservice.application.service;

import com.gogidix.aiservices.aiorchestrationservice.application.dto.CreateWorkflowRequestDto;
import com.gogidix.aiservices.aiorchestrationservice.application.dto.WorkflowResponseDto;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.Workflow;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowExecution;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStatus;
import com.gogidix.aiservices.aiorchestrationservice.domain.repository.WorkflowRepository;
import com.gogidix.aiservices.aiorchestrationservice.shared.exception.NotFoundException;

import java.util.List;

public class WorkflowApplicationService {
    private final WorkflowRepository repository;

    public WorkflowApplicationService(WorkflowRepository repository) {
        this.repository = repository;
    }

    public WorkflowResponseDto createWorkflow(String tenantId, CreateWorkflowRequestDto request) {
        Workflow workflow = new Workflow(tenantId, request.name(), request.steps());
        if (request.description() != null) workflow.setDescription(request.description());
        if (request.triggers() != null) request.triggers().forEach(workflow::addTrigger);
        Workflow saved = repository.save(workflow);
        return WorkflowResponseDto.from(saved);
    }

    public WorkflowResponseDto getWorkflowById(String workflowId, String tenantId) {
        Workflow workflow = repository.findByWorkflowIdAndTenantId(workflowId, tenantId)
                .orElseThrow(() -> new NotFoundException("Workflow", workflowId));
        return WorkflowResponseDto.from(workflow);
    }

    public List<WorkflowResponseDto> getWorkflowsByTenant(String tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(WorkflowResponseDto::from).toList();
    }

    public void deleteWorkflow(String workflowId, String tenantId) {
        if (!repository.findByWorkflowIdAndTenantId(workflowId, tenantId).isPresent()) {
            throw new NotFoundException("Workflow", workflowId);
        }
        repository.deleteByWorkflowIdAndTenantId(workflowId, tenantId);
    }

    public WorkflowExecution executeWorkflow(String workflowId, String tenantId) {
        Workflow workflow = repository.findByWorkflowIdAndTenantId(workflowId, tenantId)
                .orElseThrow(() -> new NotFoundException("Workflow", workflowId));
        if (!workflow.canExecute()) {
            throw new IllegalStateException("Workflow cannot be executed in current status");
        }
        workflow.recordExecution();
        repository.save(workflow);
        return new WorkflowExecution(workflowId, tenantId);
    }
}
