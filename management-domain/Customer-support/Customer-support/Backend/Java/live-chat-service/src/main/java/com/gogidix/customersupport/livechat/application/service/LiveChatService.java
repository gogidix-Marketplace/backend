package com.gogidix.customersupport.livechat.application.service;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageRequestDto;
import com.gogidix.customersupport.livechat.application.dto.ChatMessageResponseDto;
import com.gogidix.customersupport.livechat.application.dto.ChatSessionRequestDto;
import com.gogidix.customersupport.livechat.application.dto.ChatSessionResponseDto;
import com.gogidix.customersupport.livechat.application.mapper.ChatMessageMapper;
import com.gogidix.customersupport.livechat.application.mapper.ChatSessionMapper;
import com.gogidix.customersupport.livechat.domain.model.ChatMessage;
import com.gogidix.customersupport.livechat.domain.model.ChatSession;
import com.gogidix.customersupport.livechat.domain.repository.ChatMessageRepository;
import com.gogidix.customersupport.livechat.domain.repository.ChatSessionRepository;
import com.gogidix.customersupport.livechat.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionMapper chatSessionMapper;
    private final ChatMessageMapper chatMessageMapper;

    public List<ChatSessionResponseDto> getAllSessions() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all chat sessions for tenant: {}", tenantId);
        return chatSessionRepository.findByTenantId(tenantId).stream()
                .map(chatSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ChatSessionResponseDto getSessionById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching chat session with id: {} for tenant: {}", id, tenantId);
        ChatSession session = chatSessionRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found with id: " + id));
        return chatSessionMapper.toResponseDto(session);
    }

    public ChatSessionResponseDto getSessionBySessionId(String sessionId) {
        log.debug("Fetching chat session with sessionId: {}", sessionId);
        ChatSession session = chatSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found with sessionId: " + sessionId));
        return chatSessionMapper.toResponseDto(session);
    }

    public List<ChatSessionResponseDto> getSessionsByCustomerId(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching chat sessions for customer: {} and tenant: {}", customerId, tenantId);
        return chatSessionRepository.findByTenantIdAndCustomerId(tenantId, customerId).stream()
                .map(chatSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ChatSessionResponseDto> getSessionsByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching chat sessions for agent: {} and tenant: {}", agentId, tenantId);
        return chatSessionRepository.findByTenantIdAndAssignedAgentId(tenantId, agentId).stream()
                .map(chatSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ChatSessionResponseDto> getSessionsByStatus(ChatSession.ChatStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching chat sessions with status: {} for tenant: {}", status, tenantId);
        return chatSessionRepository.findByTenantIdAndStatusOrderByStartedAtDesc(tenantId, status).stream()
                .map(chatSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ChatSessionResponseDto> getActiveSessionsByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching active chat sessions for agent: {} and tenant: {}", agentId, tenantId);
        return chatSessionRepository.findByTenantIdAndAssignedAgentIdAndStatus(
                tenantId, agentId, ChatSession.ChatStatus.ACTIVE).stream()
                .map(chatSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatSessionResponseDto createSession(ChatSessionRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new chat session for tenant: {}", tenantId);

        ChatSession session = chatSessionMapper.toEntity(request, tenantId);
        ChatSession saved = chatSessionRepository.save(session);

        log.info("Created chat session with sessionId: {} for tenant: {}", saved.getSessionId(), tenantId);
        return chatSessionMapper.toResponseDto(saved);
    }

    @Transactional
    public ChatSessionResponseDto assignSession(String id, String agentId, String agentName) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Assigning chat session: {} to agent: {} for tenant: {}", id, agentId, tenantId);

        ChatSession session = chatSessionRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found with id: " + id));

        session.assignToAgent(agentId, agentName);
        ChatSession updated = chatSessionRepository.save(session);

        log.info("Assigned chat session: {} to agent: {}", updated.getSessionId(), agentId);
        return chatSessionMapper.toResponseDto(updated);
    }

    @Transactional
    public ChatSessionResponseDto endSession(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Ending chat session: {} for tenant: {}", id, tenantId);

        ChatSession session = chatSessionRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found with id: " + id));

        session.endChat();
        ChatSession updated = chatSessionRepository.save(session);

        log.info("Ended chat session: {}", updated.getSessionId());
        return chatSessionMapper.toResponseDto(updated);
    }

    @Transactional
    public ChatSessionResponseDto rateSession(String id, Integer rating, String feedback) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Rating chat session: {} for tenant: {}", id, tenantId);

        ChatSession session = chatSessionRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found with id: " + id));

        session.setRating(rating);
        session.setFeedback(feedback);
        session.updateTimestamp();

        ChatSession updated = chatSessionRepository.save(session);
        log.info("Rated chat session: {} with rating: {}", updated.getSessionId(), rating);
        return chatSessionMapper.toResponseDto(updated);
    }

    public List<ChatMessageResponseDto> getMessagesBySessionId(String sessionId) {
        log.debug("Fetching messages for session: {}", sessionId);
        return chatMessageRepository.findBySessionIdOrderBySentAtAsc(sessionId).stream()
                .map(chatMessageMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageResponseDto sendMessage(ChatMessageRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Sending message to session: {} for tenant: {}", request.getSessionId(), tenantId);

        ChatMessage message = chatMessageMapper.toEntity(request, tenantId);
        ChatMessage saved = chatMessageRepository.save(message);

        ChatSession session = chatSessionRepository.findBySessionId(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found"));

        if (saved.getSenderType() == ChatMessage.SenderType.CUSTOMER) {
            session.addCustomerMessage();
        } else if (saved.getSenderType() == ChatMessage.SenderType.AGENT) {
            session.addAgentMessage();
        }

        chatSessionRepository.save(session);

        log.info("Sent message: {} to session: {}", saved.getMessageId(), saved.getSessionId());
        return chatMessageMapper.toResponseDto(saved);
    }

    @Transactional
    public ChatMessageResponseDto markMessageAsRead(String messageId) {
        log.debug("Marking message: {} as read", messageId);
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        message.markAsRead();
        ChatMessage updated = chatMessageRepository.save(message);

        log.info("Marked message: {} as read", messageId);
        return chatMessageMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteMessage(String messageId) {
        log.debug("Deleting message: {}", messageId);
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));

        message.deleteMessage();
        chatMessageRepository.save(message);

        log.info("Deleted message: {}", messageId);
    }

    public long getSessionCountByStatus(ChatSession.ChatStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return chatSessionRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long getActiveSessionCountByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        return chatSessionRepository.countByTenantIdAndAssignedAgentIdAndStatus(
                tenantId, agentId, ChatSession.ChatStatus.ACTIVE);
    }
}
