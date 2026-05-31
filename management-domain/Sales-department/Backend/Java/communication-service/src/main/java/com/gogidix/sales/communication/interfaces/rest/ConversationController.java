package com.gogidix.sales.communication.interfaces.rest;

import com.gogidix.sales.communication.application.dto.response.ConversationResponseDto;
import com.gogidix.sales.communication.application.dto.response.PagedResponseDto;
import com.gogidix.sales.communication.application.service.ConversationCommandService;
import com.gogidix.sales.communication.application.service.ConversationQueryService;
import com.gogidix.sales.communication.domain.model.Conversation;
import com.gogidix.sales.communication.domain.port.in.ConversationCommand;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Conversation REST Controller
 * Handles HTTP requests for conversation operations
 */
@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@Tag(name = "Conversations", description = "Conversation management endpoints")
public class ConversationController {

    private final ConversationCommandService conversationCommandService;
    private final ConversationQueryService conversationQueryService;

    @PostMapping
    @Operation(summary = "Create a new conversation")
    public ResponseEntity<ConversationResponseDto> createConversation(
            @Valid @RequestBody CreateConversationRequestDto request) {

        ConversationCommand.CreateConversationCommand command = new ConversationCommand.CreateConversationCommand(
                RequestContextHolder.getTenantId(),
                request.getTitle(),
                request.getType(),
                RequestContextHolder.getUserId(),
                request.getOwnerName(),
                request.getParticipants(),
                request.getDefaultChannel(),
                request.getDescription(),
                request.getRelatedEntityType(),
                request.getRelatedEntityId()
        );

        Conversation conversation = conversationCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(conversation));
    }

    @PostMapping("/{conversationId}/participants")
    @Operation(summary = "Add participant to conversation")
    public ResponseEntity<ConversationResponseDto> addParticipant(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestBody AddParticipantRequestDto request) {

        ConversationCommand.AddParticipantCommand command = new ConversationCommand.AddParticipantCommand(
                RequestContextHolder.getTenantId(), conversationId, request.getParticipant());

        Conversation conversation = conversationCommandService.addParticipant(command);
        return ResponseEntity.ok(toDto(conversation));
    }

    @DeleteMapping("/{conversationId}/participants/{participantId}")
    @Operation(summary = "Remove participant from conversation")
    public ResponseEntity<ConversationResponseDto> removeParticipant(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @Parameter(description = "Participant ID") @PathVariable String participantId) {

        ConversationCommand.RemoveParticipantCommand command = new ConversationCommand.RemoveParticipantCommand(
                RequestContextHolder.getTenantId(), conversationId, participantId);

        Conversation conversation = conversationCommandService.removeParticipant(command);
        return ResponseEntity.ok(toDto(conversation));
    }

    @PutMapping("/{conversationId}")
    @Operation(summary = "Update conversation")
    public ResponseEntity<ConversationResponseDto> updateConversation(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestBody UpdateConversationRequestDto request) {

        ConversationCommand.UpdateConversationCommand command = new ConversationCommand.UpdateConversationCommand(
                RequestContextHolder.getTenantId(),
                conversationId,
                request.getTitle(),
                request.getDescription(),
                request.getAssignedTo(),
                request.getPriority(),
                request.getRelatedEntityType(),
                request.getRelatedEntityId()
        );

        Conversation conversation = conversationCommandService.update(command);
        return ResponseEntity.ok(toDto(conversation));
    }

    @PostMapping("/{conversationId}/read")
    @Operation(summary = "Mark conversation as read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId) {

        ConversationCommand.MarkAsReadCommand command = new ConversationCommand.MarkAsReadCommand(
                RequestContextHolder.getTenantId(), conversationId, RequestContextHolder.getUserId());

        conversationCommandService.markAsRead(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{conversationId}/archive")
    @Operation(summary = "Archive conversation")
    public ResponseEntity<Void> archiveConversation(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId) {

        ConversationCommand.ArchiveCommand command = new ConversationCommand.ArchiveCommand(
                RequestContextHolder.getTenantId(), conversationId, RequestContextHolder.getUserId());

        conversationCommandService.archive(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{conversationId}/resolve")
    @Operation(summary = "Resolve conversation")
    public ResponseEntity<Void> resolveConversation(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestBody ResolveConversationRequestDto request) {

        ConversationCommand.ResolveCommand command = new ConversationCommand.ResolveCommand(
                RequestContextHolder.getTenantId(), conversationId, RequestContextHolder.getUserId(),
                request.getResolutionNotes());

        conversationCommandService.resolve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{conversationId}/assign")
    @Operation(summary = "Assign conversation")
    public ResponseEntity<Void> assignConversation(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestBody AssignConversationRequestDto request) {

        ConversationCommand.AssignCommand command = new ConversationCommand.AssignCommand(
                RequestContextHolder.getTenantId(), conversationId, request.getAssignedTo());

        conversationCommandService.assign(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{conversationId}/tags")
    @Operation(summary = "Add tag to conversation")
    public ResponseEntity<Void> addTag(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestBody AddTagRequestDto request) {

        ConversationCommand.AddTagCommand command = new ConversationCommand.AddTagCommand(
                RequestContextHolder.getTenantId(), conversationId, request.getTag());

        conversationCommandService.addTag(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{conversationId}/sla")
    @Operation(summary = "Set SLA deadline for conversation")
    public ResponseEntity<Void> setSlaDeadline(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId,
            @RequestBody SetSlaDeadlineRequestDto request) {

        ConversationCommand.SetSlaDeadlineCommand command = new ConversationCommand.SetSlaDeadlineCommand(
                RequestContextHolder.getTenantId(), conversationId, request.getDeadline());

        conversationCommandService.setSlaDeadline(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "Get conversation by ID")
    public ResponseEntity<ConversationResponseDto> getConversation(
            @Parameter(description = "Conversation ID") @PathVariable String conversationId) {

        Conversation conversation = conversationQueryService.getById(conversationId);
        return ResponseEntity.ok(toDto(conversation));
    }

    @GetMapping
    @Operation(summary = "Get all conversations for tenant")
    public ResponseEntity<List<ConversationResponseDto>> getAllConversations() {
        List<Conversation> conversations = conversationQueryService.getAllForTenant();
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get conversations by status")
    public ResponseEntity<List<ConversationResponseDto>> getConversationsByStatus(
            @Parameter(description = "Status") @PathVariable String status) {
        List<Conversation> conversations = conversationQueryService.getByStatus(status);
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get conversations by type")
    public ResponseEntity<List<ConversationResponseDto>> getConversationsByType(
            @Parameter(description = "Type") @PathVariable String type) {
        List<Conversation> conversations = conversationQueryService.getByType(type);
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    @GetMapping("/participant/{participantId}")
    @Operation(summary = "Get conversations for participant")
    public ResponseEntity<List<ConversationResponseDto>> getConversationsByParticipant(
            @Parameter(description = "Participant ID") @PathVariable String participantId) {
        List<Conversation> conversations = conversationQueryService.getByParticipant(participantId);
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    @GetMapping("/assigned-to/{assignedTo}")
    @Operation(summary = "Get conversations assigned to user")
    public ResponseEntity<List<ConversationResponseDto>> getConversationsAssignedTo(
            @Parameter(description = "Assigned to") @PathVariable String assignedTo) {
        List<Conversation> conversations = conversationQueryService.getByAssignedTo(assignedTo);
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    @GetMapping("/archived/{archived}")
    @Operation(summary = "Get archived conversations")
    public ResponseEntity<List<ConversationResponseDto>> getArchivedConversations(
            @Parameter(description = "Archived") @PathVariable boolean archived) {
        List<Conversation> conversations = conversationQueryService.getArchived(archived);
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    @GetMapping("/search")
    @Operation(summary = "Search conversations")
    public ResponseEntity<PagedResponseDto<ConversationResponseDto>> searchConversations(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Conversation> conversations = conversationQueryService.search(searchTerm, page, size);

        PagedResponseDto<ConversationResponseDto> response = PagedResponseDto.of(
                conversations.getContent().stream().map(this::toDto).toList(),
                page, size, conversations.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get conversation summary")
    public ResponseEntity<ConversationQueryService.ConversationSummary> getSummary() {
        ConversationQueryService.ConversationSummary summary = conversationQueryService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/user")
    @Operation(summary = "Get conversation summary for user")
    public ResponseEntity<ConversationQueryService.ConversationSummary> getUserSummary() {
        ConversationQueryService.ConversationSummary summary =
                conversationQueryService.getSummaryForUser(RequestContextHolder.getUserId());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/sla-breaching")
    @Operation(summary = "Get conversations with breached SLA")
    public ResponseEntity<List<ConversationResponseDto>> getSlaBreachingConversations() {
        List<Conversation> conversations = conversationQueryService.getSlaBreachingConversations();
        return ResponseEntity.ok(conversations.stream().map(this::toDto).toList());
    }

    private ConversationResponseDto toDto(Conversation conversation) {
        return ConversationResponseDto.builder()
                .id(conversation.getId())
                .conversationId(conversation.getConversationId())
                .tenantId(conversation.getTenantId())
                .title(conversation.getTitle())
                .description(conversation.getDescription())
                .type(ConversationResponseDto.mapType(conversation.getType()))
                .ownerId(conversation.getOwnerId())
                .ownerName(conversation.getOwnerName())
                .participantIds(new ArrayList<>(conversation.getParticipantIds()))
                .participants(mapParticipants(conversation.getParticipants()))
                .status(ConversationResponseDto.mapStatus(conversation.getStatus()))
                .defaultChannel(ConversationResponseDto.mapChannelType(conversation.getDefaultChannel()))
                .unreadCount(conversation.getUnreadCount())
                .lastMessageAt(conversation.getLastMessageAt())
                .lastMessageId(conversation.getLastMessageId())
                .lastMessagePreview(conversation.getLastMessagePreview())
                .isPinned(conversation.getIsPinned())
                .isArchived(conversation.getIsArchived())
                .archivedAt(conversation.getArchivedAt())
                .archivedBy(conversation.getArchivedBy())
                .tags(conversation.getTags())
                .assignedTo(conversation.getAssignedTo())
                .priority(conversation.getPriority())
                .relatedEntityType(conversation.getRelatedEntityType())
                .relatedEntityId(conversation.getRelatedEntityId())
                .resolvedAt(conversation.getResolvedAt())
                .resolvedBy(conversation.getResolvedBy())
                .resolutionNotes(conversation.getResolutionNotes())
                .slaDeadline(conversation.getSlaDeadline())
                .slaBreach(conversation.getSlaBreach())
                .isMuted(conversation.getIsMuted())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }

    private List<ConversationResponseDto.ParticipantDetailsDto> mapParticipants(
            List<Conversation.ParticipantDetails> participants) {
        if (participants == null) return null;
        return participants.stream().map(p -> ConversationResponseDto.ParticipantDetailsDto.builder()
                .participantId(p.getParticipantId())
                .participantName(p.getParticipantName())
                .participantType(p.getParticipantType())
                .role(ConversationResponseDto.mapParticipantRole(p.getRole()))
                .joinedAt(p.getJoinedAt())
                .isActive(p.getIsActive())
                .lastReadAt(p.getLastReadAt())
                .build()).toList();
    }

    // Request DTOs
    @Data
    public static class CreateConversationRequestDto {
        public String title;
        public Conversation.ConversationType type;
        public String ownerName;
        public List<Conversation.ParticipantDetails> participants;
        public Conversation.ChannelType defaultChannel;
        public String description;
        public String relatedEntityType;
        public String relatedEntityId;
    }

    @Data

    public static class AddParticipantRequestDto {
        public Conversation.ParticipantDetails participant;
    }

    @Data

    public static class UpdateConversationRequestDto {
        public String title;
        public String description;
        public String assignedTo;
        public Integer priority;
        public String relatedEntityType;
        public String relatedEntityId;
    }

    @Data

    public static class ResolveConversationRequestDto {
        public String resolutionNotes;
    }

    @Data

    public static class AssignConversationRequestDto {
        public String assignedTo;
    }

    @Data

    public static class AddTagRequestDto {
        public String tag;
    }

    @Data

    public static class SetSlaDeadlineRequestDto {
        public Instant deadline;
    }
}
