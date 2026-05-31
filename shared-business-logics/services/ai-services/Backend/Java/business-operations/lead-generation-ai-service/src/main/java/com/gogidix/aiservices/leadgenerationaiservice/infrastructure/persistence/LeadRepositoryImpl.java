package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.persistence;

import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;
import com.gogidix.aiservices.leadgenerationaiservice.domain.event.LeadActivity;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*;
import com.gogidix.aiservices.leadgenerationaiservice.domain.port.out.LeadRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LeadRepositoryImpl implements LeadRepository {

    private final InMemoryLeadDataSource dataSource;

    public LeadRepositoryImpl(InMemoryLeadDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Lead save(Lead lead) {
        LeadEntity entity = toEntity(lead);
        LeadEntity saved = dataSource.save(entity);
        return toLead(saved, lead);
    }

    @Override
    public Optional<Lead> findById(String id) {
        return dataSource.findById(id).map(this::toLead);
    }

    @Override
    public Optional<Lead> findByEmail(String email) {
        return dataSource.findByEmail(email).map(this::toLead);
    }

    @Override
    public List<Lead> findAll() {
        return dataSource.findAll().stream()
                .map(this::toLead)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findByStatus(LeadStatus status) {
        return dataSource.findByStatus(status).stream()
                .map(this::toLead)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findByOwnerId(String ownerId) {
        return dataSource.findByOwnerId(ownerId).stream()
                .map(this::toLead)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findByCreatedAtAfter(Instant timestamp) {
        return dataSource.findByCreatedAtAfter(timestamp).stream()
                .map(this::toLead)
                .collect(Collectors.toList());
    }

    @Override
    public List<Lead> findTopScores(int limit) {
        return dataSource.findTopScores(limit).stream()
                .map(this::toLead)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        dataSource.deleteById(id);
    }

    @Override
    public List<Lead> saveAll(List<Lead> leads) {
        return dataSource.saveAll(leads.stream()
                .map(this::toEntity)
                .collect(Collectors.toList())).stream()
                .map(this::toLead)
                .collect(Collectors.toList());
    }

    private LeadEntity toEntity(Lead lead) {
        LeadEntity entity = new LeadEntity();
        entity.setLeadId(lead.getLeadId().toString());
        entity.setEmail(lead.getContactInfo().getEmail());
        entity.setFirstName(lead.getContactInfo().getFirstName());
        entity.setLastName(lead.getContactInfo().getLastName());
        entity.setPhone(lead.getContactInfo().getPhone());
        entity.setCompany(lead.getContactInfo().getCompany());
        entity.setJobTitle(lead.getContactInfo().getJobTitle());
        entity.setWebsite(lead.getContactInfo().getWebsite());
        entity.setLinkedInUrl(lead.getContactInfo().getLinkedInUrl());
        entity.setCompanySize(lead.getContactInfo().getCompanySize());
        entity.setIndustry(lead.getContactInfo().getIndustry());
        entity.setStatus(lead.getStatus());
        entity.setScore(lead.getScore() != null ? lead.getScore().getScore() : null);
        entity.setOwnerId(lead.getOwnerId());
        entity.setOwnerName(lead.getOwnerName());
        entity.setCreatedAt(lead.getCreatedAt());
        entity.setUpdatedAt(lead.getUpdatedAt());
        entity.setAssignedAt(lead.getAssignedAt());

        if (lead.getSource() != null) {
            entity.setSourceName(lead.getSource().getName());
            entity.setChannel(lead.getSource().getChannel());
        }

        Set<String> criteria = lead.getQualificationCriteria().stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());
        entity.setQualificationCriteria(criteria);

        return entity;
    }

    private Lead toLead(LeadEntity entity) {
        ContactInfo contact = ContactInfo.builder()
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phone(entity.getPhone())
                .company(entity.getCompany())
                .jobTitle(entity.getJobTitle())
                .website(entity.getWebsite())
                .linkedInUrl(entity.getLinkedInUrl())
                .companySize(entity.getCompanySize())
                .industry(entity.getIndustry())
                .build();

        Lead lead = Lead.reconstruct(
                java.util.UUID.fromString(entity.getLeadId()),
                contact,
                entity.getStatus(),
                entity.getCreatedAt());
        // createdAt is final and set during creation, cannot be modified
        // lead.setCreatedAt(entity.getCreatedAt());
        // updatedAt is updated through touch() method, not directly settable
        // lead.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getSourceName() != null && entity.getChannel() != null) {
            LeadSource source = LeadSource.builder()
                    .name(entity.getSourceName())
                    .channel(entity.getChannel())
                    .build();
            lead.setSource(source);
        }

        if (entity.getScore() != null) {
            LeadScore score = LeadScore.builder()
                    .leadId(entity.getLeadId())
                    .score(entity.getScore())
                    .build();
            lead.setScore(score);
        }

        if (entity.getOwnerId() != null) {
            lead.assignTo(entity.getOwnerId(), entity.getOwnerName());
        }

        Set<QualificationCriteria> criteria = entity.getQualificationCriteria().stream()
                .map(QualificationCriteria::valueOf)
                .collect(Collectors.toSet());
        criteria.forEach(lead::addQualificationCriteria);

        return lead;
    }

    private Lead toLead(LeadEntity entity, Lead original) {
        Lead lead = toLead(entity);

        // Preserve activities from original
        for (LeadActivity activity : original.getActivities()) {
            try {
                lead.addActivity(activity);
            } catch (Exception ignored) {
            }
        }

        return lead;
    }
}
