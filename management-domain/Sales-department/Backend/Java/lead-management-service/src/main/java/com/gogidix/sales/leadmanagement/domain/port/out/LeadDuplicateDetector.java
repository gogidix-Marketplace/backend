package com.gogidix.sales.leadmanagement.domain.port.out;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
import java.util.List;
import java.util.Optional;

public interface LeadDuplicateDetector {
    boolean isEnabled();
    List<Lead> findDuplicates(String tenantId, String email, String phone, String firstName, String lastName);
    List<Lead> findPotentialDuplicates(String tenantId, String email, String phone, String firstName, String lastName, String company);
    double calculateMatchScore(Lead lead1, Lead lead2);
    Optional<DuplicateResult> checkForDuplicates(String tenantId, Lead lead);

    record DuplicateResult(Lead duplicateLead, double matchScore, String reason) {}
}
