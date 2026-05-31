package com.gogidix.sales.onboarding.interfaces.rest;

import com.gogidix.sales.onboarding.application.dto.response.OnboardingResponseDto;
import com.gogidix.sales.onboarding.application.dto.response.OnboardingTemplateResponseDto;
import com.gogidix.sales.onboarding.application.service.OnboardingCommandService;
import com.gogidix.sales.onboarding.application.service.OnboardingQueryService;
import com.gogidix.sales.onboarding.domain.model.DocumentChecklist;
import com.gogidix.sales.onboarding.domain.model.Onboarding;
import com.gogidix.sales.onboarding.domain.model.OnboardingTemplate;
import com.gogidix.sales.onboarding.domain.model.TemplateStep;
import com.gogidix.sales.onboarding.domain.port.in.OnboardingCommand;
import com.gogidix.sales.onboarding.domain.port.in.OnboardingTemplateCommand;
import com.gogidix.sales.onboarding.domain.port.in.OnboardingQuery;
import com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Onboarding REST Controller
 * Handles HTTP requests for onboarding operations
 */
@RestController
@RequestMapping("/onboardings")
@RequiredArgsConstructor
@Tag(name = "Customer Onboarding", description = "Customer onboarding workflow management endpoints")
public class OnboardingController {

    private final OnboardingCommandService onboardingCommandService;
    private final OnboardingQueryService onboardingQueryService;

    // ========== Onboarding Operations ==========

    @PostMapping
    @Operation(summary = "Create a new onboarding")
    public ResponseEntity<OnboardingResponseDto> createOnboarding(
            @Valid @RequestBody CreateOnboardingRequestDto request) {

        OnboardingCommand.CreateOnboardingCommand command = new OnboardingCommand.CreateOnboardingCommand(
                RequestContextHolder.getTenantId(),
                request.getCustomerId(),
                request.getCustomerName(),
                request.getCustomerEmail(),
                request.getCustomerType(),
                request.getTemplateId(),
                request.getTemplateName(),
                RequestContextHolder.getUserId(),
                request.getPriority(),
                null,
                null
        );

        Onboarding onboarding = onboardingCommandService.createOnboarding(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(onboarding));
    }

    @GetMapping("/{onboardingId}")
    @Operation(summary = "Get onboarding by ID")
    public ResponseEntity<OnboardingResponseDto> getOnboarding(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId) {

        Onboarding onboarding = onboardingQueryService.getById(onboardingId);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @GetMapping
    @Operation(summary = "Get all onboardings for tenant")
    public ResponseEntity<List<OnboardingResponseDto>> getAllOnboardings() {
        List<Onboarding> onboardings = onboardingQueryService.getAllForTenant();
        return ResponseEntity.ok(onboardings.stream().map(this::toDto).toList());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get onboardings by customer")
    public ResponseEntity<List<OnboardingResponseDto>> getOnboardingsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {

        List<Onboarding> onboardings = onboardingQueryService.getByCustomerId(customerId);
        return ResponseEntity.ok(onboardings.stream().map(this::toDto).toList());
    }

    @GetMapping("/assigned")
    @Operation(summary = "Get onboardings assigned to current user")
    public ResponseEntity<List<OnboardingResponseDto>> getAssignedOnboardings() {
        List<Onboarding> onboardings = onboardingQueryService.getByAssignedUser();
        return ResponseEntity.ok(onboardings.stream().map(this::toDto).toList());
    }

    @PostMapping("/{onboardingId}/start")
    @Operation(summary = "Start onboarding process")
    public ResponseEntity<OnboardingResponseDto> startOnboarding(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId) {

        OnboardingCommand.StartOnboardingCommand command = new OnboardingCommand.StartOnboardingCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                RequestContextHolder.getUserId()
        );

        Onboarding onboarding = onboardingCommandService.startOnboarding(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/assign")
    @Operation(summary = "Assign onboarding to a user")
    public ResponseEntity<OnboardingResponseDto> assignOnboarding(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @RequestBody AssignRequestDto request) {

        OnboardingCommand.AssignOnboardingCommand command = new OnboardingCommand.AssignOnboardingCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                request.getAssignee(),
                RequestContextHolder.getUserId()
        );

        Onboarding onboarding = onboardingCommandService.assignOnboarding(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/steps/{stepId}")
    @Operation(summary = "Update onboarding step status")
    public ResponseEntity<OnboardingResponseDto> updateStep(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @Parameter(description = "Step ID") @PathVariable String stepId,
            @RequestBody UpdateStepRequestDto request) {

        OnboardingCommand.UpdateStepCommand command = new OnboardingCommand.UpdateStepCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                stepId,
                request.getStatus(),
                RequestContextHolder.getUserId(),
                request.getNotes()
        );

        Onboarding onboarding = onboardingCommandService.updateStep(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/steps/{stepId}/skip")
    @Operation(summary = "Skip an optional onboarding step")
    public ResponseEntity<OnboardingResponseDto> skipStep(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @Parameter(description = "Step ID") @PathVariable String stepId,
            @RequestBody SkipStepRequestDto request) {

        OnboardingCommand.SkipStepCommand command = new OnboardingCommand.SkipStepCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                stepId,
                RequestContextHolder.getUserId(),
                request.getReason()
        );

        Onboarding onboarding = onboardingCommandService.skipStep(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/complete")
    @Operation(summary = "Complete onboarding process")
    public ResponseEntity<OnboardingResponseDto> completeOnboarding(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId) {

        OnboardingCommand.CompleteOnboardingCommand command = new OnboardingCommand.CompleteOnboardingCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                RequestContextHolder.getUserId()
        );

        Onboarding onboarding = onboardingCommandService.completeOnboarding(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/hold")
    @Operation(summary = "Put onboarding on hold")
    public ResponseEntity<OnboardingResponseDto> putOnHold(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @RequestBody PutOnHoldRequestDto request) {

        OnboardingCommand.PutOnHoldCommand command = new OnboardingCommand.PutOnHoldCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                request.getReason(),
                RequestContextHolder.getUserId()
        );

        Onboarding onboarding = onboardingCommandService.putOnHold(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/resume")
    @Operation(summary = "Resume onboarding process")
    public ResponseEntity<OnboardingResponseDto> resumeOnboarding(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId) {

        OnboardingCommand.ResumeOnboardingCommand command = new OnboardingCommand.ResumeOnboardingCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                RequestContextHolder.getUserId()
        );

        Onboarding onboarding = onboardingCommandService.resumeOnboarding(command);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/cancel")
    @Operation(summary = "Cancel onboarding process")
    public ResponseEntity<Void> cancelOnboarding(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @RequestBody CancelRequestDto request) {

        OnboardingCommand.CancelOnboardingCommand command = new OnboardingCommand.CancelOnboardingCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                request.getReason(),
                RequestContextHolder.getUserId()
        );

        onboardingCommandService.cancelOnboarding(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{onboardingId}/documents")
    @Operation(summary = "Upload document for onboarding")
    public ResponseEntity<OnboardingResponseDto> uploadDocument(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @RequestBody UploadDocumentRequestDto request) {

        OnboardingCommand.UploadDocumentCommand command = new OnboardingCommand.UploadDocumentCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                request.getChecklistId(),
                request.getItemId(),
                request.getFileName(),
                request.getFileUrl(),
                request.getFileSizeBytes(),
                RequestContextHolder.getUserId()
        );

        onboardingCommandService.uploadDocument(command);
        Onboarding onboarding = onboardingQueryService.getById(onboardingId);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @PostMapping("/{onboardingId}/documents/verify")
    @Operation(summary = "Verify document")
    public ResponseEntity<OnboardingResponseDto> verifyDocument(
            @Parameter(description = "Onboarding ID") @PathVariable String onboardingId,
            @RequestBody VerifyDocumentRequestDto request) {

        OnboardingCommand.VerifyDocumentCommand command = new OnboardingCommand.VerifyDocumentCommand(
                RequestContextHolder.getTenantId(),
                onboardingId,
                request.getChecklistId(),
                request.getItemId(),
                request.getApproved(),
                RequestContextHolder.getUserId(),
                request.getNotes()
        );

        onboardingCommandService.verifyDocument(command);
        Onboarding onboarding = onboardingQueryService.getById(onboardingId);
        return ResponseEntity.ok(toDto(onboarding));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get onboarding summary")
    public ResponseEntity<OnboardingQuery.OnboardingSummary> getSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        OnboardingQuery.OnboardingSummary summary = onboardingQueryService.getSummary(tenantId);
        return ResponseEntity.ok(summary);
    }

    // ========== Template Operations ==========

    @GetMapping("/templates")
    @Operation(summary = "Get all onboarding templates")
    public ResponseEntity<List<OnboardingTemplateResponseDto>> getAllTemplates() {
        List<OnboardingTemplate> templates = onboardingQueryService.getAllTemplates();
        return ResponseEntity.ok(templates.stream().map(this::toTemplateDto).toList());
    }

    @GetMapping("/templates/active")
    @Operation(summary = "Get active onboarding templates")
    public ResponseEntity<List<OnboardingTemplateResponseDto>> getActiveTemplates() {
        List<OnboardingTemplate> templates = onboardingQueryService.getActiveTemplates();
        return ResponseEntity.ok(templates.stream().map(this::toTemplateDto).toList());
    }

    @GetMapping("/templates/{templateId}")
    @Operation(summary = "Get template by ID")
    public ResponseEntity<OnboardingTemplateResponseDto> getTemplate(
            @Parameter(description = "Template ID") @PathVariable String templateId) {

        OnboardingTemplate template = onboardingQueryService.getTemplateById(templateId);
        return ResponseEntity.ok(toTemplateDto(template));
    }

    @PostMapping("/templates")
    @Operation(summary = "Create a new onboarding template")
    public ResponseEntity<OnboardingTemplateResponseDto> createTemplate(
            @Valid @RequestBody CreateTemplateRequestDto request) {

        OnboardingTemplateCommand.CreateTemplateCommand command = new OnboardingTemplateCommand.CreateTemplateCommand(
                RequestContextHolder.getTenantId(),
                request.getName(),
                request.getDescription(),
                request.getCustomerType(),
                request.getSteps(),
                request.getRequiredDocumentTypes(),
                request.getEstimatedDurationHours(),
                request.getWelcomeEmailTemplate(),
                request.getAutoAssign(),
                request.getDefaultAssigneeRole(),
                RequestContextHolder.getUserId()
        );

        OnboardingTemplate template = onboardingCommandService.createTemplate(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toTemplateDto(template));
    }

    @PutMapping("/templates/{templateId}")
    @Operation(summary = "Update onboarding template")
    public ResponseEntity<OnboardingTemplateResponseDto> updateTemplate(
            @Parameter(description = "Template ID") @PathVariable String templateId,
            @RequestBody UpdateTemplateRequestDto request) {

        OnboardingTemplateCommand.UpdateTemplateCommand command = new OnboardingTemplateCommand.UpdateTemplateCommand(
                RequestContextHolder.getTenantId(),
                templateId,
                request.getName(),
                request.getDescription(),
                request.getSteps(),
                request.getRequiredDocumentTypes(),
                request.getEstimatedDurationHours(),
                request.getWelcomeEmailTemplate(),
                request.getAutoAssign(),
                request.getDefaultAssigneeRole(),
                RequestContextHolder.getUserId()
        );

        OnboardingTemplate template = onboardingCommandService.updateTemplate(command);
        return ResponseEntity.ok(toTemplateDto(template));
    }

    @PostMapping("/templates/{templateId}/activate")
    @Operation(summary = "Activate onboarding template")
    public ResponseEntity<OnboardingTemplateResponseDto> activateTemplate(
            @Parameter(description = "Template ID") @PathVariable String templateId) {

        OnboardingTemplateCommand.ActivateTemplateCommand command = new OnboardingTemplateCommand.ActivateTemplateCommand(
                RequestContextHolder.getTenantId(),
                templateId
        );

        OnboardingTemplate template = onboardingCommandService.activateTemplate(command);
        return ResponseEntity.ok(toTemplateDto(template));
    }

    @PostMapping("/templates/{templateId}/deactivate")
    @Operation(summary = "Deactivate onboarding template")
    public ResponseEntity<OnboardingTemplateResponseDto> deactivateTemplate(
            @Parameter(description = "Template ID") @PathVariable String templateId) {

        OnboardingTemplateCommand.DeactivateTemplateCommand command = new OnboardingTemplateCommand.DeactivateTemplateCommand(
                RequestContextHolder.getTenantId(),
                templateId
        );

        OnboardingTemplate template = onboardingCommandService.deactivateTemplate(command);
        return ResponseEntity.ok(toTemplateDto(template));
    }

    // ========== Helper Methods ==========

    private OnboardingResponseDto toDto(Onboarding onboarding) {
        return OnboardingResponseDto.builder()
                .id(onboarding.getId())
                .onboardingId(onboarding.getOnboardingId())
                .tenantId(onboarding.getTenantId())
                .customerId(onboarding.getCustomerId())
                .customerName(onboarding.getCustomerName())
                .customerEmail(onboarding.getCustomerEmail())
                .customerType(mapCustomerType(onboarding.getCustomerType()))
                .templateId(onboarding.getTemplateId())
                .templateName(onboarding.getTemplateName())
                .status(mapStatus(onboarding.getStatus()))
                .priority(mapPriority(onboarding.getPriority()))
                .assignedTo(onboarding.getAssignedTo())
                .assignedBy(onboarding.getAssignedBy())
                .assignedAt(onboarding.getAssignedAt())
                .startedAt(onboarding.getStartedAt())
                .completedAt(onboarding.getCompletedAt())
                .estimatedCompletionDate(onboarding.getEstimatedCompletionDate())
                .estimatedDurationHours(onboarding.getEstimatedDurationHours())
                .actualDurationMinutes(onboarding.getActualDurationMinutes())
                .initiatedBy(onboarding.getInitiatedBy())
                .completedBy(onboarding.getCompletedBy())
                .notes(onboarding.getNotes())
                .cancellationReason(onboarding.getCancellationReason())
                .steps(onboarding.getSteps().stream().map(this::mapStep).toList())
                .documentChecklist(mapDocumentChecklist(onboarding.getDocumentChecklist()))
                .metadata(onboarding.getMetadata())
                .progressPercentage(onboarding.getProgressPercentage())
                .lastProgressUpdate(onboarding.getLastProgressUpdate())
                .createdAt(onboarding.getCreatedAt())
                .updatedAt(onboarding.getUpdatedAt())
                .build();
    }

    private OnboardingTemplateResponseDto toTemplateDto(OnboardingTemplate template) {
        return OnboardingTemplateResponseDto.builder()
                .id(template.getId())
                .templateId(template.getTemplateId())
                .tenantId(template.getTenantId())
                .name(template.getName())
                .description(template.getDescription())
                .customerType(template.getCustomerType())
                .active(template.getActive())
                .version(template.getVersion())
                .parentTemplateId(template.getParentTemplateId())
                .steps(template.getSteps().stream().map(this::mapTemplateStep).toList())
                .requiredDocumentTypes(template.getRequiredDocumentTypes())
                .estimatedDurationHours(template.getEstimatedDurationHours())
                .welcomeEmailTemplate(template.getWelcomeEmailTemplate())
                .autoAssign(template.getAutoAssign())
                .defaultAssigneeRole(template.getDefaultAssigneeRole())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }

    private OnboardingResponseDto.OnboardingStepDto mapStep(com.gogidix.sales.onboarding.domain.model.OnboardingStep step) {
        return OnboardingResponseDto.OnboardingStepDto.builder()
                .stepId(step.getStepId())
                .onboardingId(step.getOnboardingId())
                .name(step.getName())
                .description(step.getDescription())
                .stepType(mapStepType(step.getStepType()))
                .status(mapStepStatus(step.getStatus()))
                .order(step.getOrder())
                .optional(step.getOptional())
                .autoComplete(step.getAutoComplete())
                .assignedTo(step.getAssignedTo())
                .assignedBy(step.getAssignedBy())
                .startedAt(step.getStartedAt())
                .completedAt(step.getCompletedAt())
                .durationMinutes(step.getDurationMinutes())
                .completedBy(step.getCompletedBy())
                .updatedBy(step.getUpdatedBy())
                .updatedAt(step.getUpdatedAt())
                .dependencies(step.getDependencies())
                .notes(step.getNotes())
                .estimatedDurationMinutes(step.getEstimatedDurationMinutes())
                .helpUrl(step.getHelpUrl())
                .build();
    }

    private OnboardingTemplateResponseDto.TemplateStepDto mapTemplateStep(TemplateStep step) {
        return OnboardingTemplateResponseDto.TemplateStepDto.builder()
                .stepId(step.getStepId())
                .name(step.getName())
                .description(step.getDescription())
                .stepType(step.getStepType().name())
                .order(step.getOrder())
                .optional(step.getOptional())
                .autoComplete(step.getAutoComplete())
                .dependencies(step.getDependencies())
                .estimatedDurationMinutes(step.getEstimatedDurationMinutes())
                .helpUrl(step.getHelpUrl())
                .requiredFields(step.getRequiredFields())
                .assigneeRole(step.getAssigneeRole())
                .build();
    }

    private OnboardingResponseDto.DocumentChecklistDto mapDocumentChecklist(DocumentChecklist checklist) {
        if (checklist == null) {
            return null;
        }
        return OnboardingResponseDto.DocumentChecklistDto.builder()
                .id(checklist.getId())
                .checklistId(checklist.getChecklistId())
                .onboardingId(checklist.getOnboardingId())
                .tenantId(checklist.getTenantId())
                .customerId(checklist.getCustomerId())
                .documents(checklist.getDocuments().stream().map(this::mapDocumentItem).toList())
                .requireAllDocuments(checklist.getRequireAllDocuments())
                .totalRequired(checklist.getTotalRequired())
                .totalCompleted(checklist.getTotalCompleted())
                .completed(checklist.getCompleted())
                .completedAt(checklist.getCompletedAt())
                .build();
    }

    private OnboardingResponseDto.DocumentItemDto mapDocumentItem(DocumentChecklist.DocumentItem item) {
        return OnboardingResponseDto.DocumentItemDto.builder()
                .itemId(item.getItemId())
                .documentType(item.getDocumentType())
                .fileName(item.getFileName())
                .fileUrl(item.getFileUrl())
                .fileSizeBytes(item.getFileSizeBytes())
                .required(item.getRequired())
                .status(item.getStatus().name())
                .uploadedBy(item.getUploadedBy())
                .uploadedAt(item.getUploadedAt())
                .verifiedBy(item.getVerifiedBy())
                .verifiedAt(item.getVerifiedAt())
                .verificationNotes(item.getVerificationNotes())
                .notes(item.getNotes())
                .build();
    }

    private OnboardingResponseDto.CustomerTypeDto mapCustomerType(com.gogidix.sales.onboarding.domain.valueobject.CustomerType type) {
        return type != null ? OnboardingResponseDto.CustomerTypeDto.valueOf(type.name()) : null;
    }

    private OnboardingResponseDto.OnboardingStatusDto mapStatus(com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus status) {
        return status != null ? OnboardingResponseDto.OnboardingStatusDto.valueOf(status.name()) : null;
    }

    private OnboardingResponseDto.PriorityDto mapPriority(com.gogidix.sales.onboarding.domain.valueobject.Priority priority) {
        return priority != null ? OnboardingResponseDto.PriorityDto.valueOf(priority.name()) : null;
    }

    private OnboardingResponseDto.StepTypeDto mapStepType(com.gogidix.sales.onboarding.domain.valueobject.StepType type) {
        return type != null ? OnboardingResponseDto.StepTypeDto.valueOf(type.name()) : null;
    }

    private OnboardingResponseDto.StepStatusDto mapStepStatus(com.gogidix.sales.onboarding.domain.valueobject.StepStatus status) {
        return status != null ? OnboardingResponseDto.StepStatusDto.valueOf(status.name()) : null;
    }

    // ========== Request DTOs ==========

    @Data

    public static class CreateOnboardingRequestDto {
        public String customerId;
        public String customerName;
        public String customerEmail;
        public com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType;
        public String templateId;
        public String templateName;
        public com.gogidix.sales.onboarding.domain.valueobject.Priority priority;
    }

    @Data

    public static class AssignRequestDto {
        public String assignee;
    }

    @Data

    public static class UpdateStepRequestDto {
        public com.gogidix.sales.onboarding.domain.valueobject.StepStatus status;
        public String notes;
    }

    @Data

    public static class SkipStepRequestDto {
        public String reason;
    }

    @Data

    public static class PutOnHoldRequestDto {
        public String reason;
    }

    @Data

    public static class CancelRequestDto {
        public String reason;
    }

    @Data

    public static class UploadDocumentRequestDto {
        public String checklistId;
        public String itemId;
        public String fileName;
        public String fileUrl;
        public Long fileSizeBytes;
    }

    @Data

    public static class VerifyDocumentRequestDto {
        public String checklistId;
        public String itemId;
        public Boolean approved;
        public String notes;
    }

    @Data

    public static class CreateTemplateRequestDto {
        public String name;
        public String description;
        public com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType;
        public List<TemplateStep> steps;
        public List<String> requiredDocumentTypes;
        public Integer estimatedDurationHours;
        public String welcomeEmailTemplate;
        public Boolean autoAssign;
        public String defaultAssigneeRole;
    }

    @Data

    public static class UpdateTemplateRequestDto {
        public String name;
        public String description;
        public List<TemplateStep> steps;
        public List<String> requiredDocumentTypes;
        public Integer estimatedDurationHours;
        public String welcomeEmailTemplate;
        public Boolean autoAssign;
        public String defaultAssigneeRole;
    }
}
