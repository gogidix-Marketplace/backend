package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.persistence;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.CompanySize;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadChannel;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class LeadEntity {
    private String leadId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String company;
    private String jobTitle;
    private String website;
    private String linkedInUrl;
    private CompanySize companySize;
    private String industry;
    private LeadStatus status;
    private Double score;
    private String sourceName;
    private LeadChannel channel;
    private Set<String> qualificationCriteria = new HashSet<>();
    private String ownerId;
    private String ownerName;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant assignedAt;

    public LeadEntity() {
        this.leadId = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public LeadEntity(String leadId) {
        this.leadId = leadId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
