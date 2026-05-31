package com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Insight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsightRepository extends MongoRepository<Insight, String> {
    List<Insight> findByTenantId(String tenantId);
}
