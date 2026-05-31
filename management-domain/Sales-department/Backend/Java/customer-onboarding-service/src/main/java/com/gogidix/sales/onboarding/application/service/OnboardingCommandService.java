package com.gogidix.sales.onboarding.application.service;

import com.gogidix.sales.onboarding.domain.event.DocumentUploadedEvent;
import com.gogidix.sales.onboarding.domain.model.*;
import com.gogidix.sales.onboarding.domain.port.in.OnboardingCommand;
import com.gogidix.sales.onboarding.domain.port.in.OnboardingTemplateCommand;
import com.gogidix.sales.onboarding.domain.port.out.EmailService;
import com.gogidix.sales.onboarding.domain.port.out.EventPublisher;
import com.gogidix.sales.onboarding.domain.repository.*;
import com.gogidix.sales.onboarding.shared.exception.ConflictException;
import com.gogidix.sales.onboarding.shared.exception.NotFoundException;
import com.gogidix.sales.onboarding.shared.exception.ValidationException;
import com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Onboarding Command Service
 * Handles all write operations for onboarding
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingCommandService {

    private final OnboardingRepository onboardingRepository;
    private final OnboardingTemplateRepository templateRepository;
    private final DocumentChecklistRepository checklistRepository;
    private final EventPublisher eventPublisher;
    private final EmailService emailService;

    @Transactional
    public Onboarding createOnboarding(OnboardingCommand.CreateOnboardingCommand command) {
        log.info("Creating onboarding for customer: {} in tenant: {}",
                command.getCustomerId(), command.getTenantId());

        // Check if customer already has an active onboarding
        List<Onboarding> existing = onboardingRepository.findByTenantIdAndCustomerId(
                command.getTenantId(), command.getCustomerId());
        if (existing.stream().anyMatch(o -> !o.getStatus().isTerminal())) {
            throw new ConflictException("Customer already has an active onboarding process");
        }

        // Load template to get steps
        OnboardingTemplate template = templateRepository
                .findByTemplateIdAndTenantId(command.getTemplateId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Template", command.getTemplateId()));

        List<OnboardingStep> steps = template.createOnboardingSteps("pending");

        Onboarding onboarding = Onboarding.create(
                command.getTenantId(),
                command.getCustomerId(),
                command.getCustomerName(),
                command.getCustomerEmail(),
                command.getCustomerType(),
                command.getTemplateId(),
                template.getName(),
                command.getInitiatedBy(),
                command.getPriority(),
                steps,
                template.getEstimatedDurationHours()
        );

        // Create document checklist
        DocumentChecklist checklist = DocumentChecklist.create(
                onboarding.getOnboardingId(),
                command.getTenantId(),
                command.getCustomerId(),
                template.getRequiredDocumentTypes()
        );
        checklistRepository.save(checklist);
        onboarding.setDocumentChecklist(checklist);

        // Auto-assign if configured
        if (template.getAutoAssign() != null && template.getAutoAssign()) {
            onboarding.assignTo(template.getDefaultAssigneeRole(), command.getInitiatedBy());
        }

        Onboarding savedOnboarding = onboardingRepository.save(onboarding);
        publishEvents(savedOnboarding);

        // Send welcome email
        if (emailService.isReady()) {
            Map<String, Object> context = buildEmailContext(savedOnboarding);
            emailService.sendWelcomeEmail(savedOnboarding, context);
        }

        log.info("Created onboarding: {} for customer: {}",
                savedOnboarding.getOnboardingId(), command.getCustomerId());
        return savedOnboarding;
    }

    @Transactional
    public Onboarding startOnboarding(OnboardingCommand.StartOnboardingCommand command) {
        log.info("Starting onboarding: {} for tenant: {}",
                command.getOnboardingId(), command.getTenantId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.start(command.getStartedBy());
        Onboarding savedOnboarding = onboardingRepository.save(onboarding);
        publishEvents(savedOnboarding);

        // Send assignment email if assigned
        if (savedOnboarding.getAssignedTo() != null && emailService.isReady()) {
            Map<String, Object> context = buildEmailContext(savedOnboarding);
            emailService.sendAssignmentEmail(savedOnboarding, savedOnboarding.getAssignedTo(), context);
        }

        log.info("Started onboarding: {}", command.getOnboardingId());
        return savedOnboarding;
    }

    @Transactional
    public Onboarding updateStep(OnboardingCommand.UpdateStepCommand command) {
        log.info("Updating step: {} for onboarding: {}",
                command.getStepId(), command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.updateStepStatus(command.getStepId(), command.getStatus(),
                command.getUpdatedBy(), command.getNotes());

        Onboarding savedOnboarding = onboardingRepository.save(onboarding);
        publishEvents(savedOnboarding);

        // Send step completion email
        if (command.getStatus() == com.gogidix.sales.onboarding.domain.valueobject.StepStatus.COMPLETED
                && emailService.isReady()) {
            OnboardingStep step = savedOnboarding.getSteps().stream()
                    .filter(s -> s.getStepId().equals(command.getStepId()))
                    .findFirst()
                    .orElse(null);

            if (step != null) {
                Map<String, Object> context = buildEmailContext(savedOnboarding);
                context.put("stepName", step.getName());
                emailService.sendStepCompletionEmail(savedOnboarding, step.getName(), context);
            }
        }

        return savedOnboarding;
    }

    @Transactional
    public Onboarding skipStep(OnboardingCommand.SkipStepCommand command) {
        log.info("Skipping step: {} for onboarding: {}",
                command.getStepId(), command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.skipStep(command.getStepId(), command.getSkippedBy(), command.getReason());
        Onboarding savedOnboarding = onboardingRepository.save(onboarding);

        return savedOnboarding;
    }

    @Transactional
    public Onboarding assignOnboarding(OnboardingCommand.AssignOnboardingCommand command) {
        log.info("Assigning onboarding: {} to: {}",
                command.getOnboardingId(), command.getAssignee());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.assignTo(command.getAssignee(), command.getAssignedBy());
        Onboarding savedOnboarding = onboardingRepository.save(onboarding);

        // Send assignment email
        if (emailService.isReady()) {
            Map<String, Object> context = buildEmailContext(savedOnboarding);
            emailService.sendAssignmentEmail(savedOnboarding, command.getAssignee(), context);
        }

        return savedOnboarding;
    }

    @Transactional
    public Onboarding completeOnboarding(OnboardingCommand.CompleteOnboardingCommand command) {
        log.info("Completing onboarding: {} for tenant: {}",
                command.getOnboardingId(), command.getTenantId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.complete(command.getCompletedBy());
        Onboarding savedOnboarding = onboardingRepository.save(onboarding);
        publishEvents(savedOnboarding);

        // Send completion email
        if (emailService.isReady()) {
            Map<String, Object> context = buildEmailContext(savedOnboarding);
            emailService.sendOnboardingCompletionEmail(savedOnboarding, context);
        }

        log.info("Completed onboarding: {}", command.getOnboardingId());
        return savedOnboarding;
    }

    @Transactional
    public Onboarding putOnHold(OnboardingCommand.PutOnHoldCommand command) {
        log.info("Putting onboarding: {} on hold", command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.putOnHold(command.getReason(), command.getUpdatedBy());
        return onboardingRepository.save(onboarding);
    }

    @Transactional
    public Onboarding resumeOnboarding(OnboardingCommand.ResumeOnboardingCommand command) {
        log.info("Resuming onboarding: {}", command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.resume(command.getResumedBy());
        return onboardingRepository.save(onboarding);
    }

    @Transactional
    public Onboarding cancelOnboarding(OnboardingCommand.CancelOnboardingCommand command) {
        log.info("Cancelling onboarding: {}", command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        onboarding.cancel(command.getReason(), command.getCancelledBy());
        return onboardingRepository.save(onboarding);
    }

    @Transactional
    public DocumentChecklist uploadDocument(OnboardingCommand.UploadDocumentCommand command) {
        log.info("Uploading document: {} for onboarding: {}",
                command.getItemId(), command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        DocumentChecklist checklist = checklistRepository
                .findByOnboardingId(command.getOnboardingId())
                .orElseThrow(() -> new NotFoundException("DocumentChecklist", command.getChecklistId()));

        checklist.uploadDocument(command.getItemId(), command.getFileName(),
                command.getFileUrl(), command.getFileSizeBytes(), command.getUploadedBy());

        DocumentChecklist savedChecklist = checklistRepository.save(checklist);
        onboarding.updateDocumentChecklistStatus();
        onboardingRepository.save(onboarding);

        // Publish document upload event
        if (eventPublisher.isReady()) {
            DocumentChecklist.DocumentItem item = checklist.getDocuments().stream()
                    .filter(d -> d.getItemId().equals(command.getItemId()))
                    .findFirst()
                    .orElse(null);

            if (item != null) {
                DocumentUploadedEvent event = DocumentUploadedEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .onboardingId(command.getOnboardingId())
                        .tenantId(command.getTenantId())
                        .customerId(onboarding.getCustomerId())
                        .documentChecklistId(checklist.getChecklistId())
                        .documentType(item.getDocumentType())
                        .documentName(item.getFileName())
                        .documentUrl(item.getFileUrl())
                        .fileSizeBytes(item.getFileSizeBytes())
                        .uploadedBy(command.getUploadedBy())
                        .timestamp(java.time.Instant.now())
                        .eventType("DOCUMENT_UPLOADED")
                        .build();
                eventPublisher.publish(event);
            }
        }

        return savedChecklist;
    }

    @Transactional
    public DocumentChecklist verifyDocument(OnboardingCommand.VerifyDocumentCommand command) {
        log.info("Verifying document: {} for onboarding: {}",
                command.getItemId(), command.getOnboardingId());

        Onboarding onboarding = onboardingRepository
                .findByOnboardingIdAndTenantId(command.getOnboardingId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Onboarding", command.getOnboardingId()));

        DocumentChecklist checklist = checklistRepository
                .findByOnboardingId(command.getOnboardingId())
                .orElseThrow(() -> new NotFoundException("DocumentChecklist", command.getChecklistId()));

        checklist.verifyDocument(command.getItemId(), command.getVerifiedBy(),
                command.getApproved(), command.getNotes());

        DocumentChecklist savedChecklist = checklistRepository.save(checklist);
        onboarding.updateDocumentChecklistStatus();
        onboardingRepository.save(onboarding);

        return savedChecklist;
    }

    @Transactional
    public OnboardingTemplate createTemplate(OnboardingTemplateCommand.CreateTemplateCommand command) {
        log.info("Creating onboarding template: {} for tenant: {}",
                command.getName(), command.getTenantId());

        OnboardingTemplate template = OnboardingTemplate.create(
                command.getTenantId(),
                command.getName(),
                command.getDescription(),
                command.getCustomerType(),
                command.getSteps(),
                command.getEstimatedDurationHours(),
                command.getCreatedBy()
        );

        template.setRequiredDocumentTypes(command.getRequiredDocumentTypes());
        template.setWelcomeEmailTemplate(command.getWelcomeEmailTemplate());
        template.setAutoAssign(command.getAutoAssign());
        template.setDefaultAssigneeRole(command.getDefaultAssigneeRole());

        return templateRepository.save(template);
    }

    @Transactional
    public OnboardingTemplate updateTemplate(OnboardingTemplateCommand.UpdateTemplateCommand command) {
        log.info("Updating template: {} for tenant: {}",
                command.getTemplateId(), command.getTenantId());

        OnboardingTemplate template = templateRepository
                .findByTemplateIdAndTenantId(command.getTemplateId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Template", command.getTemplateId()));

        if (command.getName() != null) {
            template.setName(command.getName());
        }
        if (command.getDescription() != null) {
            template.setDescription(command.getDescription());
        }
        if (command.getSteps() != null) {
            template.setSteps(command.getSteps());
        }
        if (command.getRequiredDocumentTypes() != null) {
            template.setRequiredDocumentTypes(command.getRequiredDocumentTypes());
        }
        if (command.getEstimatedDurationHours() != null) {
            template.setEstimatedDurationHours(command.getEstimatedDurationHours());
        }
        if (command.getWelcomeEmailTemplate() != null) {
            template.setWelcomeEmailTemplate(command.getWelcomeEmailTemplate());
        }
        if (command.getAutoAssign() != null) {
            template.setAutoAssign(command.getAutoAssign());
        }
        if (command.getDefaultAssigneeRole() != null) {
            template.setDefaultAssigneeRole(command.getDefaultAssigneeRole());
        }

        return templateRepository.save(template);
    }

    @Transactional
    public OnboardingTemplate activateTemplate(OnboardingTemplateCommand.ActivateTemplateCommand command) {
        OnboardingTemplate template = templateRepository
                .findByTemplateIdAndTenantId(command.getTemplateId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Template", command.getTemplateId()));

        template.activate();
        return templateRepository.save(template);
    }

    @Transactional
    public OnboardingTemplate deactivateTemplate(OnboardingTemplateCommand.DeactivateTemplateCommand command) {
        OnboardingTemplate template = templateRepository
                .findByTemplateIdAndTenantId(command.getTemplateId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Template", command.getTemplateId()));

        template.deactivate();
        return templateRepository.save(template);
    }

    @Transactional
    public OnboardingTemplate createNewVersion(OnboardingTemplateCommand.CreateNewVersionCommand command) {
        OnboardingTemplate template = templateRepository
                .findByTemplateIdAndTenantId(command.getTemplateId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Template", command.getTemplateId()));

        OnboardingTemplate newVersion = template.createNewVersion(command.getUpdatedBy());
        templateRepository.save(template);
        return templateRepository.save(newVersion);
    }

    private void publishEvents(Onboarding onboarding) {
        if (!onboarding.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(onboarding.getDomainEvents());
            onboarding.clearDomainEvents();
        }
    }

    private Map<String, Object> buildEmailContext(Onboarding onboarding) {
        Map<String, Object> context = new HashMap<>();
        context.put("onboardingId", onboarding.getOnboardingId());
        context.put("customerName", onboarding.getCustomerName());
        context.put("customerEmail", onboarding.getCustomerEmail());
        context.put("customerType", onboarding.getCustomerType().name());
        context.put("templateName", onboarding.getTemplateName());
        context.put("progress", onboarding.getProgressPercentage());
        context.put("estimatedCompletionDate", onboarding.getEstimatedCompletionDate());
        return context;
    }
}
