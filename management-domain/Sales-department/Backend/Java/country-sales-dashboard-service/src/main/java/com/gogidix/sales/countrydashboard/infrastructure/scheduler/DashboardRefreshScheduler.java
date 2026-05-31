package com.gogidix.sales.countrydashboard.infrastructure.scheduler;

import com.gogidix.sales.countrydashboard.application.service.CountryDashboardCommandService;
import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
import com.gogidix.sales.countrydashboard.domain.repository.CountrySalesDashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dashboard Refresh Scheduler
 * Scheduled tasks for refreshing dashboard data
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DashboardRefreshScheduler {

    private final CountrySalesDashboardRepository dashboardRepository;
    private final CountryDashboardCommandService commandService;

    /**
     * Refresh active dashboards every 5 minutes
     */
    @Scheduled(fixedRate = 300000, initialDelay = 60000)
    public void refreshActiveDashboards() {
        log.debug("Starting scheduled refresh of active dashboards");

        List<String> tenants = List.of("default"); // Could be enhanced to fetch all tenants

        for (String tenantId : tenants) {
            try {
                List<com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard> activeDashboards =
                        dashboardRepository.findActiveDashboardsForTenant(tenantId);

                for (var dashboard : activeDashboards) {
                    try {
                        CountryDashboardCommand.RefreshDashboardCommand command =
                                new CountryDashboardCommand.RefreshDashboardCommand(
                                        tenantId,
                                        dashboard.getDashboardId(),
                                        "SYSTEM",
                                        false,
                                        true,
                                        true,
                                        true
                                );

                        commandService.refreshDashboard(command);

                        log.debug("Refreshed dashboard: {} for tenant: {}",
                                dashboard.getDashboardId(), tenantId);

                    } catch (Exception e) {
                        log.error("Failed to refresh dashboard: {} for tenant: {} - {}",
                                dashboard.getDashboardId(), tenantId, e.getMessage());
                    }
                }

                log.info("Refreshed {} active dashboards for tenant: {}",
                        activeDashboards.size(), tenantId);

            } catch (Exception e) {
                log.error("Failed to refresh dashboards for tenant: {} - {}",
                        tenantId, e.getMessage());
            }
        }
    }

    /**
     * Calculate comparisons hourly
     */
    @Scheduled(fixedRate = 3600000, initialDelay = 300000)
    public void calculateComparisons() {
        log.debug("Starting scheduled comparison calculations");

        List<String> tenants = List.of("default"); // Could be enhanced to fetch all tenants

        for (String tenantId : tenants) {
            try {
                List<com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard> dashboards =
                        dashboardRepository.findByTenantId(tenantId);

                for (var dashboard : dashboards) {
                    if (dashboard.getStatus() !=
                        com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard.DashboardStatus.ARCHIVED) {

                        try {
                            CountryDashboardCommand.RefreshDashboardCommand command =
                                    new CountryDashboardCommand.RefreshDashboardCommand(
                                            tenantId,
                                            dashboard.getDashboardId(),
                                            "SYSTEM",
                                            false,
                                            true,
                                            true,
                                            true
                                    );

                            commandService.refreshDashboard(command);

                            log.debug("Calculated comparisons for dashboard: {}",
                                    dashboard.getDashboardId());

                        } catch (Exception e) {
                            log.error("Failed to calculate comparisons for dashboard: {} - {}",
                                    dashboard.getDashboardId(), e.getMessage());
                        }
                    }
                }

                log.info("Calculated comparisons for {} dashboards for tenant: {}",
                        dashboards.size(), tenantId);

            } catch (Exception e) {
                log.error("Failed to calculate comparisons for tenant: {} - {}",
                        tenantId, e.getMessage());
            }
        }
    }
}
