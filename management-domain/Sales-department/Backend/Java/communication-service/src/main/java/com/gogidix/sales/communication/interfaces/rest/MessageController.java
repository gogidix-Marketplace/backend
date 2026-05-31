package com.gogidix.sales.communication.interfaces.rest;

import com.gogidix.sales.communication.application.dto.response.ErrorResponseDto;
import com.gogidix.sales.communication.application.dto.response.MessageResponseDto;
import com.gogidix.sales.communication.application.dto.response.PagedResponseDto;
import com.gogidix.sales.communication.application.service.MessageCommandService;
import com.gogidix.sales.communication.application.service.MessageQueryService;
import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.port.in.MessageCommand;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * Message REST Controller
 * Handles HTTP requests for message operations
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Message management endpoints")
public class MessageController {

    private final MessageCommandService messageCommandService;
    private final MessageQueryService messageQueryService;

    @PostMapping
    @Operation(summary = "Create a new message")
    public ResponseEntity<MessageResponseDto> createMessage(
            @Valid @RequestBody CreateMessageRequestDto request) {

        MessageCommand.CreateMessageCommand command = new MessageCommand.CreateMessageCommand(
                RequestContextHolder.getTenantId(),
                request.getConversationId(),
                RequestContextHolder.getUserId(),
                request.getSenderName(),
                request.getRecipients(),
                request.getChannel(),
                request.getSubject(),
                request.getContent(),
                request.getTemplateId(),
                request.getAttachments(),
                request.getPriority(),
                request.getScheduledAt(),
                request.getParentMessageId(),
                request.getRelatedEntityType(),
                request.getRelatedEntityId()
        );

        Message message = messageCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(message));
    }

    @PostMapping("/{messageId}/send")
    @Operation(summary = "Send a message")
    public ResponseEntity<MessageResponseDto> sendMessage(
            @Parameter(description = "Message ID") @PathVariable String messageId) {

        MessageCommand.SendMessageCommand command = new MessageCommand.SendMessageCommand(
                RequestContextHolder.getTenantId(), messageId);

        Message message = messageCommandService.send(command);
        return ResponseEntity.ok(toDto(message));
    }

    @PostMapping("/{messageId}/read")
    @Operation(summary = "Mark message as read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "Message ID") @PathVariable String messageId) {

        MessageCommand.MarkAsReadCommand command = new MessageCommand.MarkAsReadCommand(
                RequestContextHolder.getTenantId(), messageId, RequestContextHolder.getUserId());

        messageCommandService.markAsRead(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{messageId}/schedule")
    @Operation(summary = "Schedule a message")
    public ResponseEntity<MessageResponseDto> scheduleMessage(
            @Parameter(description = "Message ID") @PathVariable String messageId,
            @RequestBody ScheduleMessageRequestDto request) {

        MessageCommand.ScheduleMessageCommand command = new MessageCommand.ScheduleMessageCommand(
                RequestContextHolder.getTenantId(), messageId, request.getScheduledAt());

        Message message = messageCommandService.schedule(command);
        return ResponseEntity.ok(toDto(message));
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete a message")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Message ID") @PathVariable String messageId) {

        MessageCommand.DeleteMessageCommand command = new MessageCommand.DeleteMessageCommand(
                RequestContextHolder.getTenantId(), messageId);

        messageCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{messageId}/attachments")
    @Operation(summary = "Add attachment to message")
    public ResponseEntity<MessageResponseDto> addAttachment(
            @Parameter(description = "Message ID") @PathVariable String messageId,
            @RequestBody AddAttachmentRequestDto request) {

        MessageCommand.AddAttachmentCommand command = new MessageCommand.AddAttachmentCommand(
                RequestContextHolder.getTenantId(), messageId, request.getAttachment());

        Message message = messageCommandService.addAttachment(command);
        return ResponseEntity.ok(toDto(message));
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "Get message by ID")
    public ResponseEntity<MessageResponseDto> getMessage(
            @Parameter(description = "Message ID") @PathVariable String messageId) {

        Message message = messageQueryService.getById(messageId);
        return ResponseEntity.ok(toDto(message));
    }

    @GetMapping
    @Operation(summary = "Get all messages for tenant")
    public ResponseEntity<List<MessageResponseDto>> getAllMessages() {
        List<Message> messages = messageQueryService.getAllForTenant();
        return ResponseEntity.ok(messages.stream().map(this::toDto).toList());
    }

    @GetMapping("/conversation/{conversationId}")
    @Operation(summary = "Get messages by conversation")
    public ResponseEntity<PagedResponseDto<MessageResponseDto>> getMessagesByConversation(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sentAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<Message> messages = messageQueryService.getByConversationId(
                conversationId, page, size, sortBy, sortDirection);

        PagedResponseDto<MessageResponseDto> response = PagedResponseDto.of(
                messages.getContent().stream().map(this::toDto).toList(),
                page, size, messages.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sender/{senderId}")
    @Operation(summary = "Get messages by sender")
    public ResponseEntity<PagedResponseDto<MessageResponseDto>> getMessagesBySender(
            @Parameter(description = "Sender ID") @PathVariable String senderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Message> messages = messageQueryService.getBySenderId(senderId, page, size);

        PagedResponseDto<MessageResponseDto> response = PagedResponseDto.of(
                messages.getContent().stream().map(this::toDto).toList(),
                page, size, messages.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread messages")
    public ResponseEntity<PagedResponseDto<MessageResponseDto>> getUnreadMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Message> messages = messageQueryService.getUnreadMessages(
                RequestContextHolder.getUserId(), page, size);

        PagedResponseDto<MessageResponseDto> response = PagedResponseDto.of(
                messages.getContent().stream().map(this::toDto).toList(),
                page, size, messages.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search messages")
    public ResponseEntity<PagedResponseDto<MessageResponseDto>> searchMessages(
            @RequestParam String searchTerm,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Message> messages = messageQueryService.searchMessages(
                searchTerm, startDate, endDate, page, size);

        PagedResponseDto<MessageResponseDto> response = PagedResponseDto.of(
                messages.getContent().stream().map(this::toDto).toList(),
                page, size, messages.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get messages by status")
    public ResponseEntity<PagedResponseDto<MessageResponseDto>> getMessagesByStatus(
            @Parameter(description = "Status") @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Message> messages = messageQueryService.getByStatus(status, page, size);

        PagedResponseDto<MessageResponseDto> response = PagedResponseDto.of(
                messages.getContent().stream().map(this::toDto).toList(),
                page, size, messages.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/thread/{parentMessageId}")
    @Operation(summary = "Get message thread")
    public ResponseEntity<List<MessageResponseDto>> getMessageThread(
            @Parameter(description = "Parent Message ID") @PathVariable String parentMessageId) {

        List<Message> messages = messageQueryService.getThread(parentMessageId);
        return ResponseEntity.ok(messages.stream().map(this::toDto).toList());
    }

    private MessageResponseDto toDto(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .messageId(message.getMessageId())
                .tenantId(message.getTenantId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .senderType(message.getSenderType())
                .recipientIds(message.getRecipientIds())
                .recipients(mapRecipients(message.getRecipients()))
                .channel(MessageResponseDto.mapChannelType(message.getChannel()))
                .subject(message.getSubject())
                .content(message.getContent())
                .templateId(message.getTemplateId())
                .status(MessageResponseDto.mapMessageStatus(message.getStatus()))
                .isRead(message.getIsRead())
                .readAt(message.getReadAt())
                .readBy(message.getReadBy())
                .attachments(mapAttachments(message.getAttachments()))
                .parentMessageId(message.getParentMessageId())
                .isSystemMessage(message.getIsSystemMessage())
                .priority(message.getPriority())
                .scheduledAt(message.getScheduledAt())
                .sentAt(message.getSentAt())
                .deliveredAt(message.getDeliveredAt())
                .externalMessageId(message.getExternalMessageId())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    private List<MessageResponseDto.RecipientInfoDto> mapRecipients(List<Message.RecipientInfo> recipients) {
        if (recipients == null) return null;
        return recipients.stream().map(r -> MessageResponseDto.RecipientInfoDto.builder()
                .recipientId(r.getRecipientId())
                .recipientName(r.getRecipientName())
                .recipientType(r.getRecipientType())
                .emailAddress(r.getEmailAddress())
                .phoneNumber(r.getPhoneNumber())
                .isRead(r.getIsRead())
                .readAt(r.getReadAt())
                .deliveryStatus(r.getDeliveryStatus())
                .deliveredAt(r.getDeliveredAt())
                .build()).toList();
    }

    private List<MessageResponseDto.AttachmentDto> mapAttachments(List<Message.Attachment> attachments) {
        if (attachments == null) return null;
        return attachments.stream().map(a -> MessageResponseDto.AttachmentDto.builder()
                .attachmentId(a.getAttachmentId())
                .fileName(a.getFileName())
                .fileType(a.getFileType())
                .fileSize(a.getFileSize())
                .fileUrl(a.getFileUrl())
                .build()).toList();
    }

    // Request DTOs
    @Data
    public static class CreateMessageRequestDto {
        public String conversationId;
        public String senderName;
        public List<Message.RecipientInfo> recipients;
        public Message.ChannelType channel;
        public String subject;
        public String content;
        public String templateId;
        public List<Message.Attachment> attachments;
        public Integer priority;
        public Instant scheduledAt;
        public String parentMessageId;
        public String relatedEntityType;
        public String relatedEntityId;
    }

    @Data

    public static class ScheduleMessageRequestDto {
        public Instant scheduledAt;
    }

    @Data

    public static class AddAttachmentRequestDto {
        public Message.Attachment attachment;
    }
}
