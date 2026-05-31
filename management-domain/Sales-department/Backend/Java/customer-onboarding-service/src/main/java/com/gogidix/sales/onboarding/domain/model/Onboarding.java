package com.gogidix.sales.onboarding.domain.model;

import com.gogidix.sales.onboarding.domain.event.*;
import com.gogidix.sales.onboarding.domain.valueobject.*;
import com.gogidix.sales.onboarding.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Onboarding Domain Entity
 * Multi-tenant customer onboarding workflow management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "onboardings")
public class Onboarding extends BaseEntity {

    private String onboardingId;

    private String tenantId;

    private String customerId;

    private String customerName;

    private String customerEmail;

    private CustomerType customerType;

    private String templateId;

    private String templateName;

    private OnboardingStatus status;

    private Priority priority;

    private String assignedTo;

    private String assignedBy;

    private Instant assignedAt;

    private Instant startedAt;

    private Instant completedAt;

    private Instant estimatedCompletionDate;

    private Integer estimatedDurationHours;

    private Long actualDurationMinutes;

    private String initiatedBy;

    private String completedBy;

    private String notes;

    private String cancellationReason;

    @Builder.Default
    private List<OnboardingStep> steps = new ArrayList<>();

    @Builder.Default
    private DocumentChecklist documentChecklist = DocumentChecklist.builder().build();

    private Map<String, Object> metadata;

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    private Integer progressPercentage;

    private Instant lastProgressUpdate;

    /**
     * Creates a new onboarding process
     */
    public static Onboarding create(String tenantId, String customerId, String customerName,
                                     String customerEmail, CustomerType customerType,
                                     String templateId, String templateName,
                                     String initiatedBy, Priority priority,
                                     List<OnboardingStep> steps, Integer estimatedDurationHours) {

        Onboarding onboarding = Onboarding.builder()
                .onboardingId(generateOnboardingId())
                .tenantId(tenantId)
                .customerId(customerId)
                .customerName(customerName)
                .customerEmail(customerEmail)
                .customerType(customerType)
                .templateId(templateId)
                .templateName(templateName)
                .status(OnboardingStatus.NOT_STARTED)
                .priority(priority != null ? priority : Priority.MEDIUM)
                .initiatedBy(initiatedBy)
                .steps(steps != null ? steps : new ArrayList<>())
                .estimatedDurationHours(estimatedDurationHours != null ? estimatedDurationHours : 24)
                .documentChecklist(DocumentChecklist.builder()
                        .checklistId(generateChecklistId())
                        .tenantId(tenantId)
                        .onboardingId(generateOnboardingId())
                        .documents(new ArrayList<>())
                        .build())
                .progressPercentage(0)
                .build();

        onboarding.addDomainEvent(OnboardingStartedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .onboardingId(onboarding.getOnboardingId())
                .tenantId(tenantId)
                .customerId(customerId)
                .customerName(customerName)
                .customerType(customerType.name())
                .templateId(templateId)
                .initiatedBy(initiatedBy)
                .timestamp(Instant.now())
                .eventType("ONBOARDING_CREATED")
                .build());

        return onboarding;
    }

    /**
     * Starts the onboarding process
     */
    public void start(String startedBy) {
        if (this.status != OnboardingStatus.NOT_STARTED) {
            throw new IllegalStateException("Can only start onboarding in NOT_STARTED status");
        }

        this.status = OnboardingStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
        this.estimatedCompletionDate = calculateEstimatedCompletion();

        addDomainEvent(OnboardingStartedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .onboardingId(this.onboardingId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .customerName(this.customerName)
                .customerType(this.customerType.name())
                .templateId(this.templateId)
                .initiatedBy(startedBy)
                .timestamp(Instant.now())
                .eventType("ONBOARDING_STARTED")
                .build());

        updateProgress();
    }

    /**
     * Assigns the onboarding to a user
     */
    public void assignTo(String userId, String assignedBy) {
        this.assignedTo = userId;
        this.assignedBy = assignedBy;
        this.assignedAt = Instant.now();
    }

    /**
     * Updates a step status
     */
    public void updateStepStatus(String stepId, StepStatus status, String updatedBy, String notes) {
        OnboardingStep step = findStepById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));

        StepStatus previousStatus = step.getStatus();
        step.setStatus(status);
        step.setUpdatedBy(updatedBy);
        step.setUpdatedAt(Instant.now());

        if (notes != null) {
            step.setNotes(notes);
        }

        if (status == StepStatus.IN_PROGRESS && previousStatus != StepStatus.IN_PROGRESS) {
            step.setStartedAt(Instant.now());
        }

        if (status == StepStatus.COMPLETED && step.getCompletedAt() == null) {
            completeStep(step, updatedBy);
        }

        updateProgress();
        checkAutoTransition();
    }

    /**
     * Completes a step
     */
    private void completeStep(OnboardingStep step, String completedBy) {
        step.setCompletedAt(Instant.now());
        step.setCompletedBy(completedBy);

        if (step.getStartedAt() != null) {
            long duration = ChronoUnit.MINUTES.between(step.getStartedAt(), step.getCompletedAt());
            step.setDurationMinutes(duration);
        }

        addDomainEvent(OnboardingStepCompletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .onboardingId(this.onboardingId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .stepId(step.getStepId())
                .stepName(step.getName())
                .stepType(step.getStepType().name())
                .stepOrder(step.getOrder())
                .completedBy(completedBy)
                .startedAt(step.getStartedAt())
                .completedAt(step.getCompletedAt())
                .durationMinutes(step.getDurationMinutes())
                .notes(step.getNotes())
                .timestamp(Instant.now())
                .eventType("STEP_COMPLETED")
                .build());
    }

    /**
     * Skips a step
     */
    public void skipStep(String stepId, String skippedBy, String reason) {
        OnboardingStep step = findStepById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));

        if (!Boolean.TRUE.equals(step.getOptional())) {
            throw new IllegalStateException("Cannot skip required step: " + step.getName());
        }

        step.setStatus(StepStatus.SKIPPED);
        step.setUpdatedBy(skippedBy);
        step.setUpdatedAt(Instant.now());
        step.setNotes(reason);

        updateProgress();
        checkAutoTransition();
    }

    /**
     * Updates the overall progress
     */
    public void updateProgress() {
        if (this.steps.isEmpty()) {
            this.progressPercentage = 0;
            return;
        }

        long completedSteps = this.steps.stream()
                .filter(s -> s.getStatus() == StepStatus.COMPLETED)
                .count();

        long totalSteps = this.steps.size();
        this.progressPercentage = (int) ((completedSteps * 100) / totalSteps);
        this.lastProgressUpdate = Instant.now();
    }

    /**
     * Checks if onboarding can be auto-transitioned
     */
    private void checkAutoTransition() {
        if (allStepsCompleted()) {
            if (Boolean.TRUE.equals(this.documentChecklist.getCompleted())) {
                this.status = OnboardingStatus.PENDING_REVIEW;
            }
        }
    }

    /**
     * Completes the onboarding process
     */
    public void complete(String completedBy) {
        if (this.status == OnboardingStatus.COMPLETED) {
            throw new IllegalStateException("Onboarding is already completed");
        }

        if (!allStepsCompleted()) {
            throw new IllegalStateException("Cannot complete onboarding with pending steps");
        }

        this.status = OnboardingStatus.COMPLETED;
        this.completedBy = completedBy;
        this.completedAt = Instant.now();

        if (this.startedAt != null) {
            this.actualDurationMinutes = ChronoUnit.MINUTES.between(this.startedAt, this.completedAt);
        }

        addDomainEvent(OnboardingCompletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .onboardingId(this.onboardingId)
                .tenantId(this.tenantId)
                .customerId(this.customerId)
                .customerName(this.customerName)
                .customerType(this.customerType.name())
                .templateId(this.templateId)
                .totalSteps(this.steps.size())
                .completedSteps((int) this.steps.stream().filter(s -> s.getStatus() == StepStatus.COMPLETED).count())
                .completedBy(completedBy)
                .startedAt(this.startedAt)
                .completedAt(this.completedAt)
                .durationMinutes(this.actualDurationMinutes)
                .timestamp(Instant.now())
                .eventType("ONBOARDING_COMPLETED")
                .build());
    }

    /**
     * Puts onboarding on hold
     */
    public void putOnHold(String reason, String updatedBy) {
        if (this.status == OnboardingStatus.COMPLETED || this.status == OnboardingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot put onboarding on hold in current status");
        }

        this.status = OnboardingStatus.ON_HOLD;
        this.notes = reason;
    }

    /**
     * Resumes onboarding
     */
    public void resume(String resumedBy) {
        if (this.status != OnboardingStatus.ON_HOLD) {
            throw new IllegalStateException("Can only resume onboarding that is on hold");
        }

        this.status = OnboardingStatus.IN_PROGRESS;
    }

    /**
     * Cancels the onboarding
     */
    public void cancel(String reason, String cancelledBy) {
        if (this.status == OnboardingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed onboarding");
        }

        this.status = OnboardingStatus.CANCELLED;
        this.cancellationReason = reason;
        this.completedBy = cancelledBy;
        this.completedAt = Instant.now();
    }

    /**
     * Updates document checklist status
     */
    public void updateDocumentChecklistStatus() {
        this.documentChecklist.updateStatus();
        if (Boolean.TRUE.equals(this.documentChecklist.getCompleted()) && allStepsCompleted()) {
            this.status = OnboardingStatus.PENDING_REVIEW;
        }
        updateProgress();
    }

    /**
     * Checks if all steps are completed
     */
    public boolean allStepsCompleted() {
        return this.steps.stream()
                .allMatch(s -> s.getStatus().isTerminal() || Boolean.TRUE.equals(s.getOptional()));
    }

    /**
     * Gets next pending step
     */
    public Optional<OnboardingStep> getNextPendingStep() {
        return this.steps.stream()
                .filter(s -> s.getStatus() == StepStatus.PENDING)
                .filter(this::canStartStep)
                .min(Comparator.comparingInt(OnboardingStep::getOrder));
    }

    /**
     * Checks if a step can be started based on dependencies
     */
    private boolean canStartStep(OnboardingStep step) {
        if (step.getDependencies() == null || step.getDependencies().isEmpty()) {
            return true;
        }

        return step.getDependencies().stream()
                .allMatch(depId -> findStepById(depId)
                        .map(s -> s.getStatus() == StepStatus.COMPLETED)
                        .orElse(false));
    }

    /**
     * Calculates estimated completion date
     */
    private Instant calculateEstimatedCompletion() {
        if (this.estimatedDurationHours == null) {
            return Instant.now().plus(7, ChronoUnit.DAYS);
        }
        return Instant.now().plus(this.estimatedDurationHours, ChronoUnit.HOURS);
    }

    /**
     * Finds a step by ID
     */
    private Optional<OnboardingStep> findStepById(String stepId) {
        return this.steps.stream()
                .filter(s -> s.getStepId().equals(stepId))
                .findFirst();
    }

    /**
     * Adds a domain event
     */
    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    /**
     * Clears domain events
     */
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    private static String generateOnboardingId() {
        return "ONB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static String generateChecklistId() {
        return "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
