package com.gogidix.hr.performancereview.domain.repository;

import com.gogidix.hr.performancereview.domain.model.PerformanceReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends MongoRepository<PerformanceReview, String> {
    List<PerformanceReview> findByTenantId(String tenantId);
}
