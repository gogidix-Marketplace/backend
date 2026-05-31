package com.gogidix.sales.forecast.application.service;

import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.domain.repository.ForecastRepository;
import com.gogidix.sales.forecast.shared.exception.NotFoundException;
import com.gogidix.sales.forecast.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Forecast Query Service
 * Handles all read operations for forecasts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ForecastQueryService {

    private final ForecastRepository forecastRepository;

    public Forecast getById(String forecastId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecast: {} for tenant: {}", forecastId, tenantId);

        return forecastRepository.findByForecastIdAndTenantId(forecastId, tenantId)
            .orElseThrow(() -> new NotFoundException("Forecast", forecastId));
    }

    public List<Forecast> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all forecasts for tenant: {}", tenantId);

        return forecastRepository.findByTenantId(tenantId);
    }

    public List<Forecast> getByStatus(Forecast.ForecastStatus status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by status: {} for tenant: {}", status, tenantId);

        return forecastRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Forecast> getByPeriod(Forecast.ForecastPeriod period) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by period: {} for tenant: {}", period, tenantId);

        return forecastRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    public List<Forecast> getByDateRange(YearMonth startDate, YearMonth endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for date range: {} to {} for tenant: {}",
            startDate, endDate, tenantId);

        return forecastRepository.findByTenantIdAndStartDateBetween(tenantId, startDate, endDate);
    }

    public List<Forecast> getByRegion(String region) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by region: {} for tenant: {}", region, tenantId);

        return forecastRepository.findByTenantIdAndRegion(tenantId, region);
    }

    public List<Forecast> getByTerritory(String territory) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by territory: {} for tenant: {}", territory, tenantId);

        return forecastRepository.findByTenantIdAndTerritory(tenantId, territory);
    }

    public List<Forecast> getByBusinessUnit(String businessUnit) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by business unit: {} for tenant: {}", businessUnit, tenantId);

        return forecastRepository.findByTenantIdAndBusinessUnit(tenantId, businessUnit);
    }

    public List<Forecast> getByCreatedBy(String createdBy) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by creator: {} for tenant: {}", createdBy, tenantId);

        return forecastRepository.findByTenantIdAndCreatedBy(tenantId, createdBy);
    }

    public List<Forecast> getVersions(String forecastId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching versions for forecast: {} for tenant: {}", forecastId, tenantId);

        return forecastRepository.findVersionsByForecastIdAndTenantId(forecastId, tenantId);
    }

    public List<Forecast> getPendingApproval(Forecast.ApprovalLevel approvalLevel) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching pending approvals for level: {} in tenant: {}", approvalLevel, tenantId);

        return forecastRepository.findPendingApprovalByTenantIdAndApproverLevel(tenantId, approvalLevel);
    }

    public List<Forecast> getActiveForecasts() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching active forecasts for tenant: {}", tenantId);

        return forecastRepository.findActiveByTenantId(tenantId);
    }

    public List<Forecast> getPublishedForecasts(YearMonth startDate, YearMonth endDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching published forecasts for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        return forecastRepository.findPublishedByTenantIdAndDateRange(tenantId, startDate, endDate);
    }

    public ForecastSummary getSummary(YearMonth startDate, YearMonth endDate,
                                       String region, String territory) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecast summary for tenant: {}", tenantId);

        List<Forecast> forecasts;

        if (territory != null && !territory.isBlank()) {
            forecasts = forecastRepository.findByTenantIdAndTerritory(tenantId, territory);
        } else if (region != null && !region.isBlank()) {
            forecasts = forecastRepository.findByTenantIdAndRegion(tenantId, region);
        } else if (startDate != null && endDate != null) {
            forecasts = forecastRepository.findByTenantIdAndStartDateBetween(tenantId, startDate, endDate);
        } else {
            forecasts = forecastRepository.findByTenantId(tenantId);
        }

        BigDecimal totalBestCase = forecasts.stream()
            .map(Forecast::getTotalBestCase)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLikely = forecasts.stream()
            .map(Forecast::getTotalLikely)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWorstCase = forecasts.stream()
            .map(Forecast::getTotalWorstCase)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long draftCount = forecasts.stream()
            .filter(f -> f.getStatus() == Forecast.ForecastStatus.DRAFT)
            .count();

        long submittedCount = forecasts.stream()
            .filter(f -> f.getStatus() == Forecast.ForecastStatus.SUBMITTED)
            .count();

        long approvedCount = forecasts.stream()
            .filter(f -> f.getStatus() == Forecast.ForecastStatus.APPROVED)
            .count();

        long publishedCount = forecasts.stream()
            .filter(f -> f.getStatus() == Forecast.ForecastStatus.PUBLISHED)
            .count();

        return new ForecastSummary(
            (long) forecasts.size(),
            totalBestCase,
            totalLikely,
            totalWorstCase,
            draftCount,
            submittedCount,
            approvedCount,
            publishedCount
        );
    }

    public Map<String, BigDecimal> getForecastByRegion() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecast totals by region for tenant: {}", tenantId);

        return getAllForTenant().stream()
            .filter(f -> f.getRegion() != null)
            .collect(Collectors.groupingBy(
                Forecast::getRegion,
                Collectors.reducing(BigDecimal.ZERO, f -> f.getTotalLikely() != null ? f.getTotalLikely() : BigDecimal.ZERO, BigDecimal::add)
            ));
    }

    public Map<String, BigDecimal> getForecastByTerritory() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecast totals by territory for tenant: {}", tenantId);

        return getAllForTenant().stream()
            .filter(f -> f.getTerritory() != null)
            .collect(Collectors.groupingBy(
                Forecast::getTerritory,
                Collectors.reducing(BigDecimal.ZERO, f -> f.getTotalLikely() != null ? f.getTotalLikely() : BigDecimal.ZERO, BigDecimal::add)
            ));
    }

    public Page<Forecast> getPaginated(int page, int size, String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching paginated forecasts for tenant: {}, page: {}", tenantId, page);

        List<Forecast> forecasts = forecastRepository.findByTenantId(tenantId);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // Sort the list
        Comparator<Forecast> comparator = getComparator(sortBy, sortDirection);
        forecasts.sort(comparator);

        // Paginate
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), forecasts.size());

        List<Forecast> pageContent = forecasts.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, forecasts.size());
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return forecastRepository.countByTenantId(tenantId);
    }

    public long countByStatus(Forecast.ForecastStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return forecastRepository.countByTenantIdAndStatus(tenantId, status);
    }

    private Comparator<Forecast> getComparator(String sortBy, String sortDirection) {
        boolean ascending = sortDirection.equalsIgnoreCase("asc");

        Comparator<Forecast> comparator = switch (sortBy) {
            case "name" -> Comparator.comparing(Forecast::getName, Comparator.nullsLast(String::compareTo));
            case "startDate" -> Comparator.comparing(Forecast::getStartDate, Comparator.nullsLast(YearMonth::compareTo));
            case "endDate" -> Comparator.comparing(Forecast::getEndDate, Comparator.nullsLast(YearMonth::compareTo));
            case "totalLikely" -> Comparator.comparing(Forecast::getTotalLikely, Comparator.nullsLast(BigDecimal::compareTo));
            case "status" -> Comparator.comparing(Forecast::getStatus, Comparator.nullsLast(Enum::compareTo));
            case "createdAt" -> Comparator.comparing(Forecast::getCreatedAt, Comparator.nullsLast(java.time.Instant::compareTo));
            default -> Comparator.comparing(Forecast::getCreatedAt, Comparator.nullsLast(java.time.Instant::compareTo));
        };
        return comparator.thenComparing(f -> f.getId() != null ? f.getId() : "");
    }

    public record ForecastSummary(
        long totalCount,
        BigDecimal totalBestCase,
        BigDecimal totalLikely,
        BigDecimal totalWorstCase,
        long draftCount,
        long submittedCount,
        long approvedCount,
        long publishedCount
    ) {}
}
