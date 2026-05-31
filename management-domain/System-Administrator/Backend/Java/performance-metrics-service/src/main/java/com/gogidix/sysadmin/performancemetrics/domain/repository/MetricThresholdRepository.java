package com.gogidix.sysadmin.performancemetrics.domain.repository;

import com.gogidix.sysadmin.performancemetrics.domain.model.MetricThreshold;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricThresholdRepository extends MongoRepository<MetricThreshold, String> {

    List<MetricThreshold> findByTenantId(String tenantId);

    List<MetricThreshold> findByTenantIdAndResourceId(String tenantId, String resourceId);

    List<MetricThreshold> findByTenantIdAndEnabledTrue(String tenantId);

    List<MetricThreshold> findByTenantIdAndMetricNameAndEnabledTrue(String tenantId, String metricName);

    List<MetricThreshold> findByTenantIdAndResourceIdAndMetricNameAndEnabledTrue(
            String tenantId, String resourceId, String metricName);

    boolean existsByTenantIdAndResourceIdAndMetricName(String tenantId, String resourceId, String metricName);

    void deleteByTenantId(String tenantId);
}
