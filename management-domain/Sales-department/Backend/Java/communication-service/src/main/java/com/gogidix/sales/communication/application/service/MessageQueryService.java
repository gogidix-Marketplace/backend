package com.gogidix.sales.communication.application.service;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.repository.MessageRepository;
import com.gogidix.sales.communication.shared.exception.NotFoundException;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Message Query Service
 * Handles all read operations for messages
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageQueryService {

    private final MessageRepository messageRepository;

    public Message getById(String messageId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting message: {} for tenant: {}", messageId, tenantId);

        return messageRepository.findByMessageIdAndTenantId(messageId, tenantId)
                .orElseThrow(() -> new NotFoundException("Message", messageId));
    }

    public Page<Message> getByConversationId(String conversationId, int page, int size,
                                              String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting messages for conversation: {} for tenant: {}", conversationId, tenantId);

        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Message> messages = messageRepository.findByConversationIdAndTenantIdOrderBySentAtAsc(
                conversationId, tenantId);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), messages.size());
        List<Message> pagedMessages = messages.subList(start, end);

        return new PageImpl<>(pagedMessages, pageable, messages.size());
    }

    public Page<Message> getBySenderId(String senderId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting messages for sender: {} for tenant: {}", senderId, tenantId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Message> messages = messageRepository.findBySenderIdAndTenantId(senderId, tenantId);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), messages.size());
        List<Message> pagedMessages = messages.subList(start, end);

        return new PageImpl<>(pagedMessages, pageable, messages.size());
    }

    public Page<Message> getUnreadMessages(String userId, int page, int size) {
        log.debug("Getting unread messages for user: {}", userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Message> messages = messageRepository.findByIsReadFalseAndRecipientIdsContaining(userId);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), messages.size());
        List<Message> pagedMessages = messages.subList(start, end);

        return new PageImpl<>(pagedMessages, pageable, messages.size());
    }

    public Page<Message> searchMessages(String searchTerm, Instant startDate, Instant endDate,
                                         int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Searching messages for tenant: {} with term: {}", tenantId, searchTerm);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Message> messages;
        if (startDate != null && endDate != null) {
            List<Message> allMessages = messageRepository.findByTenantIdAndCreatedAtBetween(tenantId, startDate, endDate);
            messages = allMessages.stream()
                    .filter(m -> m.getContent() != null && m.getContent().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
        } else {
            messages = messageRepository.searchByContent(tenantId, searchTerm);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), messages.size());
        List<Message> pagedMessages = messages.subList(start, end);

        return new PageImpl<>(pagedMessages, pageable, messages.size());
    }

    public Page<Message> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting messages with status: {} for tenant: {}", status, tenantId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Message.MessageStatus messageStatus = Message.MessageStatus.valueOf(status.toUpperCase());

        List<Message> messages = messageRepository.findByTenantIdAndStatus(tenantId, messageStatus);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), messages.size());
        List<Message> pagedMessages = messages.subList(start, end);

        return new PageImpl<>(pagedMessages, pageable, messages.size());
    }

    public List<Message> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting all messages for tenant: {}", tenantId);
        return messageRepository.findByTenantId(tenantId);
    }

    public List<Message> getByChannel(String channel) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting messages for channel: {} for tenant: {}", channel, tenantId);
        Message.ChannelType channelType = Message.ChannelType.valueOf(channel.toUpperCase());
        return messageRepository.findByTenantIdAndChannel(tenantId, channelType);
    }

    public List<Message> getByTemplateId(String templateId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting messages for template: {} for tenant: {}", templateId, tenantId);
        return messageRepository.findByTemplateIdAndTenantId(templateId, tenantId);
    }

    public List<Message> getByRelatedEntity(String relatedEntityType, String relatedEntityId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting messages for entity: {}:{} for tenant: {}", relatedEntityType, relatedEntityId, tenantId);
        return messageRepository.findByRelatedEntityTypeAndRelatedEntityIdAndTenantId(
                relatedEntityType, relatedEntityId, tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Message.MessageStatus messageStatus = Message.MessageStatus.valueOf(status.toUpperCase());
        return messageRepository.countByTenantIdAndStatus(tenantId, messageStatus);
    }

    public long countUnread(String recipientId) {
        return messageRepository.countUnreadByRecipientId(recipientId);
    }

    public List<Message> getThread(String parentMessageId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Getting message thread for: {} for tenant: {}", parentMessageId, tenantId);
        return messageRepository.findByTenantIdAndParentMessageId(tenantId, parentMessageId);
    }
}
