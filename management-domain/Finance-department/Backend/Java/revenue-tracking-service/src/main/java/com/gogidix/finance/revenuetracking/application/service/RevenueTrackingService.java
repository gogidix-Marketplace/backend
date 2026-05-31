package com.gogidix.finance.revenuetracking.application.service;

import com.gogidix.finance.revenue.domain.model.Revenue;
import com.gogidix.finance.revenue.domain.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevenueTrackingService {
    private final RevenueRepository repository;

    public Revenue create(Revenue revenue) { return repository.save(revenue); }
    public Optional<Revenue> getById(String id) { return repository.findById(id); }
    public List<Revenue> getAll() { return repository.findByTenantId("default"); }
    public List<Revenue> getByTenantId(String tenantId) { return repository.findByTenantId(tenantId); }
    public void delete(String id) { repository.deleteById(id); }
}
