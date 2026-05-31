package com.gogidix.dashboard.gateway.chart.domain.repository;

import com.gogidix.dashboard.gateway.chart.domain.model.ChartConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ChartConfiguration entities.
 */
@Repository
public interface ChartConfigurationRepository extends JpaRepository<ChartConfiguration, Long> {

    /**
     * Find chart by chart ID and tenant ID
     */
    Optional<ChartConfiguration> findByChartIdAndTenantId(String chartId, String tenantId);

    /**
     * Find all charts by tenant ID
     */
    List<ChartConfiguration> findByTenantIdAndEnabledTrueOrderByCreatedAtDesc(String tenantId);

    /**
     * Find charts by type and tenant ID
     */
    List<ChartConfiguration> findByChartTypeAndTenantIdAndEnabledTrue(String chartType, String tenantId);
}
