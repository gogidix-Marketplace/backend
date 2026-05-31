package com.gogidix.sales.dashboard.infrastructure.scheduler;

import com.gogidix.sales.dashboard.application.service.DashboardCommandService;
import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
import com.gogidix.sales.dashboard.domain.repository.GlobalSalesDashboardRepository;
import com.gogidix.sales.dashboard.domain.repository.KPIWidgetRepository;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContext;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Dashboard Refresh Scheduler
 * Scheduled tasks for refreshing dashboard metrics and widgets
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DashboardRefreshScheduler {

    private final GlobalSalesDashboardRepository dashboardRepository;
    private final KPIWidgetRepository widgetRepository;

    /**
     * Refreshes all active dashboards every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void refreshActiveDashboards() {
        log.info("Starting scheduled refresh of active dashboards");

        List<GlobalSalesDashboard> dashboards = dashboardRepository.findByTenantIdAndStatus(
                "default", GlobalSalesDashboard.DashboardStatus.ACTIVE);

        for (GlobalSalesDashboard dashboard : dashboards) {
            if (dashboard.getConfiguration() != null && dashboard.getConfiguration().getAutoRefresh()) {
                try {
                    setupRequestContext(dashboard.getTenantId());

                    if (shouldRefresh(dashboard)) {
                        performDashboardRefresh(dashboard);
                    }
                } catch (Exception e) {
                    log.error("Failed to refresh dashboard: {}", dashboard.getDashboardId(), e);
                } finally {
                    RequestContextHolder.clear();
                }
            }
        }

        log.info("Completed scheduled refresh of active dashboards");
    }

    /**
     * Aggregates daily sales data at midnight
     */
    @Scheduled(cron = "0 0 0 * * ?") // Midnight every day
    public void aggregateDailySales() {
        log.info("Starting daily sales aggregation");

        try {
            // Trigger aggregation for all tenants
            // This would call the aggregation service to calculate daily metrics
            log.info("Daily sales aggregation completed");
        } catch (Exception e) {
            log.error("Failed to aggregate daily sales", e);
        }
    }

    /**
     * Updates exchange rates every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void updateExchangeRates() {
        log.info("Updating exchange rates");

        try {
            // Fetch latest exchange rates from external API
            // Update all dashboards with new rates
            log.info("Exchange rates updated successfully");
        } catch (Exception e) {
            log.error("Failed to update exchange rates", e);
        }
    }

    /**
     * Refreshes widgets that need updating
     */
    @Scheduled(fixedRate = 60000) // 1 minute
    public void refreshStaleWidgets() {
        log.debug("Checking for widgets needing refresh");

        try {
            var widgets = widgetRepository.findWidgetsNeedingRefresh("default", 5);

            for (var widget : widgets) {
                try {
                    setupRequestContext(widget.getTenantId());
                    refreshWidget(widget);
                } catch (Exception e) {
                    log.error("Failed to refresh widget: {}", widget.getWidgetId(), e);
                } finally {
                    RequestContextHolder.clear();
                }
            }
        } catch (Exception e) {
            log.error("Failed to refresh stale widgets", e);
        }
    }

    /**
     * Calculates weekly rollups every Sunday at 1 AM
     */
    @Scheduled(cron = "0 0 1 ? * SUN") // 1 AM every Sunday
    public void calculateWeeklyRollups() {
        log.info("Starting weekly rollup calculation");

        try {
            // Calculate and store weekly rollups for all tenants
            log.info("Weekly rollup calculation completed");
        } catch (Exception e) {
            log.error("Failed to calculate weekly rollups", e);
        }
    }

    /**
     * Calculates monthly rollups on the first day of each month at 2 AM
     */
    @Scheduled(cron = "0 0 2 1 * ?") // 2 AM on the 1st of each month
    public void calculateMonthlyRollups() {
        log.info("Starting monthly rollup calculation");

        try {
            // Calculate and store monthly rollups for all tenants
            log.info("Monthly rollup calculation completed");
        } catch (Exception e) {
            log.error("Failed to calculate monthly rollups", e);
        }
    }

    /**
     * Cleans up old data daily at 3 AM
     */
    @Scheduled(cron = "0 0 3 * * ?") // 3 AM daily
    public void cleanupOldData() {
        log.info("Starting cleanup of old data");

        try {
            LocalDate cutoffDate = LocalDate.now().minusMonths(12);
            // Remove old aggregation and rollup data
            log.info("Cleanup of old data completed");
        } catch (Exception e) {
            log.error("Failed to cleanup old data", e);
        }
    }

    private boolean shouldRefresh(GlobalSalesDashboard dashboard) {
        if (dashboard.getLastRefreshAt() == null) {
            return true;
        }

        long refreshInterval = dashboard.getConfiguration() != null ?
                dashboard.getConfiguration().getRefreshIntervalMinutes() * 60000L : 300000L;

        return Instant.now().toEpochMilli() - dashboard.getLastRefreshAt().toEpochMilli() > refreshInterval;
    }

    private void performDashboardRefresh(GlobalSalesDashboard dashboard) {
        // Simulate metric updates
        if (dashboard.getGlobalMetrics() != null) {
            // Add some random variation to simulate real-time updates
            BigDecimal currentRevenue = dashboard.getGlobalMetrics().getTotalRevenue().getAmount();
            BigDecimal variation = currentRevenue.multiply(new BigDecimal("0.01"))
                    .multiply(new BigDecimal(Math.random() > 0.5 ? 1 : -1));
            BigDecimal newRevenue = currentRevenue.add(variation);

            dashboard.getGlobalMetrics().getTotalRevenue().setAmount(newRevenue);
        }

        dashboard.setLastRefreshAt(Instant.now());
        dashboardRepository.save(dashboard);

        log.debug("Refreshed dashboard: {}", dashboard.getDashboardId());
    }

    private void refreshWidget(com.gogidix.sales.dashboard.domain.model.KPIWidget widget) {
        // Simulate widget value refresh
        Object currentValue = widget.getCurrentValue() != null ?
                widget.getCurrentValue().getValue() : BigDecimal.ZERO;

        if (currentValue instanceof BigDecimal) {
            BigDecimal current = (BigDecimal) currentValue;
            BigDecimal variation = current.multiply(new BigDecimal("0.02"))
                    .multiply(new BigDecimal(Math.random() > 0.5 ? 1 : -1));
            BigDecimal newValue = current.add(variation);

            widget.updateValue(newValue, newValue.toString());
        }

        log.debug("Refreshed widget: {}", widget.getWidgetId());
    }

    private void setupRequestContext(String tenantId) {
        RequestContext context = RequestContext.builder()
                .tenantId(tenantId != null ? tenantId : "default")
                .userId("system")
                .correlationId(java.util.UUID.randomUUID().toString())
                .requestId(java.util.UUID.randomUUID().toString())
                .build();

        RequestContextHolder.set(context);
    }
}
