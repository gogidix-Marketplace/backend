package com.gogidix.platform.metering.domain.repository;

import com.gogidix.platform.metering.domain.model.MetricDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for MetricDefinition entity.
 */
@Repository
public interface MetricDefinitionRepository extends JpaRepository<MetricDefinition, String> {

    /**
     * Find metric definition by name
     */
    Optional<MetricDefinition> findByMetricName(String metricName);

    /**
     * Find all active metrics
     */
    List<MetricDefinition> findByIsActive(boolean isActive);

    /**
     * Find metrics by category
     */
    List<MetricDefinition> findByMetricCategory(String metricCategory);

    /**
     * Find billable metrics
     */
    List<MetricDefinition> findByBillable(boolean billable);

    /**
     * Check if metric exists
     */
    boolean existsByMetricName(String metricName);
}
