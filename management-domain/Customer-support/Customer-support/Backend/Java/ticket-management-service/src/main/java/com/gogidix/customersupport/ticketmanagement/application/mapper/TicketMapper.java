package com.gogidix.customersupport.ticketmanagement.application.mapper;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketRequestDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketResponseDto;
import com.gogidix.customersupport.ticketmanagement.application.dto.TicketUpdateRequestDto;
import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper {

    public Ticket toEntity(TicketRequestDto dto, String tenantId) {
        return Ticket.create(
                tenantId,
                dto.getTitle(),
                dto.getDescription(),
                dto.toEntityPriority(),
                dto.getCustomerId(),
                dto.getCustomerEmail(),
                dto.toEntityChannel(),
                dto.getCategory()
        );
    }

    public TicketResponseDto toResponseDto(Ticket entity) {
        List<TicketResponseDto.AttachmentDto> attachmentDtos = null;
        if (entity.getAttachments() != null) {
            attachmentDtos = entity.getAttachments().stream()
                    .map(a -> TicketResponseDto.AttachmentDto.builder()
                            .fileName(a.getFileName())
                            .fileUrl(a.getFileUrl())
                            .fileSize(a.getFileSize())
                            .contentType(a.getContentType())
                            .uploadedAt(a.getUploadedAt())
                            .uploadedBy(a.getUploadedBy())
                            .build())
                    .collect(Collectors.toList());
        }

        return TicketResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .ticketNumber(entity.getTicketNumber())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(TicketResponseDto.fromEntityStatus(entity.getStatus()))
                .priority(TicketResponseDto.fromEntityPriority(entity.getPriority()))
                .category(entity.getCategory())
                .subCategory(entity.getSubCategory())
                .tags(entity.getTags())
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .customerEmail(entity.getCustomerEmail())
                .customerPhone(entity.getCustomerPhone())
                .assignedAgentId(entity.getAssignedAgentId())
                .assignedAgentName(entity.getAssignedAgentName())
                .assignedTeam(entity.getAssignedTeam())
                .channel(TicketResponseDto.fromEntityChannel(entity.getChannel()))
                .source(entity.getSource())
                .dueDate(entity.getDueDate())
                .resolvedAt(entity.getResolvedAt())
                .closedAt(entity.getClosedAt())
                .escalatedAt(entity.getEscalatedAt())
                .escalationReason(entity.getEscalationReason())
                .slaPolicyId(entity.getSlaPolicyId())
                .slaDueDate(entity.getSlaDueDate())
                .slaBreached(entity.getSlaBreached())
                .firstResponseAt(entity.getFirstResponseAt())
                .resolutionNotes(entity.getResolutionNotes())
                .attachments(attachmentDtos)
                .watchers(entity.getWatchers())
                .relatedTicketIds(entity.getRelatedTicketIds())
                .parentTicketId(entity.getParentTicketId())
                .customerRating(entity.getCustomerRating())
                .customerFeedback(entity.getCustomerFeedback())
                .reopenedCount(entity.getReopenedCount())
                .lastReopenedAt(entity.getLastReopenedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDto(TicketUpdateRequestDto dto, Ticket entity) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPriority() != null) {
            entity.setPriority(dto.toEntityPriority());
        }
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory());
        }
        if (dto.getSubCategory() != null) {
            entity.setSubCategory(dto.getSubCategory());
        }
        if (dto.getTags() != null) {
            entity.setTags(dto.getTags());
        }
        if (dto.getCustomerName() != null) {
            entity.setCustomerName(dto.getCustomerName());
        }
        if (dto.getCustomerEmail() != null) {
            entity.setCustomerEmail(dto.getCustomerEmail());
        }
        if (dto.getCustomerPhone() != null) {
            entity.setCustomerPhone(dto.getCustomerPhone());
        }
        if (dto.getDueDate() != null) {
            entity.setDueDate(dto.getDueDate());
        }
        if (dto.getEscalationReason() != null) {
            entity.setEscalationReason(dto.getEscalationReason());
        }
        if (dto.getResolutionNotes() != null) {
            entity.setResolutionNotes(dto.getResolutionNotes());
        }
        if (dto.getWatchers() != null) {
            entity.setWatchers(dto.getWatchers());
        }
        if (dto.getRelatedTicketIds() != null) {
            entity.setRelatedTicketIds(dto.getRelatedTicketIds());
        }
        if (dto.getParentTicketId() != null) {
            entity.setParentTicketId(dto.getParentTicketId());
        }
        if (dto.getCustomerRating() != null) {
            entity.setCustomerRating(dto.getCustomerRating());
        }
        if (dto.getCustomerFeedback() != null) {
            entity.setCustomerFeedback(dto.getCustomerFeedback());
        }
        entity.updateTimestamp();
    }
}
