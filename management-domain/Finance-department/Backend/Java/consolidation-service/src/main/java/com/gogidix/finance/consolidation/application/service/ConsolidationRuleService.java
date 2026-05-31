package com.gogidix.finance.consolidation.application.service;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
import com.gogidix.finance.consolidation.domain.repository.ConsolidationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsolidationRuleService {
    private final ConsolidationRuleRepository repository;

    public ConsolidationRule create(ConsolidationRule rule) { return repository.save(rule); }
    public Optional<ConsolidationRule> getById(String id) { return repository.findById(id); }
    public List<ConsolidationRule> getByTenantId(String tenantId) { return repository.findByTenantId(tenantId); }
    public void delete(String id) { repository.deleteById(id); }
}
