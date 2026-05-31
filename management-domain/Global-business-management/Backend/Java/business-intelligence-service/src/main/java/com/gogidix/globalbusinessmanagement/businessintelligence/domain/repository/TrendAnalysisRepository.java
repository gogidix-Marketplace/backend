package com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.TrendAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrendAnalysisRepository extends MongoRepository<TrendAnalysis, String> {
    List<TrendAnalysis> findByTenantId(String tenantId);
    List<TrendAnalysis> findByMetric(String metric);
}
