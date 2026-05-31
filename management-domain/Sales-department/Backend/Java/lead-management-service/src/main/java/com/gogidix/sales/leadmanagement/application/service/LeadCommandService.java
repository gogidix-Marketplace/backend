package com.gogidix.sales.leadmanagement.application.service;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
import com.gogidix.sales.leadmanagement.domain.port.out.EventPublisher;
import com.gogidix.sales.leadmanagement.domain.port.out.LeadAssigner;
import com.gogidix.sales.leadmanagement.domain.port.out.LeadDuplicateDetector;
import com.gogidix.sales.leadmanagement.domain.repository.LeadRepository;
import com.gogidix.sales.leadmanagement.domain.repository.LeadActivityRepository;
import com.gogidix.sales.leadmanagement.shared.exception.ConflictException;
import com.gogidix.sales.leadmanagement.shared.exception.NotFoundException;
import com.gogidix.sales.leadmanagement.shared.exception.ValidationException;
import com.gogidix.sales.leadmanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lead Command Service
 * Handles all write operations for leads
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeadCommandService {

    private final LeadRepository leadRepository;
    private final LeadActivityRepository activityRepository;
    private final EventPublisher eventPublisher;
    private final LeadDuplicateDetector duplicateDetector;
    private final LeadAssigner leadAssigner;

    @Value("${lead-management-service.assignment.enabled:true}")
    private boolean autoAssignmentEnabled;

    @Transactional
    public Lead create(LeadCommand.CreateLeadCommand command) {
        log.info("Creating lead for tenant: {}, email: {}", command.getTenantId(), command.getEmail());

        // Check for duplicates
        if (duplicateDetector.isEnabled()) {
            List<Lead> duplicates = duplicateDetector.findDuplicates(
                    command.getTenantId(),
                    command.getEmail(),
                    command.getPhone(),
                    command.getFirstName(),
                    command.getLastName()
            );

            if (!duplicates.isEmpty()) {
                Lead original = duplicates.get(0);
                log.warn("Duplicate lead detected. Original: {}", original.getLeadId());
                throw new ConflictException("Duplicate lead detected. Existing lead: " + original.getLeadId());
            }
        }

        // Determine owner if not specified
        String ownerId = command.getOwnerId();
        if (ownerId == null && autoAssignmentEnabled) {
            ownerId = leadAssigner.assignLead(command.getTenantId(), null, null).orElse(null);
            log.info("Auto-assigned lead to owner: {}", ownerId);
        }

        Lead lead = Lead.create(
                command.getTenantId(),
                command.getFirstName(),
                command.getLastName(),
                command.getEmail(),
                command.getPhone(),
                command.getCompany(),
                command.getSource(),
                ownerId
        );

        // Set additional fields
        lead.setMobilePhone(command.getMobilePhone());
        lead.setTitle(command.getTitle());
        lead.setIndustry(command.getIndustry());
        lead.setCompanySize(command.getCompanySize());
        lead.setWebsite(command.getWebsite());
        lead.setLinkedInUrl(command.getLinkedInUrl());
        lead.setSourceDetails(command.getSourceDetails());
        lead.setCampaign(command.getCampaign());
        lead.setTerritory(command.getTerritory());
        lead.setRegion(command.getRegion());
        lead.setSegment(command.getSegment());
        lead.setBudget(command.getBudget());
        lead.setAuthority(command.getAuthority());
        lead.setNeed(command.getNeed());
        lead.setTimeline(command.getTimeline());
        lead.setEstimatedValue(command.getEstimatedValue());
        lead.setCurrency(command.getCurrency());
        lead.setExpectedCloseDate(command.getExpectedCloseDate());
        lead.setNotes(command.getNotes());

        if (command.getOwnerName() != null) {
            lead.setOwnerName(command.getOwnerName());
        }

        // Set tags
        if (command.getTags() != null && !command.getTags().isEmpty()) {
            command.getTags().forEach(lead::addTag);
        }

        // Add creation activity
        lead.addActivity(ownerId != null ? ownerId : "system",
                LeadActivity.ActivityType.NOTE,
                "Lead created",
                "Lead captured from " + command.getSource().getDisplayName());

        Lead savedLead = leadRepository.save(lead);
        publishEvents(savedLead);

        log.info("Created lead: {} for tenant: {}", savedLead.getLeadId(), command.getTenantId());
        return savedLead;
    }

    @Transactional
    public Lead update(LeadCommand.UpdateLeadCommand command) {
        log.info("Updating lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        if (lead.isClosed()) {
            throw new ValidationException("Cannot update closed leads");
        }

        // Update fields
        if (command.getFirstName() != null) {
            lead.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            lead.setLastName(command.getLastName());
        }
        if (command.getEmail() != null) {
            lead.setEmail(command.getEmail());
        }
        if (command.getPhone() != null) {
            lead.setPhone(command.getPhone());
        }
        if (command.getMobilePhone() != null) {
            lead.setMobilePhone(command.getMobilePhone());
        }
        if (command.getCompany() != null) {
            lead.setCompany(command.getCompany());
        }
        if (command.getTitle() != null) {
            lead.setTitle(command.getTitle());
        }
        if (command.getIndustry() != null) {
            lead.setIndustry(command.getIndustry());
        }
        if (command.getCompanySize() != null) {
            lead.setCompanySize(command.getCompanySize());
        }
        if (command.getWebsite() != null) {
            lead.setWebsite(command.getWebsite());
        }
        if (command.getLinkedInUrl() != null) {
            lead.setLinkedInUrl(command.getLinkedInUrl());
        }
        if (command.getTerritory() != null) {
            lead.setTerritory(command.getTerritory());
        }
        if (command.getRegion() != null) {
            lead.setRegion(command.getRegion());
        }
        if (command.getSegment() != null) {
            lead.setSegment(command.getSegment());
        }
        if (command.getBudget() != null) {
            lead.setBudget(command.getBudget());
        }
        if (command.getAuthority() != null) {
            lead.setAuthority(command.getAuthority());
        }
        if (command.getNeed() != null) {
            lead.setNeed(command.getNeed());
        }
        if (command.getTimeline() != null) {
            lead.setTimeline(command.getTimeline());
        }
        if (command.getEstimatedValue() != null) {
            lead.setEstimatedValue(command.getEstimatedValue());
        }
        if (command.getCurrency() != null) {
            lead.setCurrency(command.getCurrency());
        }
        if (command.getExpectedCloseDate() != null) {
            lead.setExpectedCloseDate(command.getExpectedCloseDate());
        }
        if (command.getNotes() != null) {
            lead.setNotes(command.getNotes());
        }

        // Update BANT score
        lead.calculateBantScore();

        // Update tags
        if (command.getTags() != null) {
            lead.getTagList().clear();
            command.getTags().forEach(lead::addTag);
        }

        Lead savedLead = leadRepository.save(lead);

        log.info("Updated lead: {}", command.getLeadId());
        return savedLead;
    }

    @Transactional
    public void assign(LeadCommand.AssignLeadCommand command) {
        log.info("Assigning lead: {} to owner: {}", command.getLeadId(), command.getOwnerId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        String previousOwner = lead.getOwnerId();
        lead.assignTo(command.getOwnerId(), command.getOwnerName(),
                RequestContextHolder.getUserId(), command.getReason());

        leadRepository.save(lead);

        log.info("Assigned lead: {} from {} to {}", command.getLeadId(), previousOwner, command.getOwnerId());
    }

    @Transactional
    public void advanceStage(LeadCommand.AdvanceStageCommand command) {
        log.info("Advancing stage for lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        String userId = RequestContextHolder.getUserId();

        lead.advanceStage(userId, command.getNotes());
        leadRepository.save(lead);
        publishEvents(lead);

        log.info("Advanced lead: {} to stage: {}", command.getLeadId(), lead.getStage());
    }

    @Transactional
    public void regressStage(LeadCommand.RegressStageCommand command) {
        log.info("Regressing stage for lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        String userId = RequestContextHolder.getUserId();

        lead.regressStage(userId, command.getTargetStage(), command.getReason());
        leadRepository.save(lead);
        publishEvents(lead);

        log.info("Regressed lead: {} to stage: {}", command.getLeadId(), command.getTargetStage());
    }

    @Transactional
    public void convert(LeadCommand.ConvertLeadCommand command) {
        log.info("Converting lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        String userId = RequestContextHolder.getUserId();

        lead.markAsConverted(userId, command.getDealId(), command.getReason());
        leadRepository.save(lead);
        publishEvents(lead);

        log.info("Converted lead: {} to deal: {}", command.getLeadId(), command.getDealId());
    }

    @Transactional
    public void markAsLost(LeadCommand.MarkAsLostCommand command) {
        log.info("Marking lead as lost: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        String userId = RequestContextHolder.getUserId();

        lead.markAsLost(userId, command.getLossReason(), command.getLossDetails());
        leadRepository.save(lead);
        publishEvents(lead);

        log.info("Marked lead as lost: {}", command.getLeadId());
    }

    @Transactional
    public void updateScore(LeadCommand.UpdateScoreCommand command) {
        log.info("Updating score for lead: {} to {}", command.getLeadId(), command.getScore());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        lead.updateScore(command.getScore());
        leadRepository.save(lead);

        log.info("Updated lead score: {} to {}", command.getLeadId(), command.getScore());
    }

    @Transactional
    public LeadActivity addActivity(LeadCommand.AddActivityCommand command) {
        log.info("Adding activity to lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        LeadActivity activity;
        String userId = RequestContextHolder.getUserId();

        if (command.getDueDate() != null) {
            activity = LeadActivity.schedule(
                    command.getLeadId(),
                    command.getTenantId(),
                    userId,
                    command.getActivityType(),
                    command.getSubject(),
                    command.getDueDate(),
                    command.getPriority() != null ? command.getPriority() : LeadActivity.Priority.MEDIUM
            );
            activity.setDescription(command.getDescription());
        } else {
            activity = LeadActivity.create(
                    command.getLeadId(),
                    command.getTenantId(),
                    userId,
                    command.getActivityType(),
                    command.getSubject(),
                    command.getDescription()
            );
        }

        LeadActivity savedActivity = activityRepository.save(activity);
        lead.addActivity(userId, command.getActivityType(), command.getSubject(), command.getDescription());
        leadRepository.save(lead);

        log.info("Added activity: {} to lead: {}", savedActivity.getActivityId(), command.getLeadId());
        return savedActivity;
    }

    @Transactional
    public void recordInteraction(LeadCommand.RecordInteractionCommand command) {
        log.info("Recording interaction for lead: {}", command.getLeadId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        switch (command.getInteractionType()) {
            case EMAIL_OPEN -> {
                if (command.getEmailOpened() != null && command.getEmailOpened()) {
                    lead.recordEmailInteraction(true, false);
                }
            }
            case EMAIL_CLICK -> {
                if (command.getEmailClicked() != null && command.getEmailClicked()) {
                    lead.recordEmailInteraction(true, true);
                }
            }
            case WEB_VISIT -> lead.recordWebVisit();
            case FORM_SUBMIT -> lead.recordFormSubmission(command.getFormName() != null ? command.getFormName() : "Unknown Form");
        }

        leadRepository.save(lead);

        log.info("Recorded interaction: {} for lead: {}", command.getInteractionType(), command.getLeadId());
    }

    @Transactional
    public void recycle(LeadCommand.RecycleLeadCommand command) {
        log.info("Recycling lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        String userId = RequestContextHolder.getUserId();

        lead.recycle(userId, command.getReason());
        leadRepository.save(lead);

        log.info("Recycled lead: {}", command.getLeadId());
    }

    @Transactional
    public void delete(LeadCommand.DeleteLeadCommand command) {
        log.info("Deleting lead: {} for tenant: {}", command.getLeadId(), command.getTenantId());

        Lead lead = leadRepository.findByLeadIdAndTenantId(command.getLeadId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Lead", command.getLeadId()));

        if (lead.isClosed()) {
            throw new ValidationException("Cannot delete closed leads");
        }

        // Delete activities
        activityRepository.deleteByLeadIdAndTenantId(command.getLeadId(), command.getTenantId());

        // Delete lead
        leadRepository.deleteByLeadIdAndTenantId(command.getLeadId(), command.getTenantId());

        log.info("Deleted lead: {}", command.getLeadId());
    }

    private void publishEvents(Lead lead) {
        if (!lead.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(lead.getDomainEvents());
            lead.clearDomainEvents();
        }
    }
}
