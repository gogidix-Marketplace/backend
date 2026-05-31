package com.gogidix.sales.leadmanagement.infrastructure.messaging.kafka;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.port.out.LeadDuplicateDetector;
import com.gogidix.sales.leadmanagement.domain.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Lead Duplicate Detector Implementation
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaLeadDuplicateDetector implements LeadDuplicateDetector {

    private final LeadRepository leadRepository;

    @Value("${lead-management-service.duplicate-detection.match-threshold:85}")
    private double matchThreshold;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public List<Lead> findDuplicates(String tenantId, String email, String phone,
                                      String firstName, String lastName) {
        return findPotentialDuplicates(tenantId, email, phone, firstName, lastName, null);
    }

    @Override
    public List<Lead> findPotentialDuplicates(String tenantId, String email, String phone,
                                              String firstName, String lastName, String company) {
        return leadRepository.findDuplicateLeads(tenantId, email, phone, firstName, lastName);
    }

    @Override
    public double calculateMatchScore(Lead lead1, Lead lead2) {
        double score = 0.0;
        int factors = 0;

        // Email match (highest weight)
        if (lead1.getEmail() != null && lead1.getEmail().equalsIgnoreCase(lead2.getEmail())) {
            score += 40;
        }
        factors++;

        // Phone match
        if (lead1.getPhone() != null && lead1.getPhone().equals(lead2.getPhone())) {
            score += 30;
        }
        factors++;

        // Name match
        if (lead1.getFirstName() != null && lead1.getFirstName().equalsIgnoreCase(lead2.getFirstName())) {
            score += 10;
        }
        if (lead1.getLastName() != null && lead1.getLastName().equalsIgnoreCase(lead2.getLastName())) {
            score += 10;
        }
        factors++;

        // Company match
        if (lead1.getCompany() != null && lead1.getCompany().equalsIgnoreCase(lead2.getCompany())) {
            score += 10;
        }
        factors++;

        return score;
    }

    @Override
    public Optional<DuplicateResult> checkForDuplicates(String tenantId, Lead lead) {
        List<Lead> potentialDuplicates = findPotentialDuplicates(
                tenantId,
                lead.getEmail(),
                lead.getPhone(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getCompany()
        );

        for (Lead potential : potentialDuplicates) {
            if (!potential.getLeadId().equals(lead.getLeadId())) {
                double matchScore = calculateMatchScore(lead, potential);
                if (matchScore >= matchThreshold) {
                    log.info("Duplicate lead found: {} matches {} with {}% score",
                            lead.getLeadId(), potential.getLeadId(), matchScore);
                    return Optional.of(new DuplicateResult(
                            potential,
                            matchScore,
                            "High match score based on email, phone, name, or company"
                    ));
                }
            }
        }

        return Optional.empty();
    }
}
