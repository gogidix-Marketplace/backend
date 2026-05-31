package com.gogidix.sales.countrydashboard.application.service;

import com.gogidix.sales.countrydashboard.domain.event.*;
import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
import com.gogidix.sales.countrydashboard.domain.port.out.EventPublisher;
import com.gogidix.sales.countrydashboard.domain.repository.CountrySalesDashboardRepository;
import com.gogidix.sales.countrydashboard.domain.valueobject.Money;
import com.gogidix.sales.countrydashboard.shared.exception.ConflictException;
import com.gogidix.sales.countrydashboard.shared.exception.NotFoundException;
import com.gogidix.sales.countrydashboard.shared.exception.ValidationException;
import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Country Dashboard Command Service
 * Handles all write operations for country sales dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CountryDashboardCommandService {

    private final CountrySalesDashboardRepository dashboardRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", allEntries = true),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public CountrySalesDashboard create(CountryDashboardCommand.CreateDashboardCommand command) {
        log.info("Creating country dashboard for tenant: {}, country: {}",
                command.getTenantId(), command.getCountryCode());

        // Check if dashboard already exists for this country
        if (dashboardRepository.existsByCountryCodeAndTenantId(
                command.getCountryCode(), command.getTenantId())) {
            throw new ConflictException("CountryDashboard", command.getCountryCode());
        }

        CountrySalesDashboard dashboard = CountrySalesDashboard.create(
                command.getTenantId(),
                command.getCountryCode(),
                command.getCountryName(),
                command.getType(),
                command.getLocalCurrency() != null ? command.getLocalCurrency() : "USD"
        );

        // Apply configuration overrides
        if (command.getEnabledTerritories() != null && !command.getEnabledTerritories().isEmpty()) {
            dashboard.getConfiguration().setEnabledTerritories(command.getEnabledTerritories());
        }

        if (command.getRefreshIntervalMinutes() != null) {
            dashboard.getConfiguration().setRefreshIntervalMinutes(command.getRefreshIntervalMinutes());
        }

        CountrySalesDashboard savedDashboard = dashboardRepository.save(dashboard);
        publishDashboardEvents(savedDashboard);

        log.info("Created country dashboard: {} for tenant: {}, country: {}",
                savedDashboard.getDashboardId(), command.getTenantId(), command.getCountryCode());

        return savedDashboard;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public CountrySalesDashboard update(CountryDashboardCommand.UpdateDashboardCommand command) {
        log.info("Updating country dashboard: {} for tenant: {}",
                command.getDashboardId(), command.getTenantId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        if (command.getName() != null) {
            dashboard.setCountryName(command.getName());
        }
        if (command.getStatus() != null) {
            dashboard.setStatus(command.getStatus());
        }

        CountrySalesDashboard savedDashboard = dashboardRepository.save(dashboard);
        publishDashboardEvents(savedDashboard);

        return savedDashboard;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardMetrics", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void updateMetrics(CountryDashboardCommand.UpdateCountryMetricsCommand command) {
        log.info("Updating country metrics for dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        String currency = dashboard.getLocalCurrency();
        CountrySalesDashboard.CountryMetrics metrics = CountrySalesDashboard.CountryMetrics.builder()
                .totalRevenue(toMoney(command.getTotalRevenue(), currency))
                .targetRevenue(toMoney(command.getTargetRevenue(), currency))
                .totalDeals(command.getTotalDeals() != null ? command.getTotalDeals() : 0)
                .wonDeals(command.getWonDeals() != null ? command.getWonDeals() : 0)
                .lostDeals(command.getLostDeals() != null ? command.getLostDeals() : 0)
                .averageDealSize(toMoney(command.getAverageDealSize(), currency))
                .weightedPipeline(toMoney(command.getWeightedPipeline(), currency))
                .opportunitiesInPipeline(command.getOpportunitiesInPipeline() != null ?
                        command.getOpportunitiesInPipeline() : 0)
                .newCustomers(command.getNewCustomers() != null ? command.getNewCustomers() : 0)
                .churnedCustomers(command.getChurnedCustomers() != null ? command.getChurnedCustomers() : 0)
                .retentionRate(command.getRetentionRate() != null ? command.getRetentionRate() : BigDecimal.ZERO)
                .npsScore(command.getNpsScore() != null ? command.getNpsScore() : BigDecimal.ZERO)
                .activeSalesReps(command.getActiveSalesReps() != null ? command.getActiveSalesReps() : 0)
                .build();

        dashboard.updateMetrics(metrics);
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Updated country metrics for dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardTerritories", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void updateTerritoryMetric(CountryDashboardCommand.UpdateTerritoryMetricCommand command) {
        log.info("Updating territory metric for dashboard: {}, territory: {}",
                command.getDashboardId(), command.getTerritoryId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        String currency = command.getCurrency() != null ? command.getCurrency() : dashboard.getLocalCurrency();

        CountrySalesDashboard.TerritoryBreakdown territory = CountrySalesDashboard.TerritoryBreakdown.builder()
                .territoryId(command.getTerritoryId())
                .territoryName(command.getTerritoryName() != null ? command.getTerritoryName() : command.getTerritoryId())
                .territoryCode(command.getTerritoryCode())
                .revenue(toMoney(command.getRevenue(), currency))
                .quota(toMoney(command.getQuota(), currency))
                .deals(command.getDeals() != null ? command.getDeals() : 0)
                .wonDeals(command.getWonDeals() != null ? command.getWonDeals() : 0)
                .growthRate(command.getGrowthRate() != null ? command.getGrowthRate() : BigDecimal.ZERO)
                .attributes(command.getAttributes() != null ? command.getAttributes() : new HashMap<>())
                .build();

        // Calculate achievement percentage
        if (territory.getQuota() != null && territory.getQuota().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal achievement = territory.getRevenue().getAmount()
                    .divide(territory.getQuota().getAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            territory.setAchievementPercentage(achievement);
        }

        // Calculate win rate
        if (territory.getDeals() > 0) {
            BigDecimal winRate = new BigDecimal(territory.getWonDeals())
                    .divide(new BigDecimal(territory.getDeals()), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            territory.setWinRate(winRate);
        }

        // Calculate average deal size
        if (territory.getDeals() > 0) {
            territory.setAverageDealSize(territory.getRevenue().divide(new BigDecimal(territory.getDeals())));
        }

        dashboard.updateTerritoryMetric(territory);
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Updated territory metric for dashboard: {}, territory: {}",
                command.getDashboardId(), command.getTerritoryId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardQuota", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void updateQuota(CountryDashboardCommand.UpdateQuotaCommand command) {
        log.info("Updating quota for dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        String currency = command.getCurrency() != null ? command.getCurrency() : dashboard.getLocalCurrency();
        Money quota = Money.of(command.getAnnualQuota(), currency);

        dashboard.updateQuota(quota, command.getReason());
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Updated quota for dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardKPIs", key = "#command.dashboardId")
    })
    public void addKPI(CountryDashboardCommand.AddKPICommand command) {
        log.info("Adding KPI to dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        CountrySalesDashboard.CountryKPI kpi = CountrySalesDashboard.CountryKPI.builder()
                .name(command.getName())
                .type(command.getType())
                .value(command.getValue())
                .target(command.getTarget())
                .weight(command.getWeight() != null ? command.getWeight() : 1)
                .isCritical(command.getIsCritical() != null ? command.getIsCritical() : false)
                .build();

        dashboard.addKPI(kpi);
        dashboardRepository.save(dashboard);

        log.info("Added KPI to dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardKPIs", key = "#command.dashboardId")
    })
    public void updateKPI(CountryDashboardCommand.UpdateKPICommand command) {
        log.info("Updating KPI: {} for dashboard: {}", command.getKpiId(), command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        dashboard.updateKPI(command.getKpiId(), command.getValue());
        dashboardRepository.save(dashboard);

        log.info("Updated KPI: {} for dashboard: {}", command.getKpiId(), command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardTrends", key = "#command.dashboardId")
    })
    public void addTrendData(CountryDashboardCommand.AddTrendDataCommand command) {
        log.info("Adding trend data to dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        String currency = command.getCurrency() != null ? command.getCurrency() : dashboard.getLocalCurrency();

        CountrySalesDashboard.TrendDataPoint dataPoint = CountrySalesDashboard.TrendDataPoint.builder()
                .period(command.getPeriod())
                .date(command.getDate())
                .revenue(toMoney(command.getRevenue(), currency))
                .deals(command.getDeals() != null ? command.getDeals() : 0)
                .winRate(command.getWinRate() != null ? command.getWinRate() : BigDecimal.ZERO)
                .averageDealSize(toMoney(command.getAverageDealSize(), currency))
                .newCustomers(command.getNewCustomers() != null ? command.getNewCustomers() : 0)
                .growthRate(command.getGrowthRate() != null ? command.getGrowthRate() : BigDecimal.ZERO)
                .territory(command.getTerritory())
                .build();

        dashboard.addTrendDataPoint(dataPoint);
        dashboardRepository.save(dashboard);

        log.info("Added trend data to dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardMetrics", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardComparison", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void refreshDashboard(CountryDashboardCommand.RefreshDashboardCommand command) {
        log.info("Refreshing dashboard: {} for tenant: {}", command.getDashboardId(), command.getTenantId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        // Calculate comparisons based on command flags
        boolean calculateYoY = command.getCalculateYoY() != null && command.getCalculateYoY();
        boolean calculateMoM = command.getCalculateMoM() != null && command.getCalculateMoM();
        boolean calculateQoQ = command.getCalculateQoQ() != null && command.getCalculateQoQ();

        // If none specified, calculate all
        if (!calculateYoY && !calculateMoM && !calculateQoQ) {
            dashboard.refresh(command.getUserId());
        } else {
            if (calculateYoY) {
                dashboard.calculateYoYComparison();
            }
            if (calculateMoM) {
                dashboard.calculateMoMComparison();
            }
            if (calculateQoQ) {
                dashboard.calculateQoQComparison();
            }
        }

        CountrySalesDashboard savedDashboard = dashboardRepository.save(dashboard);
        publishDashboardEvents(savedDashboard);

        log.info("Refreshed dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void publishDashboard(CountryDashboardCommand.PublishDashboardCommand command) {
        log.info("Publishing dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        dashboard.publish();
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Published dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void archiveDashboard(CountryDashboardCommand.ArchiveDashboardCommand command) {
        log.info("Archiving dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        dashboard.archive();
        dashboardRepository.save(dashboard);
        publishDashboardEvents(dashboard);

        log.info("Archived dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", key = "#command.dashboardId"),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void updateExchangeRates(CountryDashboardCommand.UpdateExchangeRatesCommand command) {
        log.info("Updating exchange rates for dashboard: {}", command.getDashboardId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        dashboard.updateExchangeRates(command.getExchangeRates());
        dashboardRepository.save(dashboard);

        log.info("Updated exchange rates for dashboard: {}", command.getDashboardId());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "countryDashboards", allEntries = true),
        @CacheEvict(value = "dashboardByCountry", allEntries = true)
    })
    public void delete(CountryDashboardCommand.DeleteDashboardCommand command) {
        log.info("Deleting dashboard: {} for tenant: {}", command.getDashboardId(), command.getTenantId());

        CountrySalesDashboard dashboard = dashboardRepository.findByDashboardIdAndTenantId(
                command.getDashboardId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("CountryDashboard", command.getDashboardId()));

        dashboardRepository.deleteByDashboardIdAndTenantId(command.getDashboardId(), command.getTenantId());

        log.info("Deleted dashboard: {}", command.getDashboardId());
    }

    private void publishDashboardEvents(CountrySalesDashboard dashboard) {
        if (dashboard.getDomainEvents() != null && !dashboard.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : dashboard.getDomainEvents()) {
                if (event instanceof CountryDashboardCreatedEvent) {
                    eventPublisher.publishDashboardCreatedEvent((CountryDashboardCreatedEvent) event);
                } else if (event instanceof CountryMetricUpdatedEvent) {
                    eventPublisher.publishMetricUpdatedEvent((CountryMetricUpdatedEvent) event);
                } else if (event instanceof TerritoryPerformanceUpdatedEvent) {
                    eventPublisher.publishTerritoryPerformanceUpdatedEvent((TerritoryPerformanceUpdatedEvent) event);
                } else if (event instanceof CountryQuotaAdjustedEvent) {
                    eventPublisher.publishQuotaAdjustedEvent((CountryQuotaAdjustedEvent) event);
                } else if (event instanceof CurrencyConversionAppliedEvent) {
                    eventPublisher.publishCurrencyConversionEvent((CurrencyConversionAppliedEvent) event);
                }
            }
            dashboard.clearDomainEvents();
        }
    }

    private Money toMoney(BigDecimal amount, String currency) {
        if (amount == null) {
            return Money.zero(currency);
        }
        return Money.of(amount, currency);
    }
}
