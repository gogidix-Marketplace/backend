package com.gogidix.shared.infrastructure.services.security.orchestration.application.mapper;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.CreateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.UpdateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response.SecurityWorkflowResponseDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow;
import com.gogidix.shared.infrastructure.services.security.orchestration.domain.model.SecurityWorkflow.WorkflowStatus;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.stereotype.Component;
/**
 * Mapper for SecurityWorkflow entity.
 */
@Component
public class SecurityWorkflowMapper {
    public SecurityWorkflow toEntity(CreateSecurityWorkflowRequestDto dto, String tenantId) {
        SecurityWorkflow entity = new SecurityWorkflow(new TenantId(tenantId), dto.workflowName());
        entity.setDescription(dto.description());
        entity.setTriggerType(dto.triggerType());
        entity.setStatus(WorkflowStatus.DRAFT);
        return entity;
    }
    public SecurityWorkflowResponseDto toResponseDto(SecurityWorkflow entity) {
        return new SecurityWorkflowResponseDto(
            entity.getId(),
            entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getWorkflowId(),
            entity.getWorkflowName(),
            entity.getDescription(),
            entity.getStatus() != null ? entity.getStatus().name() : null,
            entity.getTriggerType(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    public void updateEntity(UpdateSecurityWorkflowRequestDto dto, SecurityWorkflow entity) {
        if (dto.workflowName() != null) {
            entity.setWorkflowName(dto.workflowName());
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
        if (dto.triggerType() != null) {
            entity.setTriggerType(dto.triggerType());
        }
    }
}
