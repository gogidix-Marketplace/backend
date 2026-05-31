package com.gogidix.digitalmarketing.analytics.application.service;

import com.gogidix.digitalmarketing.analytics.domain.model.CampaignAnalytics;
import com.gogidix.digitalmarketing.analytics.domain.repository.CampaignAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final CampaignAnalyticsRepository repository;

    public CampaignAnalytics create(CampaignAnalytics analytics) { return repository.save(analytics); }
    public CampaignAnalytics getById(String id) { return repository.findById(id).orElse(null); }
    public List<CampaignAnalytics> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}
