package com.gogidix.digitalmarketing.analytics.domain.repository;

import com.gogidix.digitalmarketing.analytics.domain.model.CampaignAnalytics;
import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignAnalyticsRepository extends BaseRepository<CampaignAnalytics> {}
