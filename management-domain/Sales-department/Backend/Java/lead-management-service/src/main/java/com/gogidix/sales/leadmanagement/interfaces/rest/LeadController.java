package com.gogidix.sales.leadmanagement.interfaces.rest;

import com.gogidix.sales.leadmanagement.application.dto.response.ErrorResponseDto;
import com.gogidix.sales.leadmanagement.application.dto.response.LeadActivityResponseDto;
import com.gogidix.sales.leadmanagement.application.dto.response.LeadResponseDto;
import com.gogidix.sales.leadmanagement.application.service.LeadCommandService;
import com.gogidix.sales.leadmanagement.application.service.LeadQueryService;
import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
import com.gogidix.sales.leadmanagement.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Lead REST Controller
 * Handles HTTP requests for lead operations
 */
@RestController
@RequestMapping("/leads")
@RequiredArgsConstructor
@Tag(name = "Leads", description = "Lead management endpoints")
public class LeadController {

    private final LeadCommandService leadCommandService;
    private final LeadQueryService leadQueryService;

    @PostMapping
    @Operation(summary = "Create a new lead")
    public ResponseEntity<LeadResponseDto> createLead(@Valid @RequestBody CreateLeadRequestDto request) {
        LeadCommand.CreateLeadCommand command = new LeadCommand.CreateLeadCommand(
                RequestContextHolder.getTenantId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getMobilePhone(),
                request.getCompany(),
                request.getTitle(),
                request.getIndustry(),
                request.getCompanySize(),
                request.getWebsite(),
                request.getLinkedInUrl(),
                request.getSource(),
                request.getSourceDetails(),
                request.getCampaign(),
                request.getTerritory(),
                request.getRegion(),
                request.getSegment(),
                request.getBudget(),
                request.getAuthority(),
                request.getNeed(),
                request.getTimeline(),
                request.getOwnerId(),
                request.getOwnerName(),
                request.getEstimatedValue(),
                request.getCurrency(),
                request.getExpectedCloseDate(),
                request.getNotes(),
                request.getTags()
        );

        Lead lead = leadCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(leadQueryService.toResponseDto(lead));
    }

    @GetMapping("/{leadId}")
    @Operation(summary = "Get lead by ID")
    public ResponseEntity<LeadResponseDto> getLead(
            @Parameter(description = "Lead ID") @PathVariable String leadId) {
        Lead lead = leadQueryService.getById(leadId);
        return ResponseEntity.ok(leadQueryService.toResponseDto(lead));
    }

    @GetMapping
    @Operation(summary = "Get all leads for tenant")
    public ResponseEntity<List<LeadResponseDto>> getAllLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Lead> leads = leadQueryService.getLeads(pageable);
        return ResponseEntity.ok(leads.map(lead -> leadQueryService.toResponseDto(lead)).getContent());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get leads by status")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByStatus(
            @Parameter(description = "Lead status") @PathVariable Lead.LeadStatus status) {
        List<Lead> leads = leadQueryService.getLeadsByStatus(status);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/stage/{stage}")
    @Operation(summary = "Get leads by stage")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByStage(
            @Parameter(description = "Lead stage") @PathVariable Lead.LeadStage stage) {
        List<Lead> leads = leadQueryService.getLeadsByStage(stage);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/source/{source}")
    @Operation(summary = "Get leads by source")
    public ResponseEntity<List<LeadResponseDto>> getLeadsBySource(
            @Parameter(description = "Lead source") @PathVariable Lead.LeadSource source) {
        List<Lead> leads = leadQueryService.getLeadsBySource(source);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/quality/{quality}")
    @Operation(summary = "Get leads by quality")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByQuality(
            @Parameter(description = "Lead quality") @PathVariable Lead.LeadQuality quality) {
        List<Lead> leads = leadQueryService.getLeadsByQuality(quality);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get leads by owner")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByOwner(
            @Parameter(description = "Owner ID") @PathVariable String ownerId) {
        List<Lead> leads = leadQueryService.getLeadsByOwner(ownerId);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/score/{minScore}")
    @Operation(summary = "Get leads with minimum score")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByMinScore(
            @Parameter(description = "Minimum score") @PathVariable Integer minScore) {
        List<Lead> leads = leadQueryService.getLeadsByMinScore(minScore);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Get leads by tag")
    public ResponseEntity<List<LeadResponseDto>> getLeadsByTag(
            @Parameter(description = "Tag") @PathVariable String tag) {
        List<Lead> leads = leadQueryService.getLeadsByTag(tag);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get lead summary")
    public ResponseEntity<LeadQueryService.LeadSummary> getSummary() {
        LeadQueryService.LeadSummary summary = leadQueryService.getLeadSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/search")
    @Operation(summary = "Search leads")
    public ResponseEntity<List<LeadResponseDto>> searchLeads(
            @Parameter(description = "Search term") @RequestParam String q) {
        List<Lead> leads = leadQueryService.searchLeads(q);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/follow-up-needed")
    @Operation(summary = "Get leads needing follow-up")
    public ResponseEntity<List<LeadResponseDto>> getLeadsNeedingFollowUp(
            @Parameter(description = "Since date (yyyy-MM-dd)") @RequestParam String since) {
        LocalDate sinceDate = LocalDate.parse(since);
        List<Lead> leads = leadQueryService.getLeadsNeedingFollowUp(sinceDate);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/stale")
    @Operation(summary = "Get stale leads")
    public ResponseEntity<List<LeadResponseDto>> getStaleLeads(
            @Parameter(description = "Stale days threshold") @RequestParam(defaultValue = "30") int staleDays) {
        List<Lead> leads = leadQueryService.getStaleLeads(staleDays);
        return ResponseEntity.ok(leads.stream().map(leadQueryService::toResponseDto).toList());
    }

    @GetMapping("/{leadId}/activities")
    @Operation(summary = "Get lead activities")
    public ResponseEntity<List<LeadActivityResponseDto>> getLeadActivities(
            @Parameter(description = "Lead ID") @PathVariable String leadId) {
        List<LeadActivity> activities = leadQueryService.getLeadActivities(leadId);
        return ResponseEntity.ok(activities.stream().map(leadQueryService::toActivityResponseDto).toList());
    }

    @PutMapping("/{leadId}")
    @Operation(summary = "Update lead")
    public ResponseEntity<LeadResponseDto> updateLead(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @Valid @RequestBody UpdateLeadRequestDto request) {

        LeadCommand.UpdateLeadCommand command = new LeadCommand.UpdateLeadCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getMobilePhone(),
                request.getCompany(),
                request.getTitle(),
                request.getIndustry(),
                request.getCompanySize(),
                request.getWebsite(),
                request.getLinkedInUrl(),
                request.getTerritory(),
                request.getRegion(),
                request.getSegment(),
                request.getBudget(),
                request.getAuthority(),
                request.getNeed(),
                request.getTimeline(),
                request.getEstimatedValue(),
                request.getCurrency(),
                request.getExpectedCloseDate(),
                request.getNotes(),
                request.getTags()
        );

        Lead lead = leadCommandService.update(command);
        return ResponseEntity.ok(leadQueryService.toResponseDto(lead));
    }

    @PostMapping("/{leadId}/assign")
    @Operation(summary = "Assign lead to owner")
    public ResponseEntity<Void> assignLead(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody AssignLeadRequestDto request) {

        LeadCommand.AssignLeadCommand command = new LeadCommand.AssignLeadCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getOwnerId(),
                request.getOwnerName(),
                request.getReason(),
                request.getAssignmentStrategy()
        );

        leadCommandService.assign(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{leadId}/advance")
    @Operation(summary = "Advance lead to next stage")
    public ResponseEntity<Void> advanceStage(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody AdvanceStageRequestDto request) {

        LeadCommand.AdvanceStageCommand command = new LeadCommand.AdvanceStageCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getNotes()
        );

        leadCommandService.advanceStage(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{leadId}/regress")
    @Operation(summary = "Regress lead to previous stage")
    public ResponseEntity<Void> regressStage(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody RegressStageRequestDto request) {

        LeadCommand.RegressStageCommand command = new LeadCommand.RegressStageCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getTargetStage(),
                request.getReason()
        );

        leadCommandService.regressStage(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{leadId}/convert")
    @Operation(summary = "Convert lead to deal")
    public ResponseEntity<Void> convertLead(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody ConvertLeadRequestDto request) {

        LeadCommand.ConvertLeadCommand command = new LeadCommand.ConvertLeadCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getDealId(),
                request.getReason()
        );

        leadCommandService.convert(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{leadId}/lose")
    @Operation(summary = "Mark lead as lost")
    public ResponseEntity<Void> markAsLost(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody MarkAsLostRequestDto request) {

        LeadCommand.MarkAsLostCommand command = new LeadCommand.MarkAsLostCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getLossReason(),
                request.getLossDetails()
        );

        leadCommandService.markAsLost(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{leadId}/score")
    @Operation(summary = "Update lead score")
    public ResponseEntity<Void> updateScore(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody UpdateScoreRequestDto request) {

        LeadCommand.UpdateScoreCommand command = new LeadCommand.UpdateScoreCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getScore()
        );

        leadCommandService.updateScore(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{leadId}/activities")
    @Operation(summary = "Add activity to lead")
    public ResponseEntity<LeadActivityResponseDto> addActivity(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @Valid @RequestBody AddActivityRequestDto request) {

        LeadCommand.AddActivityCommand command = new LeadCommand.AddActivityCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getActivityType(),
                request.getSubject(),
                request.getDescription(),
                request.getDueDate(),
                request.getPriority()
        );

        LeadActivity activity = leadCommandService.addActivity(command);
        return ResponseEntity.ok(leadQueryService.toActivityResponseDto(activity));
    }

    @PostMapping("/{leadId}/interactions")
    @Operation(summary = "Record lead interaction")
    public ResponseEntity<Void> recordInteraction(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody RecordInteractionRequestDto request) {

        LeadCommand.RecordInteractionCommand command = new LeadCommand.RecordInteractionCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getInteractionType(),
                request.getEmailOpened(),
                request.getEmailClicked(),
                request.getFormName()
        );

        leadCommandService.recordInteraction(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{leadId}/recycle")
    @Operation(summary = "Recycle lead")
    public ResponseEntity<Void> recycleLead(
            @Parameter(description = "Lead ID") @PathVariable String leadId,
            @RequestBody RecycleLeadRequestDto request) {

        LeadCommand.RecycleLeadCommand command = new LeadCommand.RecycleLeadCommand(
                RequestContextHolder.getTenantId(),
                leadId,
                request.getReason()
        );

        leadCommandService.recycle(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{leadId}")
    @Operation(summary = "Delete lead")
    public ResponseEntity<Void> deleteLead(
            @Parameter(description = "Lead ID") @PathVariable String leadId) {

        LeadCommand.DeleteLeadCommand command = new LeadCommand.DeleteLeadCommand(
                RequestContextHolder.getTenantId(),
                leadId
        );

        leadCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CreateLeadRequestDto {
        public String firstName;
        public String lastName;
        public String email;
        public String phone;
        public String mobilePhone;
        public String company;
        public String title;
        public String industry;
        public String companySize;
        public String website;
        public String linkedInUrl;
        public Lead.LeadSource source;
        public String sourceDetails;
        public String campaign;
        public String territory;
        public String region;
        public String segment;
        public Integer budget;
        public Integer authority;
        public Integer need;
        public Integer timeline;
        public String ownerId;
        public String ownerName;
        public java.math.BigDecimal estimatedValue;
        public String currency;
        public LocalDate expectedCloseDate;
        public String notes;
        public List<String> tags;
    }

    @Data
    public static class UpdateLeadRequestDto {
        public String firstName;
        public String lastName;
        public String email;
        public String phone;
        public String mobilePhone;
        public String company;
        public String title;
        public String industry;
        public String companySize;
        public String website;
        public String linkedInUrl;
        public String territory;
        public String region;
        public String segment;
        public Integer budget;
        public Integer authority;
        public Integer need;
        public Integer timeline;
        public java.math.BigDecimal estimatedValue;
        public String currency;
        public LocalDate expectedCloseDate;
        public String notes;
        public List<String> tags;
    }

    @Data
    public static class AssignLeadRequestDto {
        public String ownerId;
        public String ownerName;
        public String reason;
        public String assignmentStrategy;
    }

    @Data
    public static class AdvanceStageRequestDto {
        public String notes;
    }

    @Data
    public static class RegressStageRequestDto {
        public Lead.LeadStage targetStage;
        public String reason;
    }

    @Data
    public static class ConvertLeadRequestDto {
        public String dealId;
        public String reason;
    }

    @Data
    public static class MarkAsLostRequestDto {
        public String lossReason;
        public String lossDetails;
    }

    @Data
    public static class UpdateScoreRequestDto {
        public Integer score;
    }

    @Data
    public static class AddActivityRequestDto {
        public LeadActivity.ActivityType activityType;
        public String subject;
        public String description;
        public Instant dueDate;
        public LeadActivity.Priority priority;
    }

    @Data
    public static class RecordInteractionRequestDto {
        public LeadCommand.InteractionType interactionType;
        public Boolean emailOpened;
        public Boolean emailClicked;
        public String formName;
    }

    @Data
    public static class RecycleLeadRequestDto {
        public String reason;
    }
}
