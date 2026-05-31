package com.gogidix.sales.communication.application.service;

import com.gogidix.sales.communication.domain.event.MessageSentEvent;
import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.port.in.MessageCommand;
import com.gogidix.sales.communication.domain.port.out.EventPublisher;
import com.gogidix.sales.communication.domain.port.out.MessageSender;
import com.gogidix.sales.communication.domain.repository.MessageRepository;
import com.gogidix.sales.communication.shared.exception.ConflictException;
import com.gogidix.sales.communication.shared.exception.NotFoundException;
import com.gogidix.sales.communication.shared.exception.ValidationException;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Message Command Service
 * Handles all write operations for messages
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageCommandService {

    private final MessageRepository messageRepository;
    private final EventPublisher eventPublisher;
    private final MessageSender messageSender;

    @Transactional
    public Message create(MessageCommand.CreateMessageCommand command) {
        log.info("Creating message for tenant: {}, sender: {}",
                command.getTenantId(), command.getSenderId());

        validateRecipients(command.getRecipients());
        validateContentSize(command.getContent());

        Message message = Message.create(
                command.getTenantId(),
                command.getConversationId(),
                command.getSenderId(),
                command.getSenderName(),
                command.getRecipients(),
                command.getChannel(),
                command.getSubject(),
                command.getContent()
        );

        // Set additional fields
        if (command.getTemplateId() != null) {
            message.setTemplateId(command.getTemplateId());
        }
        if (command.getAttachments() != null) {
            message.setAttachments(command.getAttachments());
        }
        if (command.getPriority() != null) {
            message.setPriorityValue(command.getPriority());
        }
        if (command.getParentMessageId() != null) {
            message.setParentMessageId(command.getParentMessageId());
        }

        // Schedule if scheduled time is provided
        if (command.getScheduledAt() != null) {
            message.schedule(command.getScheduledAt());
        }

        Message savedMessage = messageRepository.save(message);
        publishEvents(savedMessage);

        log.info("Created message: {} for tenant: {}", savedMessage.getMessageId(), command.getTenantId());
        return savedMessage;
    }

    @Transactional
    public Message send(MessageCommand.SendMessageCommand command) {
        log.info("Sending message: {} for tenant: {}", command.getMessageId(), command.getTenantId());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        // Check if scheduled and time has come
        if (message.getStatus() == Message.MessageStatus.SCHEDULED) {
            if (message.getScheduledAt() != null && message.getScheduledAt().isAfter(java.time.Instant.now())) {
                throw new ValidationException("Scheduled time has not arrived yet");
            }
        }

        message.send();

        if (messageSender.isReady()) {
            try {
                String externalId = messageSender.send(message);
                message.markAsSent(externalId);
            } catch (Exception e) {
                log.error("Failed to send message: {}", message.getMessageId(), e);
                message.markAsFailed(e.getMessage());
            }
        }

        Message savedMessage = messageRepository.save(message);
        publishEvents(savedMessage);

        log.info("Sent message: {}", command.getMessageId());
        return savedMessage;
    }

    @Transactional
    public void markAsRead(MessageCommand.MarkAsReadCommand command) {
        log.info("Marking message as read: {} for tenant: {}",
                command.getMessageId(), command.getTenantId());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        message.markAsRead(command.getUserId());
        messageRepository.save(message);

        // Publish read event
        if (eventPublisher.isReady()) {
            eventPublisher.publish(MessageSentEvent.builder()
                    .messageId(message.getMessageId())
                    .tenantId(message.getTenantId())
                    .conversationId(message.getConversationId())
                    .readBy(command.getUserId())
                    .timestamp(java.time.Instant.now())
                    .eventType("MESSAGE_READ")
                    .build());
        }

        log.info("Marked message as read: {}", command.getMessageId());
    }

    @Transactional
    public Message schedule(MessageCommand.ScheduleMessageCommand command) {
        log.info("Scheduling message: {} for tenant: {} at {}",
                command.getMessageId(), command.getTenantId(), command.getScheduledAt());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        message.schedule(command.getScheduledAt());
        Message savedMessage = messageRepository.save(message);
        publishEvents(savedMessage);

        log.info("Scheduled message: {}", command.getMessageId());
        return savedMessage;
    }

    @Transactional
    public void delete(MessageCommand.DeleteMessageCommand command) {
        log.info("Deleting message: {} for tenant: {}",
                command.getMessageId(), command.getTenantId());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        if (message.getStatus() != Message.MessageStatus.DRAFT &&
                message.getStatus() != Message.MessageStatus.FAILED) {
            throw new ValidationException("Can only delete draft or failed messages");
        }

        messageRepository.deleteByMessageIdAndTenantId(command.getMessageId(), command.getTenantId());

        log.info("Deleted message: {}", command.getMessageId());
    }

    @Transactional
    public Message addAttachment(MessageCommand.AddAttachmentCommand command) {
        log.info("Adding attachment to message: {} for tenant: {}",
                command.getMessageId(), command.getTenantId());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        if (message.getStatus() != Message.MessageStatus.DRAFT) {
            throw new ValidationException("Can only add attachments to draft messages");
        }

        validateAttachment(command.getAttachment());
        message.addAttachment(command.getAttachment());

        Message savedMessage = messageRepository.save(message);
        publishEvents(savedMessage);

        log.info("Added attachment to message: {}", command.getMessageId());
        return savedMessage;
    }

    @Transactional
    public void markAsDelivered(MessageCommand.MarkAsDeliveredCommand command) {
        log.info("Marking message as delivered: {} for tenant: {}",
                command.getMessageId(), command.getTenantId());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        message.markAsDelivered();
        if (command.getExternalMessageId() != null) {
            message.setExternalMessageId(command.getExternalMessageId());
        }

        messageRepository.save(message);
        publishEvents(message);

        log.info("Marked message as delivered: {}", command.getMessageId());
    }

    @Transactional
    public void markAsFailed(MessageCommand.MarkAsFailedCommand command) {
        log.info("Marking message as failed: {} for tenant: {}",
                command.getMessageId(), command.getTenantId());

        Message message = messageRepository.findByMessageIdAndTenantId(
                        command.getMessageId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Message", command.getMessageId()));

        message.markAsFailed(command.getErrorMessage());
        messageRepository.save(message);
        publishEvents(message);

        log.info("Marked message as failed: {}", command.getMessageId());
    }

    private void validateRecipients(List<Message.RecipientInfo> recipients) {
        if (recipients == null || recipients.isEmpty()) {
            throw new ValidationException("recipients", "At least one recipient is required");
        }
        if (recipients.size() > 50) {
            throw new ValidationException("recipients", "Maximum 50 recipients allowed");
        }
    }

    private void validateContentSize(String content) {
        if (content != null && content.length() > 100000) {
            throw new ValidationException("content", "Content size exceeds maximum limit");
        }
    }

    private void validateAttachment(Message.Attachment attachment) {
        if (attachment.getFileSize() != null && attachment.getFileSize() > 5242880) {
            throw new ValidationException("attachment", "Attachment size exceeds 5MB limit");
        }
    }

    private void publishEvents(Message message) {
        if (!message.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll((List) message.getDomainEvents());
            message.clearDomainEvents();
        }
    }

    /**
     * Processes scheduled messages that are due to be sent
     */
    @Transactional
    public void processScheduledMessages() {
        log.debug("Processing scheduled messages");
        String tenantId = RequestContextHolder.getTenantId();

        List<Message> scheduledMessages = messageRepository.findByScheduledAtBeforeAndStatus(
                java.time.Instant.now(), Message.MessageStatus.SCHEDULED);

        for (Message message : scheduledMessages) {
            if (message.getTenantId().equals(tenantId)) {
                try {
                    MessageCommand.SendMessageCommand command = new MessageCommand.SendMessageCommand(
                            message.getTenantId(), message.getMessageId());
                    send(command);
                } catch (Exception e) {
                    log.error("Failed to process scheduled message: {}", message.getMessageId(), e);
                }
            }
        }
    }
}
