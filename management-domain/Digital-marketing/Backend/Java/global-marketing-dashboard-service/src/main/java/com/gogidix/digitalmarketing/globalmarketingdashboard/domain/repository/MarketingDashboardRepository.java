package com.gogidix.digitalmarketing.globalmarketingdashboard.domain.repository;

import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.model.MarketingDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingDashboardRepository extends MongoRepository<MarketingDashboard, String> {}
