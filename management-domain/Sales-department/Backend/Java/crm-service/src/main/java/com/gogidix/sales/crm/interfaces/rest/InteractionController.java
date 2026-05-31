package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.application.dto.response.InteractionResponseDto;
import com.gogidix.sales.crm.application.service.InteractionCommandService;
import com.gogidix.sales.crm.application.service.InteractionQueryService;
import com.gogidix.sales.crm.domain.model.Interaction;
import com.gogidix.sales.crm.domain.port.in.InteractionCommand;
import com.gogidix.sales.crm.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interaction REST Controller
 * Handles HTTP requests for interaction operations
 */
@RestController
@RequestMapping("/interactions")
@RequiredArgsConstructor
@Tag(name = "Interactions", description = "Interaction tracking endpoints")
public class InteractionController {

    private final InteractionCommandService interactionCommandService;
    private final InteractionQueryService interactionQueryService;

    @PostMapping
    @Operation(summary = "Create a new interaction")
    public ResponseEntity<InteractionResponseDto> createInteraction(
            @Valid @RequestBody CreateInteractionRequestDto request) {
        InteractionCommand.CreateInteractionCommand command = new InteractionCommand.CreateInteractionCommand(
            RequestContextHolder.getTenantId(),
            request.getCustomerId(),
            request.getCustomerName(),
            request.getContactId(),
            request.getContactName(),
            request.getType(),
            request.getDirection(),
            request.getInteractionDate(),
            request.getSubject(),
            request.getDescription(),
            request.getLocation(),
            request.getAssignedTo(),
            request.getAssignedToName(),
            request.getCampaignId(),
            request.getDealId(),
            request.getDealValue(),
            request.getProbability(),
            request.getIsHighPriority(),
            request.getNotes(),
            request.getParticipantContactIds()
        );

        Interaction interaction = interactionCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(interaction));
    }

    @GetMapping("/{interactionId}")
    @Operation(summary = "Get interaction by ID")
    public ResponseEntity<InteractionResponseDto> getInteraction(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId) {
        Interaction interaction = interactionQueryService.getByInteractionIdAndTenantId(
            interactionId, RequestContextHolder.getTenantId());
        return ResponseEntity.ok(toDto(interaction));
    }

    @GetMapping
    @Operation(summary = "Get all interactions for tenant")
    public ResponseEntity<List<InteractionResponseDto>> getAllInteractions() {
        List<Interaction> interactions = interactionQueryService.getAllForTenant(
            RequestContextHolder.getTenantId());
        return ResponseEntity.ok(interactions.stream().map(this::toDto).toList());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get interactions for customer")
    public ResponseEntity<List<InteractionResponseDto>> getInteractionsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        List<Interaction> interactions = interactionQueryService.getByCustomerId(
            RequestContextHolder.getTenantId(), customerId);
        return ResponseEntity.ok(interactions.stream().map(this::toDto).toList());
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Get upcoming scheduled interactions")
    public ResponseEntity<List<InteractionResponseDto>> getUpcomingInteractions() {
        List<Interaction> interactions = interactionQueryService.getUpcomingInteractions(
            RequestContextHolder.getTenantId(), LocalDateTime.now());
        return ResponseEntity.ok(interactions.stream().map(this::toDto).toList());
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue interactions")
    public ResponseEntity<List<InteractionResponseDto>> getOverdueInteractions() {
        List<Interaction> interactions = interactionQueryService.getOverdueInteractions(
            RequestContextHolder.getTenantId());
        return ResponseEntity.ok(interactions.stream().map(this::toDto).toList());
    }

    @GetMapping("/follow-up-needed")
    @Operation(summary = "Get interactions needing follow-up")
    public ResponseEntity<List<InteractionResponseDto>> getInteractionsNeedingFollowUp(
            @RequestParam LocalDate beforeDate) {
        List<Interaction> interactions = interactionQueryService.getInteractionsNeedingFollowUp(
            RequestContextHolder.getTenantId(), beforeDate);
        return ResponseEntity.ok(interactions.stream().map(this::toDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get interaction summary")
    public ResponseEntity<InteractionQueryService.InteractionSummary> getSummary() {
        InteractionQueryService.InteractionSummary summary =
            interactionQueryService.getSummary(RequestContextHolder.getTenantId());
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/{interactionId}/complete")
    @Operation(summary = "Complete interaction")
    public ResponseEntity<Void> completeInteraction(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId,
            @RequestBody CompleteInteractionRequestDto request) {
        InteractionCommand.CompleteInteractionCommand command = new InteractionCommand.CompleteInteractionCommand(
            RequestContextHolder.getTenantId(),
            interactionId,
            request.getOutcome(),
            request.getNotes(),
            request.getDurationMinutes(),
            request.getFollowUpDate(),
            request.getFollowUpNotes(),
            request.getNextStep(),
            request.getNextStepDate()
        );

        interactionCommandService.complete(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{interactionId}/cancel")
    @Operation(summary = "Cancel interaction")
    public ResponseEntity<Void> cancelInteraction(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId,
            @RequestBody CancelInteractionRequestDto request) {
        InteractionCommand.CancelInteractionCommand command = new InteractionCommand.CancelInteractionCommand(
            RequestContextHolder.getTenantId(),
            interactionId,
            request.getReason()
        );

        interactionCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{interactionId}/reschedule")
    @Operation(summary = "Reschedule interaction")
    public ResponseEntity<Void> rescheduleInteraction(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId,
            @RequestBody RescheduleInteractionRequestDto request) {
        InteractionCommand.RescheduleInteractionCommand command = new InteractionCommand.RescheduleInteractionCommand(
            RequestContextHolder.getTenantId(),
            interactionId,
            request.getNewDate(),
            request.getReason()
        );

        interactionCommandService.reschedule(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{interactionId}/participants")
    @Operation(summary = "Add participant to interaction")
    public ResponseEntity<Void> addParticipant(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId,
            @RequestBody AddParticipantRequestDto request) {
        InteractionCommand.AddParticipantCommand command = new InteractionCommand.AddParticipantCommand(
            RequestContextHolder.getTenantId(),
            interactionId,
            request.getContactId()
        );

        interactionCommandService.addParticipant(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{interactionId}/attachments")
    @Operation(summary = "Add attachment to interaction")
    public ResponseEntity<Void> addAttachment(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId,
            @RequestBody AddAttachmentRequestDto request) {
        InteractionCommand.AddAttachmentCommand command = new InteractionCommand.AddAttachmentCommand(
            RequestContextHolder.getTenantId(),
            interactionId,
            request.getAttachmentUrl()
        );

        interactionCommandService.addAttachment(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{interactionId}/deal")
    @Operation(summary = "Associate interaction with deal")
    public ResponseEntity<Void> associateWithDeal(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId,
            @RequestBody AssociateWithDealRequestDto request) {
        InteractionCommand.AssociateWithDealCommand command = new InteractionCommand.AssociateWithDealCommand(
            RequestContextHolder.getTenantId(),
            interactionId,
            request.getDealId(),
            request.getDealValue()
        );

        interactionCommandService.associateWithDeal(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{interactionId}/high-priority")
    @Operation(summary = "Mark interaction as high priority")
    public ResponseEntity<Void> markAsHighPriority(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId) {
        interactionCommandService.markAsHighPriority(RequestContextHolder.getTenantId(), interactionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{interactionId}")
    @Operation(summary = "Delete interaction")
    public ResponseEntity<Void> deleteInteraction(
            @Parameter(description = "Interaction ID") @PathVariable String interactionId) {
        InteractionCommand.DeleteInteractionCommand command = new InteractionCommand.DeleteInteractionCommand(
            RequestContextHolder.getTenantId(),
            interactionId
        );

        interactionCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private InteractionResponseDto toDto(Interaction interaction) {
        return InteractionResponseDto.builder()
            .id(interaction.getId())
            .interactionId(interaction.getInteractionId())
            .tenantId(interaction.getTenantId())
            .customerId(interaction.getCustomerId())
            .customerName(interaction.getCustomerName())
            .contactId(interaction.getContactId())
            .contactName(interaction.getContactName())
            .type(mapInteractionType(interaction.getType()))
            .direction(mapInteractionDirection(interaction.getDirection()))
            .interactionDate(interaction.getInteractionDate())
            .durationMinutes(interaction.getDurationMinutes())
            .subject(interaction.getSubject())
            .description(interaction.getDescription())
            .outcome(interaction.getOutcome())
            .notes(interaction.getNotes())
            .location(interaction.getLocation())
            .hasFollowUp(interaction.getHasFollowUp())
            .followUpDate(interaction.getFollowUpDate())
            .followUpNotes(interaction.getFollowUpNotes())
            .assignedTo(interaction.getAssignedTo())
            .assignedToName(interaction.getAssignedToName())
            .status(mapInteractionStatus(interaction.getStatus()))
            .recordingUrl(interaction.getRecordingUrl())
            .attachmentUrls(interaction.getAttachmentUrls())
            .participantContactIds(interaction.getParticipantContactIds())
            .campaignId(interaction.getCampaignId())
            .dealId(interaction.getDealId())
            .dealValue(interaction.getDealValue())
            .probability(interaction.getProbability())
            .nextStep(interaction.getNextStep())
            .nextStepDate(interaction.getNextStepDate())
            .isHighPriority(interaction.getIsHighPriority())
            .tags(interaction.getTags())
            .relatedInteractions(interaction.getRelatedInteractions())
            .createdAt(interaction.getCreatedAt())
            .updatedAt(interaction.getUpdatedAt())
            .build();
    }

    private InteractionResponseDto.InteractionTypeDto mapInteractionType(Interaction.InteractionType type) {
        return type != null ? InteractionResponseDto.InteractionTypeDto.valueOf(type.name()) : null;
    }

    private InteractionResponseDto.InteractionDirectionDto mapInteractionDirection(Interaction.InteractionDirection direction) {
        return direction != null ? InteractionResponseDto.InteractionDirectionDto.valueOf(direction.name()) : null;
    }

    private InteractionResponseDto.InteractionStatusDto mapInteractionStatus(Interaction.InteractionStatus status) {
        return status != null ? InteractionResponseDto.InteractionStatusDto.valueOf(status.name()) : null;
    }

    // Request DTOs
    @Data
    public static class CreateInteractionRequestDto {
        public String customerId;
        public String customerName;
        public String contactId;
        public String contactName;
        public Interaction.InteractionType type;
        public Interaction.InteractionDirection direction;
        public LocalDateTime interactionDate;
        public String subject;
        public String description;
        public String location;
        public String assignedTo;
        public String assignedToName;
        public String campaignId;
        public String dealId;
        public Double dealValue;
        public Integer probability;
        public Boolean isHighPriority;
        public String notes;
        public List<String> participantContactIds;
    }

    @Data

    public static class CompleteInteractionRequestDto {
        public String outcome;
        public String notes;
        public Integer durationMinutes;
        public LocalDate followUpDate;
        public String followUpNotes;
        public String nextStep;
        public LocalDate nextStepDate;
    }

    @Data

    public static class CancelInteractionRequestDto {
        public String reason;
    }

    @Data

    public static class RescheduleInteractionRequestDto {
        public LocalDateTime newDate;
        public String reason;
    }

    @Data

    public static class AddParticipantRequestDto {
        public String contactId;
    }

    @Data

    public static class AddAttachmentRequestDto {
        public String attachmentUrl;
    }

    @Data

    public static class AssociateWithDealRequestDto {
        public String dealId;
        public Double dealValue;
    }
}
