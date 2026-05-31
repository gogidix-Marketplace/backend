package com.gogidix.customersupport.slamanagement.application.mapper;

import com.gogidix.customersupport.slamanagement.application.dto.SLABreachResponseDto;
import com.gogidix.customersupport.slamanagement.domain.model.SLABreach;
import org.springframework.stereotype.Component;

@Component
public class SLABreachMapper {

    public SLABreachResponseDto toResponseDto(SLABreach entity) {
        return SLABreachResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .ticketId(entity.getTicketId())
                .ticketNumber(entity.getTicketNumber())
                .slaPolicyId(entity.getSlaPolicyId())
                .slaPolicyName(entity.getSlaPolicyName())
                .breachType(SLABreachResponseDto.fromEntityBreachType(entity.getBreachType()))
                .breachDateTime(entity.getBreachDateTime())
                .dueDateTime(entity.getDueDateTime())
                .actualResponseTimeMinutes(entity.getActualResponseTimeMinutes())
                .actualResolutionTimeMinutes(entity.getActualResolutionTimeMinutes())
                .targetTimeMinutes(entity.getTargetTimeMinutes())
                .overdueByMinutes(entity.getOverdueByMinutes())
                .severity(entity.getSeverity())
                .assignedAgentId(entity.getAssignedAgentId())
                .assignedAgentName(entity.getAssignedAgentName())
                .assignedTeam(entity.getAssignedTeam())
                .customerId(entity.getCustomerId())
                .category(entity.getCategory())
                .priority(entity.getPriority())
                .channel(entity.getChannel())
                .isNotified(entity.getIsNotified())
                .notifiedAt(entity.getNotifiedAt())
                .escalationTriggered(entity.getEscalationTriggered())
                .escalationLevel(entity.getEscalationLevel())
                .resolutionNotes(entity.getResolutionNotes())
                .resolvedAt(entity.getResolvedAt())
                .impactScore(entity.getImpactScore())
                .preventiveActions(entity.getPreventiveActions())
                .metadata(entity.getMetadata())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
