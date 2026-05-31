package com.gogidix.sales.communication.application.service;

import com.gogidix.sales.communication.domain.event.ConversationCreatedEvent;
import com.gogidix.sales.communication.domain.event.ConversationUpdatedEvent;
import com.gogidix.sales.communication.domain.model.Conversation;
import com.gogidix.sales.communication.domain.port.in.ConversationCommand;
import com.gogidix.sales.communication.domain.port.out.EventPublisher;
import com.gogidix.sales.communication.domain.repository.ConversationRepository;
import com.gogidix.sales.communication.domain.repository.MessageRepository;
import com.gogidix.sales.communication.shared.exception.NotFoundException;
import com.gogidix.sales.communication.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conversation Command Service
 * Handles all write operations for conversations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationCommandService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Conversation create(ConversationCommand.CreateConversationCommand command) {
        log.info("Creating conversation for tenant: {}, owner: {}",
                command.getTenantId(), command.getOwnerId());

        Conversation conversation = Conversation.create(
                command.getTenantId(),
                command.getTitle(),
                command.getType(),
                command.getOwnerId(),
                command.getOwnerName(),
                command.getParticipants(),
                command.getDefaultChannel()
        );

        // Set additional fields
        if (command.getDescription() != null) {
            conversation.setDescription(command.getDescription());
        }
        if (command.getRelatedEntityType() != null && command.getRelatedEntityId() != null) {
            conversation.linkToEntity(command.getRelatedEntityType(), command.getRelatedEntityId());
        }

        Conversation savedConversation = conversationRepository.save(conversation);
        publishEvents(savedConversation);

        log.info("Created conversation: {} for tenant: {}", savedConversation.getConversationId(), command.getTenantId());
        return savedConversation;
    }

    @Transactional
    public Conversation addParticipant(ConversationCommand.AddParticipantCommand command) {
        log.info("Adding participant to conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        if (conversation.getParticipantIds() != null && conversation.getParticipantIds().size() >= 100) {
            throw new ValidationException("Maximum participants (100) reached");
        }

        conversation.addParticipant(command.getParticipant());
        conversation.addParticipant(command.getParticipant());

        Conversation savedConversation = conversationRepository.save(conversation);

        // Publish update event
        publishParticipantUpdateEvent(savedConversation, "PARTICIPANT_ADDED", command.getParticipant().getParticipantId());

        log.info("Added participant to conversation: {}", command.getConversationId());
        return savedConversation;
    }

    @Transactional
    public Conversation removeParticipant(ConversationCommand.RemoveParticipantCommand command) {
        log.info("Removing participant from conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        if (command.getParticipantId().equals(conversation.getOwnerId())) {
            throw new ValidationException("Cannot remove the owner from the conversation");
        }

        conversation.removeParticipant(command.getParticipantId());
        Conversation savedConversation = conversationRepository.save(conversation);

        // Publish update event
        publishParticipantUpdateEvent(savedConversation, "PARTICIPANT_REMOVED", command.getParticipantId());

        log.info("Removed participant from conversation: {}", command.getConversationId());
        return savedConversation;
    }

    @Transactional
    public Conversation update(ConversationCommand.UpdateConversationCommand command) {
        log.info("Updating conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        if (command.getTitle() != null) {
            conversation.setTitle(command.getTitle());
        }
        if (command.getDescription() != null) {
            conversation.setDescription(command.getDescription());
        }
        if (command.getAssignedTo() != null) {
            conversation.assignTo(command.getAssignedTo());
        }
        if (command.getPriority() != null) {
            if (command.getPriority() < 1 || command.getPriority() > 3) {
                throw new ValidationException("priority", "Priority must be between 1 and 3");
            }
            conversation.setPriority(command.getPriority());
        }
        if (command.getRelatedEntityType() != null && command.getRelatedEntityId() != null) {
            conversation.linkToEntity(command.getRelatedEntityType(), command.getRelatedEntityId());
        }

        Conversation savedConversation = conversationRepository.save(conversation);

        // Publish update event
        publishUpdateEvent(savedConversation, "UPDATED");

        log.info("Updated conversation: {}", command.getConversationId());
        return savedConversation;
    }

    @Transactional
    public void markAsRead(ConversationCommand.MarkAsReadCommand command) {
        log.info("Marking conversation as read: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        conversation.markAsRead(command.getUserId());
        conversationRepository.save(conversation);

        log.info("Marked conversation as read: {}", command.getConversationId());
    }

    @Transactional
    public void archive(ConversationCommand.ArchiveCommand command) {
        log.info("Archiving conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        conversation.archive(command.getArchivedBy());
        conversationRepository.save(conversation);

        // Publish update event
        publishUpdateEvent(conversation, "ARCHIVED");

        log.info("Archived conversation: {}", command.getConversationId());
    }

    @Transactional
    public void resolve(ConversationCommand.ResolveCommand command) {
        log.info("Resolving conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        conversation.resolve(command.getResolvedBy(), command.getResolutionNotes());
        conversationRepository.save(conversation);

        // Publish update event
        publishUpdateEvent(conversation, "RESOLVED");

        log.info("Resolved conversation: {}", command.getConversationId());
    }

    @Transactional
    public void assign(ConversationCommand.AssignCommand command) {
        log.info("Assigning conversation: {} to: {} for tenant: {}",
                command.getConversationId(), command.getAssignedTo(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        conversation.assignTo(command.getAssignedTo());
        conversationRepository.save(conversation);

        // Publish update event
        publishUpdateEvent(conversation, "ASSIGNED");

        log.info("Assigned conversation: {}", command.getConversationId());
    }

    @Transactional
    public void addTag(ConversationCommand.AddTagCommand command) {
        log.info("Adding tag to conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        conversation.addTag(command.getTag());
        conversationRepository.save(conversation);

        // Publish update event
        publishUpdateEvent(conversation, "TAGGED");

        log.info("Added tag to conversation: {}", command.getConversationId());
    }

    @Transactional
    public void setSlaDeadline(ConversationCommand.SetSlaDeadlineCommand command) {
        log.info("Setting SLA deadline for conversation: {} for tenant: {}",
                command.getConversationId(), command.getTenantId());

        Conversation conversation = conversationRepository.findByConversationIdAndTenantId(
                        command.getConversationId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Conversation", command.getConversationId()));

        conversation.setSlaDeadline(command.getDeadline());
        conversationRepository.save(conversation);

        log.info("Set SLA deadline for conversation: {}", command.getConversationId());
    }

    private void publishEvents(Conversation conversation) {
        if (!conversation.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(new ArrayList<>(conversation.getDomainEvents()));
            conversation.clearDomainEvents();
        }
    }

    private void publishParticipantUpdateEvent(Conversation conversation, String updateType, String participantId) {
        if (eventPublisher.isReady()) {
            Map<String, Object> changes = new HashMap<>();
            changes.put("participantId", participantId);

            eventPublisher.publish(ConversationUpdatedEvent.builder()
                    .conversationId(conversation.getConversationId())
                    .tenantId(conversation.getTenantId())
                    .updateType(updateType)
                    .timestamp(java.time.Instant.now())
                    .changes(changes)
                    .build());
        }
    }

    private void publishUpdateEvent(Conversation conversation, String updateType) {
        if (eventPublisher.isReady()) {
            eventPublisher.publish(ConversationUpdatedEvent.builder()
                    .conversationId(conversation.getConversationId())
                    .tenantId(conversation.getTenantId())
                    .updateType(updateType)
                    .timestamp(java.time.Instant.now())
                    .changes(new HashMap<>())
                    .build());
        }
    }
}
