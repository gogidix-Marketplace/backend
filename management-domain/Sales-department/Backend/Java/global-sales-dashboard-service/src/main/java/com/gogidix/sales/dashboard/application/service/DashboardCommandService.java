package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
import com.gogidix.sales.dashboard.domain.event.DashboardRefreshedEvent;
import com.gogidix.sales.dashboard.domain.event.MetricUpdatedEvent;
import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
import com.gogidix.sales.dashboard.domain.port.in.DashboardCommand;
import com.gogidix.sales.dashboard.domain.port.out.EventPublisher;
import com.gogidix.sales.dashboard.domain.repository.GlobalSalesDashboardRepository;
import com.gogidix.sales.dashboard.domain.repository.KPIWidgetRepository;
import com.gogidix.sales.dashboard.domain.valueobject.Money;
import com.gogidix.sales.dashboard.shared.exception.ConflictException;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.exception.ValidationException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Dashboard Command Service
 * Handles all write operations for dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardCommandService {

    private final GlobalSalesDashboardRepository dashboardRepository;
    private final KPIWidgetRepository widgetRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public GlobalSalesDashboard create(DashboardCommand.CreateDashboardCommand command) {
        log.info("Creating dashboard for tenant: {}, name: {}", command.getTenantId(), command.getName());

        GlobalSalesDashboard dashboard = GlobalSalesDashboard.create(
                command.getTenantId(),
                command.getName(),
                command.getDescription(),
                command.getType(),
                command.getBaseCurrency()
        );

        if (command.getEnabledRegions() != null && !command.getEnabledRegions().isEmpty()) {
            dashboard.getConfiguration().setEnabledRegions(command.getEnabledRegions());
        }

        if (command.getRefreshIntervalMinutes() != null) {
            dashboard.getConfiguration().setRefreshIntervalMinutes(command.getRefreshIntervalMinutes());
        }

        GlobalSalesDashboard savedDashboard = dashboardRepository.save(dashboard);
        publishDashboardEvents(savedDashboard);

        log.info("Created dashboard: {} for tenant: {}", savedDashboard.getDashboardId(), command.getTenantId());
        return savedDashboard;
    }

    @Transactional
    public GlobalSalesDashboard update(DashboardCommand.UpdateDashboardCommand command) {
        log.info("Updating dashboard: {} for tenant: {}", command.getDashboardId(), command.getTenantId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        if (command.getName() != null) {
            dashboard.setName(command.getName());
        }
        if (command.getDescription() != null) {
            dashboard.setDescription(command.getDescription());
        }
        if (command.getStatus() != null) {
            dashboard.setStatus(command.getStatus());
        }

        GlobalSalesDashboard savedDashboard = dashboardRepository.save(dashboard);
        publishDashboardEvents(savedDashboard);

        return savedDashboard;
    }

    @Transactional
    public void updateGlobalMetrics(DashboardCommand.UpdateGlobalMetricsCommand command) {
        log.info("Updating global metrics for dashboard: {}", command.getDashboardId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        GlobalSalesDashboard.GlobalMetrics metrics = GlobalSalesDashboard.GlobalMetrics.builder()
                .totalRevenue(toMoney(command.getTotalRevenue(), dashboard.getBaseCurrency()))
                .targetRevenue(toMoney(command.getTargetRevenue(), dashboard.getBaseCurrency()))
                .totalDeals(command.getTotalDeals() != null ? command.getTotalDeals() : 0)
                .wonDeals(command.getWonDeals() != null ? command.getWonDeals() : 0)
                .averageDealSize(toMoney(command.getAverageDealSize(), dashboard.getBaseCurrency()))
                .weightedPipeline(toMoney(command.getWeightedPipeline(), dashboard.getBaseCurrency()))
                .opportunitiesInPipeline(command.getOpportunitiesInPipeline() != null ?
                        command.getOpportunitiesInPipeline() : 0)
                .yearOverYearGrowth(command.getYearOverYearGrowth() != null ?
                        command.getYearOverYearGrowth() : BigDecimal.ZERO)
                .monthOverMonthGrowth(command.getMonthOverMonthGrowth() != null ?
                        command.getMonthOverMonthGrowth() : BigDecimal.ZERO)
                .build();

        dashboard.updateGlobalMetrics(metrics);
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Updated global metrics for dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void updateRegionalMetric(DashboardCommand.UpdateRegionalMetricCommand command) {
        log.info("Updating regional metric for dashboard: {}, region: {}",
                command.getDashboardId(), command.getRegionCode());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        GlobalSalesDashboard.RegionalMetric regionalMetric = GlobalSalesDashboard.RegionalMetric.builder()
                .regionCode(command.getRegionCode())
                .regionName(command.getRegionName() != null ? command.getRegionName() : command.getRegionCode())
                .revenue(toMoney(command.getRevenue(), command.getCurrency()))
                .target(toMoney(command.getTarget(), command.getCurrency()))
                .deals(command.getDeals() != null ? command.getDeals() : 0)
                .growthRate(command.getGrowthRate() != null ? command.getGrowthRate() : BigDecimal.ZERO)
                .currency(command.getCurrency())
                .build();

        dashboard.updateRegionalMetric(regionalMetric);
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Updated regional metric for dashboard: {}, region: {}",
                command.getDashboardId(), command.getRegionCode());
    }

    @Transactional
    public void addWidget(DashboardCommand.AddWidgetCommand command) {
        log.info("Adding widget to dashboard: {}", command.getDashboardId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        GlobalSalesDashboard.KPIWidget widget = GlobalSalesDashboard.KPIWidget.builder()
                .title(command.getTitle())
                .type(command.getWidgetType())
                .widgetType(command.getWidgetType())
                .category(command.getCategory())
                .description(command.getDescription())
                .value(command.getInitialValue())
                .position(0)
                .isVisible(true)
                .build();

        dashboard.addWidget(widget);
        dashboardRepository.save(dashboard);

        log.info("Added widget to dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void refreshDashboard(DashboardCommand.RefreshDashboardCommand command) {
        log.info("Refreshing dashboard: {} for tenant: {}", command.getDashboardId(), command.getTenantId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        Instant startTime = Instant.now();
        dashboard.refresh(command.getUserId());

        // Refresh all associated widgets
        if (dashboard.getWidgets() != null) {
            dashboard.getWidgets().forEach(widget -> {
                widget.setLastUpdated(Instant.now());
            });
        }

        GlobalSalesDashboard savedDashboard = dashboardRepository.save(dashboard);

        // Publish refresh event
        DashboardRefreshedEvent refreshEvent = DashboardRefreshedEvent.completed(
                dashboard.getDashboardId(),
                command.getTenantId(),
                startTime,
                command.getUserId(),
                dashboard.getRegionalMetrics() != null ? dashboard.getRegionalMetrics().size() : 0,
                dashboard.getWidgets() != null ? dashboard.getWidgets().size() : 0
        );
        eventPublisher.publishDashboardRefreshEvent(refreshEvent);

        publishDashboardEvents(savedDashboard);

        log.info("Refreshed dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void publishDashboard(DashboardCommand.PublishDashboardCommand command) {
        log.info("Publishing dashboard: {}", command.getDashboardId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        dashboard.publish();
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Published dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void archiveDashboard(DashboardCommand.ArchiveDashboardCommand command) {
        log.info("Archiving dashboard: {}", command.getDashboardId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        dashboard.archive();
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Archived dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void addTrendData(DashboardCommand.AddTrendDataCommand command) {
        log.info("Adding trend data to dashboard: {}", command.getDashboardId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        GlobalSalesDashboard.TimeSeriesData dataPoint = GlobalSalesDashboard.TimeSeriesData.builder()
                .period(command.getPeriod())
                .date(command.getDate())
                .revenue(toMoney(command.getRevenue(), dashboard.getBaseCurrency()))
                .deals(command.getDeals() != null ? command.getDeals() : 0)
                .conversionRate(command.getConversionRate() != null ?
                        command.getConversionRate() : BigDecimal.ZERO)
                .averageDealSize(toMoney(null, dashboard.getBaseCurrency()))
                .region(command.getRegion())
                .build();

        dashboard.addTrendData(dataPoint);
        dashboardRepository.save(dashboard);

        log.info("Added trend data to dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void updateExchangeRates(DashboardCommand.UpdateExchangeRatesCommand command) {
        log.info("Updating exchange rates for dashboard: {}", command.getDashboardId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        dashboard.updateExchangeRates(command.getExchangeRates());
        dashboardRepository.save(dashboard);

        log.info("Updated exchange rates for dashboard: {}", command.getDashboardId());
    }

    @Transactional
    public void delete(DashboardCommand.DeleteDashboardCommand command) {
        log.info("Deleting dashboard: {} for tenant: {}", command.getDashboardId(), command.getTenantId());

        GlobalSalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Dashboard", command.getDashboardId()));

        dashboardRepository.deleteByDashboardIdAndTenantId(command.getDashboardId(), command.getTenantId());

        log.info("Deleted dashboard: {}", command.getDashboardId());
    }

    private void publishDashboardEvents(GlobalSalesDashboard dashboard) {
        if (!dashboard.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : dashboard.getDomainEvents()) {
                if (event instanceof MetricUpdatedEvent) {
                    eventPublisher.publishMetricEvent((MetricUpdatedEvent) event);
                }
            }
            dashboard.clearDomainEvents();
        }
    }

    private GlobalSalesDashboard.Money toMoney(BigDecimal amount, String currency) {
        if (amount == null) {
            return GlobalSalesDashboard.Money.builder()
                    .amount(BigDecimal.ZERO)
                    .currency(currency)
                    .build();
        }
        return GlobalSalesDashboard.Money.builder()
                .amount(amount)
                .currency(currency)
                .build();
    }
}
