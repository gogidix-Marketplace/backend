package com.gogidix.finance.consolidation.application.service;

import com.gogidix.finance.consolidation.domain.model.ConsolidationJob;
import com.gogidix.finance.consolidation.domain.repository.ConsolidationJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsolidationJobService {
    private final ConsolidationJobRepository repository;

    public ConsolidationJob create(ConsolidationJob job) { return repository.save(job); }
    public Optional<ConsolidationJob> getById(String id) { return repository.findById(id); }
    public List<ConsolidationJob> getByTenantId(String tenantId) { return repository.findByTenantId(tenantId); }
    public void delete(String id) { repository.deleteById(id); }
}
