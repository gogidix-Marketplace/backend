package com.gogidix.digitalmarketing.globalmarketingdashboard.application.service;

import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.model.MarketingDashboard;
import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.repository.MarketingDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketingDashboardService {
    private final MarketingDashboardRepository repository;

    public MarketingDashboard create(MarketingDashboard dashboard) { return repository.save(dashboard); }
    public MarketingDashboard getById(String id) { return repository.findById(id).orElse(null); }
    public List<MarketingDashboard> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}
