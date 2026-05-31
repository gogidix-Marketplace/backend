package com.gogidix.aiservices.leadgenerationaiservice.application.service;

import com.gogidix.aiservices.leadgenerationaiservice.application.dto.request.*;
import com.gogidix.aiservices.leadgenerationaiservice.application.dto.response.*;
import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.event.LeadActivity;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*;
import com.gogidix.aiservices.leadgenerationaiservice.domain.port.out.*;
import com.gogidix.aiservices.leadgenerationaiservice.domain.policy.LeadScoringPolicy;
import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadGenerationException;
import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadGenerationService {

    private final LeadRepository leadRepository;
    private final LeadScoringEnginePort scoringEngine;
    private final LeadEnrichmentPort enrichmentService;
    private final EventPublisherPort eventPublisher;
    private final LeadScoringPolicy scoringPolicy;

    public LeadResponse createLead(CreateLeadRequest request) {
        ContactInfo contact = ContactInfo.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .company(request.getCompany())
                .jobTitle(request.getJobTitle())
                .website(request.getWebsite())
                .linkedInUrl(request.getLinkedInUrl())
                .companySize(request.getCompanySize())
                .industry(request.getIndustry())
                .build();

        Lead lead = Lead.create(contact);

        if (request.getSource() != null) {
            LeadSource source = LeadSource.builder()
                    .name(request.getSource())
                    .channel(request.getChannel() != null ? request.getChannel() : LeadChannel.OTHER)
                    .build();
            lead.setSource(source);
        }

        // Enrich lead data
        try {
            lead = enrichmentService.enrichLead(lead);
        } catch (Exception e) {
            // Continue without enrichment if service fails
        }

        // Calculate initial score
        LeadScore score = scoringPolicy.calculateScore(lead);
        lead.setScore(score);

        Lead saved = leadRepository.save(lead);

        eventPublisher.publish("lead.created", Map.of(
                "leadId", saved.getLeadId().toString(),
                "email", saved.getContactInfo().getEmail()
        ));

        return toLeadResponse(saved);
    }

    public LeadResponse getLead(String leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new LeadNotFoundException(leadId));
        return toLeadResponse(lead);
    }

    public List<LeadResponse> getLeadsByStatus(LeadStatus status) {
        return leadRepository.findByStatus(status).stream()
                .map(this::toLeadResponse)
                .collect(Collectors.toList());
    }

    public List<LeadResponse> getLeadsByOwner(String ownerId) {
        return leadRepository.findByOwnerId(ownerId).stream()
                .map(this::toLeadResponse)
                .collect(Collectors.toList());
    }

    public List<LeadResponse> getTopLeads(int limit) {
        return leadRepository.findTopScores(limit).stream()
                .map(this::toLeadResponse)
                .collect(Collectors.toList());
    }

    public LeadResponse updateStatus(UpdateLeadStatusRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new LeadNotFoundException(request.getLeadId()));

        switch (request.getStatus()) {
            case CONTACTED -> lead.markAsContacted();
            case QUALIFIED -> lead.markAsQualified();
            case CONVERTED -> {
                if (lead.getScore() != null && lead.getScore().getScore() < 50) {
                    throw new LeadGenerationException("Lead must meet minimum score threshold");
                }
                lead.markAsConverted();
            }
            case LOST -> lead.markAsLost(request.getLossReason() != null ?
                    request.getLossReason() : LeadLossReason.OTHER);
        }

        Lead saved = leadRepository.save(lead);

        eventPublisher.publish("lead.status_updated", Map.of(
                "leadId", saved.getLeadId().toString(),
                "status", saved.getStatus().toString()
        ));

        return toLeadResponse(saved);
    }

    public LeadResponse assignLead(AssignLeadRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new LeadNotFoundException(request.getLeadId()));

        lead.assignTo(request.getOwnerId(), request.getOwnerName());

        Lead saved = leadRepository.save(lead);

        eventPublisher.publish("lead.assigned", Map.of(
                "leadId", saved.getLeadId().toString(),
                "ownerId", request.getOwnerId(),
                "ownerName", request.getOwnerName()
        ));

        return toLeadResponse(saved);
    }

    public LeadResponse addQualification(AddQualificationRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new LeadNotFoundException(request.getLeadId()));

        lead.addQualificationCriteria(request.getCriteria());

        // Recalculate score after adding qualification
        LeadScore newScore = scoringPolicy.calculateScore(lead);
        lead.setScore(newScore);

        Lead saved = leadRepository.save(lead);

        return toLeadResponse(saved);
    }

    public ActivityResponse addActivity(AddActivityRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new LeadNotFoundException(request.getLeadId()));

        LeadActivity activity = LeadActivity.builder()
                .leadId(UUID.fromString(request.getLeadId()))
                .type(request.getType())
                .description(request.getDescription())
                .timestamp(request.getTimestamp() != null ? request.getTimestamp() : Instant.now())
                .createdBy(request.getCreatedBy())
                .notes(request.getNotes())
                .build();

        lead.addActivity(activity);

        leadRepository.save(lead);

        return ActivityResponse.builder()
                .activityId(activity.getActivityId().toString())
                .leadId(lead.getLeadId().toString())
                .type(activity.getType())
                .description(activity.getDescription())
                .timestamp(activity.getTimestamp())
                .build();
    }

    public List<ActivityResponse> getActivities(String leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new LeadNotFoundException(leadId));

        return lead.getActivities().stream()
                .map(a -> ActivityResponse.builder()
                        .activityId(a.getActivityId().toString())
                        .leadId(leadId)
                        .type(a.getType())
                        .description(a.getDescription())
                        .timestamp(a.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

    public ScoreResponse recalculateScore(String leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new LeadNotFoundException(leadId));

        LeadScore score = scoringPolicy.calculateScore(lead);
        lead.setScore(score);

        leadRepository.save(lead);

        return ScoreResponse.builder()
                .leadId(leadId)
                .score(score.getScore())
                .tier(score.getClassifiedTier())
                .reason(score.getReason())
                .build();
    }

    public void deleteLead(String leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new LeadNotFoundException(leadId));

        leadRepository.delete(leadId);

        eventPublisher.publish("lead.deleted", Map.of(
                "leadId", leadId
        ));
    }

    public ConversionResponse convertLead(ConvertLeadRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new LeadNotFoundException(request.getLeadId()));

        if (lead.getStatus() != LeadStatus.QUALIFIED) {
            throw new LeadGenerationException("Lead must be qualified before conversion");
        }

        lead.markAsConverted();
        lead.setConversionValue(request.getValue(), request.getCurrency());

        Lead saved = leadRepository.save(lead);

        eventPublisher.publish("lead.converted", Map.of(
                "leadId", saved.getLeadId().toString(),
                "value", request.getValue(),
                "currency", request.getCurrency()
        ));

        return ConversionResponse.builder()
                .leadId(saved.getLeadId().toString())
                .status(saved.getStatus())
                .value(request.getValue())
                .currency(request.getCurrency())
                .convertedAt(Instant.now())
                .build();
    }

    private LeadResponse toLeadResponse(Lead lead) {
        return LeadResponse.builder()
                .leadId(lead.getLeadId().toString())
                .email(lead.getContactInfo().getEmail())
                .firstName(lead.getContactInfo().getFirstName())
                .lastName(lead.getContactInfo().getLastName())
                .phone(lead.getContactInfo().getPhone())
                .company(lead.getContactInfo().getCompany())
                .jobTitle(lead.getContactInfo().getJobTitle())
                .status(lead.getStatus())
                .score(lead.getScore() != null ? lead.getScore().getScore() : null)
                .tier(lead.getScore() != null ? lead.getScore().getClassifiedTier() : null)
                .ownerId(lead.getOwnerId())
                .ownerName(lead.getOwnerName())
                .source(lead.getSource() != null ? lead.getSource().getName() : null)
                .channel(lead.getSource() != null ? lead.getSource().getChannel() : null)
                .qualificationCriteria(new ArrayList<>(lead.getQualificationCriteria()))
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}
