package com.gogidix.shared.infrastructure.services.security.orchestration.application.service;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.CreateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.UpdateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response.SecurityWorkflowResponseDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.mapper.SecurityWorkflowMapper;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.exception.SecurityWorkflowNotFoundException;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.exception.SecurityWorkflowValidationException;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow.WorkflowStatus;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.in.SecurityWorkflowPort;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.out.SecurityWorkflowRepositoryPort;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * Application service for SecurityWorkflow management.
 */
@Service
@Transactional
public class SecurityWorkflowService implements SecurityWorkflowPort {
    private final SecurityWorkflowMapper mapper;
    private final SecurityWorkflowRepositoryPort repository;
    private final TenantContextHolder tenantContextHolder;
    public SecurityWorkflowService(SecurityWorkflowMapper mapper,
                                   SecurityWorkflowRepositoryPort repository,
                                   TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }
    private String getTenantId() {
        return tenantContextHolder.getRequiredTenantId();
    }
    @Override
    public SecurityWorkflowResponseDto create(CreateSecurityWorkflowRequestDto dto) {
        String tenantId = getTenantId();
        SecurityWorkflow entity = mapper.toEntity(dto, tenantId);
        SecurityWorkflow saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }
    @Override
    public SecurityWorkflowResponseDto findById(String id) {
        String tenantId = getTenantId();
        SecurityWorkflow entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new SecurityWorkflowNotFoundException(id));
        return mapper.toResponseDto(entity);
    }
    @Override
    public List<SecurityWorkflowResponseDto> findAll() {
        String tenantId = getTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<SecurityWorkflowResponseDto> findByStatus(String status) {
        String tenantId = getTenantId();
        try {
            WorkflowStatus workflowStatus = WorkflowStatus.valueOf(status.toUpperCase());
            return repository.findByTenantIdAndStatus(tenantId, workflowStatus).stream()
                .map(mapper::toResponseDto)
                .toList();
        } catch (IllegalArgumentException e) {
            throw new SecurityWorkflowValidationException("Invalid status: " + status);
        }
    }
    @Override
    public SecurityWorkflowResponseDto update(String id, UpdateSecurityWorkflowRequestDto dto) {
        String tenantId = getTenantId();
        SecurityWorkflow entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new SecurityWorkflowNotFoundException(id));
        mapper.updateEntity(dto, entity);
        SecurityWorkflow updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
    @Override
    public void delete(String id) {
        String tenantId = getTenantId();
        if (!repository.findByTenantIdAndId(tenantId, id).isPresent()) {
            throw new SecurityWorkflowNotFoundException(id);
        }
        repository.deleteByTenantIdAndId(tenantId, id);
    }
    @Override
    public SecurityWorkflowResponseDto activate(String id) {
        String tenantId = getTenantId();
        SecurityWorkflow entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new SecurityWorkflowNotFoundException(id));
        entity.setStatus(WorkflowStatus.ACTIVE);
        SecurityWorkflow updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
    @Override
    public SecurityWorkflowResponseDto deactivate(String id) {
        String tenantId = getTenantId();
        SecurityWorkflow entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new SecurityWorkflowNotFoundException(id));
        entity.setStatus(WorkflowStatus.INACTIVE);
        SecurityWorkflow updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
    @Override
    public SecurityWorkflowResponseDto execute(String id) {
        String tenantId = getTenantId();
        SecurityWorkflow entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new SecurityWorkflowNotFoundException(id));
        if (entity.getStatus() != WorkflowStatus.ACTIVE) {
            throw new SecurityWorkflowValidationException("Only active workflows can be executed");
        }
        // Workflow execution logic would be implemented here
        // For now, just return the workflow
        return mapper.toResponseDto(entity);
    }
}
