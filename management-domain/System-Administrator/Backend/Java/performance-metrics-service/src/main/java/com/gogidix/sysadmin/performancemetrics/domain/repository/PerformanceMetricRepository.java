package com.gogidix.sysadmin.performancemetrics.domain.repository;
import com.gogidix.sysadmin.performancemetrics.domain.model.PerformanceMetric;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PerformanceMetricRepository extends MongoRepository<PerformanceMetric, String> {}
