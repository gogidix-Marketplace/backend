package com.gogidix.customersupport.livechat.application.mapper;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageRequestDto;
import com.gogidix.customersupport.livechat.application.dto.ChatMessageResponseDto;
import com.gogidix.customersupport.livechat.domain.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageMapper {

    public ChatMessage toEntity(ChatMessageRequestDto dto, String tenantId) {
        ChatMessage message = ChatMessage.create(
                tenantId,
                dto.getSessionId(),
                dto.toEntitySenderType(),
                dto.getSenderId(),
                dto.getSenderName(),
                dto.getMessageContent()
        );

        message.setMessageType(dto.toEntityMessageType());

        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<ChatMessage.MessageAttachment> attachments = dto.getAttachments().stream()
                    .map(a -> ChatMessage.MessageAttachment.builder()
                            .fileName(a.getFileName())
                            .fileUrl(a.getFileUrl())
                            .fileSize(a.getFileSize())
                            .contentType(a.getContentType())
                            .build())
                    .collect(Collectors.toList());
            message.setAttachments(attachments);
        }

        message.setReplyToMessageId(dto.getReplyToMessageId());

        if (dto.getIpAddress() != null || dto.getUserAgent() != null) {
            message.setMetadata(ChatMessage.MessageMetadata.builder()
                    .ipAddress(dto.getIpAddress())
                    .userAgent(dto.getUserAgent())
                    .build());
        }

        return message;
    }

    public ChatMessageResponseDto toResponseDto(ChatMessage entity) {
        List<ChatMessageResponseDto.MessageAttachmentDto> attachmentDtos = null;
        if (entity.getAttachments() != null && !entity.getAttachments().isEmpty()) {
            attachmentDtos = entity.getAttachments().stream()
                    .map(a -> ChatMessageResponseDto.MessageAttachmentDto.builder()
                            .fileName(a.getFileName())
                            .fileUrl(a.getFileUrl())
                            .fileSize(a.getFileSize())
                            .contentType(a.getContentType())
                            .build())
                    .collect(Collectors.toList());
        }

        ChatMessageResponseDto.MessageMetadataDto metadataDto = null;
        if (entity.getMetadata() != null) {
            metadataDto = ChatMessageResponseDto.MessageMetadataDto.builder()
                    .ipAddress(entity.getMetadata().getIpAddress())
                    .userAgent(entity.getMetadata().getUserAgent())
                    .browser(entity.getMetadata().getBrowser())
                    .os(entity.getMetadata().getOs())
                    .location(entity.getMetadata().getLocation())
                    .build();
        }

        return ChatMessageResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .sessionId(entity.getSessionId())
                .messageId(entity.getMessageId())
                .senderType(ChatMessageResponseDto.fromEntitySenderType(entity.getSenderType()))
                .senderId(entity.getSenderId())
                .senderName(entity.getSenderName())
                .messageContent(entity.getMessageContent())
                .messageType(ChatMessageResponseDto.fromEntityMessageType(entity.getMessageType()))
                .attachments(attachmentDtos)
                .sentAt(entity.getSentAt())
                .readAt(entity.getReadAt())
                .isDeleted(entity.getIsDeleted())
                .editedAt(entity.getEditedAt())
                .replyToMessageId(entity.getReplyToMessageId())
                .metadata(metadataDto)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
