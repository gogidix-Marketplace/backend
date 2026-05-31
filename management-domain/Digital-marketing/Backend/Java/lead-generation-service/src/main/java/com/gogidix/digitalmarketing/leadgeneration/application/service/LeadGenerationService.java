package com.gogidix.digitalmarketing.leadgeneration.application.service;

import com.gogidix.digitalmarketing.leadgeneration.domain.model.Lead;
import com.gogidix.digitalmarketing.leadgeneration.domain.repository.LeadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeadGenerationService {

    private final LeadRepository leadRepository;

    public LeadGenerationService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public Lead getById(String id) {
        return leadRepository.findById(id).orElse(null);
    }

    public List<Lead> getBySource(String tenantId, String source) {
        return leadRepository.findByTenantIdAndSource(tenantId, source);
    }

    public Lead create(Lead lead) {
        return leadRepository.save(lead);
    }
}
