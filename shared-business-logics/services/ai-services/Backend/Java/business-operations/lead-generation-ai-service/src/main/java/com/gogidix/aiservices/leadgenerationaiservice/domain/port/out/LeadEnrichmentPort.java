package com.gogidix.aiservices.leadgenerationaiservice.domain.port.out;

import com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate.Lead;

import java.util.List;

public interface LeadEnrichmentPort {
    Lead enrichLead(Lead lead);
    EnrichmentData fetchEnrichmentData(String email, String domain);

    record EnrichmentData(String company, String industry, Integer employeeCount,
                         Long revenue, String website, String linkedInUrl,
                         String phone, List<String> technologies) {}
}
