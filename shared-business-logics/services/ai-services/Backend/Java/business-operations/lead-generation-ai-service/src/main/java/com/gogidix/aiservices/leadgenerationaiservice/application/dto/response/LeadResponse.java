package com.gogidix.aiservices.leadgenerationaiservice.application.dto.response;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadChannel;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadTier;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.QualificationCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponse {
    private String leadId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String company;
    private String jobTitle;
    private LeadStatus status;
    private Double score;
    private LeadTier tier;
    private String ownerId;
    private String ownerName;
    private String source;
    private LeadChannel channel;
    private List<QualificationCriteria> qualificationCriteria;
    private Instant createdAt;
    private Instant updatedAt;
}
