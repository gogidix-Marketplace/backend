package com.gogidix.customersupport.customerportal.application.mapper;

import com.gogidix.customersupport.customerportal.application.dto.TicketHistoryResponseDto;
import com.gogidix.customersupport.customerportal.domain.model.TicketHistory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketHistoryMapper {

    public TicketHistory toEntity(String customerId, String ticketId, String ticketNumber,
                                    String title, String description) {
        TicketHistory history = new TicketHistory();
        history.setId(java.util.UUID.randomUUID().toString());
        history.setCustomerId(customerId);
        history.setTicketId(ticketId);
        history.setTicketNumber(ticketNumber);
        history.setTitle(title);
        history.setDescription(description);
        history.setCreatedAt(java.time.Instant.now());
        history.setUpdatedAt(java.time.Instant.now());
        return history;
    }

    public TicketHistoryResponseDto toResponseDto(TicketHistory entity) {
        List<TicketHistoryResponseDto.AttachmentInfoDto> attachmentDtos = null;
        if (entity.getAttachments() != null) {
            attachmentDtos = entity.getAttachments().stream()
                    .map(a -> TicketHistoryResponseDto.AttachmentInfoDto.builder()
                            .fileName(a.getFileName())
                            .fileUrl(a.getFileUrl())
                            .uploadedAt(a.getUploadedAt())
                            .build())
                    .collect(Collectors.toList());
        }

        return TicketHistoryResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .customerId(entity.getCustomerId())
                .ticketId(entity.getTicketId())
                .ticketNumber(entity.getTicketNumber())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .category(entity.getCategory())
                .channel(entity.getChannel())
                .createdAt(entity.getCreatedAt())
                .resolvedAt(entity.getResolvedAt())
                .closedAt(entity.getClosedAt())
                .assignedAgentId(entity.getAssignedAgentId())
                .assignedAgentName(entity.getAssignedAgentName())
                .satisfactionRating(entity.getSatisfactionRating())
                .feedback(entity.getFeedback())
                .resolutionNotes(entity.getResolutionNotes())
                .attachments(attachmentDtos)
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
