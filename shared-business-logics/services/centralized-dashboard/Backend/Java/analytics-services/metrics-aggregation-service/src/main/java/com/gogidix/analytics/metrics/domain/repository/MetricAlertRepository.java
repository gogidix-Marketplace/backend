package com.gogidix.analytics.metrics.domain.repository;

import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for MetricAlert aggregate.
 */
@Repository
public interface MetricAlertRepository extends JpaRepository<MetricAlert, String> {

    List<MetricAlert> findByTenantIdAndEnabledTrue(String tenantId);

    List<MetricAlert> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<MetricAlert> findByTenantIdAndMetricName(String tenantId, String metricName);

    List<MetricAlert> findByTenantIdAndStatus(String tenantId, MetricAlert.AlertStatus status);
}
