package com.gogidix.finance.forecasting.application.service;

import com.gogidix.finance.forecasting.domain.model.Forecast;
import com.gogidix.finance.forecasting.domain.port.in.ForecastQuery;
import com.gogidix.finance.forecasting.domain.repository.ForecastRepository;
import com.gogidix.finance.forecasting.shared.exception.NotFoundException;
import com.gogidix.finance.forecasting.shared.exception.ValidationException;
import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
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

    /**
     * Get forecast by ID
     *
     * @param forecastId the forecast ID
     * @return the forecast
     */
    public Forecast getById(String forecastId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecast: {} for tenant: {}", forecastId, tenantId);

        return forecastRepository.findByForecastIdAndTenantId(forecastId, tenantId)
                .orElseThrow(() -> new NotFoundException("Forecast", forecastId));
    }

    /**
     * Get forecasts by type
     *
     * @param forecastType the forecast type
     * @param page the page number
     * @param size the page size
     * @param sortBy the sort field
     * @param sortDirection the sort direction
     * @return page of forecasts
     */
    public Page<Forecast> getByType(Forecast.ForecastType forecastType, int page, int size,
                                     String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for type: {} in tenant: {}", forecastType, tenantId);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndForecastType(
                tenantId, forecastType);

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts by date range
     *
     * @param startDate the start date
     * @param endDate the end date
     * @param statuses the list of statuses to filter
     * @param types the list of types to filter
     * @param page the page number
     * @param size the page size
     * @return page of forecasts
     */
    public Page<Forecast> getByDateRange(Instant startDate, Instant endDate,
                                          List<Forecast.ForecastStatus> statuses,
                                          List<Forecast.ForecastType> types,
                                          int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for date range: {} to {} in tenant: {}",
                startDate, endDate, tenantId);

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndStartDateBetween(
                tenantId, startDate, endDate);

        // Apply status filter if provided
        if (statuses != null && !statuses.isEmpty()) {
            forecasts = forecasts.stream()
                    .filter(f -> statuses.contains(f.getStatus()))
                    .collect(Collectors.toList());
        }

        // Apply type filter if provided
        if (types != null && !types.isEmpty()) {
            forecasts = forecasts.stream()
                    .filter(f -> types.contains(f.getForecastType()))
                    .collect(Collectors.toList());
        }

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts by status
     *
     * @param status the forecast status
     * @param page the page number
     * @param size the page size
     * @param sortBy the sort field
     * @param sortDirection the sort direction
     * @return page of forecasts
     */
    public Page<Forecast> getByStatus(Forecast.ForecastStatus status, int page, int size,
                                      String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for status: {} in tenant: {}", status, tenantId);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndStatus(tenantId, status);

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts by horizon
     *
     * @param forecastHorizon the forecast horizon
     * @param page the page number
     * @param size the page size
     * @return page of forecasts
     */
    public Page<Forecast> getByHorizon(Forecast.ForecastHorizon forecastHorizon,
                                        int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for horizon: {} in tenant: {}", forecastHorizon, tenantId);

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndForecastHorizon(
                tenantId, forecastHorizon);

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts by department
     *
     * @param department the department
     * @param page the page number
     * @param size the page size
     * @param status optional status filter
     * @return page of forecasts
     */
    public Page<Forecast> getByDepartment(String department, int page, int size,
                                          Forecast.ForecastStatus status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for department: {} in tenant: {}", department, tenantId);

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndDepartment(tenantId, department);

        if (status != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getStatus() == status)
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts by scenario
     *
     * @param scenario the scenario identifier
     * @param page the page number
     * @param size the page size
     * @return page of forecasts
     */
    public Page<Forecast> getByScenario(String scenario, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for scenario: {} in tenant: {}", scenario, tenantId);

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndScenario(tenantId, scenario);

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Search forecasts
     *
     * @param searchTerm the search term
     * @param forecastType optional forecast type filter
     * @param status optional status filter
     * @param forecastHorizon optional forecast horizon filter
     * @param department optional department filter
     * @param category optional category filter
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @param page the page number
     * @param size the page size
     * @param sortBy the sort field
     * @param sortDirection the sort direction
     * @return page of forecasts
     */
    public Page<Forecast> search(String searchTerm, Forecast.ForecastType forecastType,
                                  Forecast.ForecastStatus status, Forecast.ForecastHorizon forecastHorizon,
                                  String department, String category, Instant startDate, Instant endDate,
                                  int page, int size, String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching forecasts with term: {} in tenant: {}", searchTerm, tenantId);

        List<Forecast> forecasts = forecastRepository.searchByTenantId(tenantId, searchTerm);

        // Apply filters
        if (forecastType != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getForecastType() == forecastType)
                    .collect(Collectors.toList());
        }
        if (status != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getStatus() == status)
                    .collect(Collectors.toList());
        }
        if (forecastHorizon != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getForecastHorizon() == forecastHorizon)
                    .collect(Collectors.toList());
        }
        if (department != null) {
            forecasts = forecasts.stream()
                    .filter(f -> department.equals(f.getDepartment()))
                    .collect(Collectors.toList());
        }
        if (category != null) {
            forecasts = forecasts.stream()
                    .filter(f -> category.equals(f.getCategory()))
                    .collect(Collectors.toList());
        }
        if (startDate != null && endDate != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getStartDate() != null
                            && !f.getStartDate().isBefore(startDate)
                            && !f.getStartDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecast summary
     *
     * @param startDate optional start date
     * @param endDate optional end date
     * @param department optional department filter
     * @param forecastType optional forecast type filter
     * @param scenario optional scenario filter
     * @return forecast summary
     */
    public ForecastSummary getSummary(Instant startDate, Instant endDate,
                                       String department, Forecast.ForecastType forecastType,
                                       String scenario) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecast summary for tenant: {}", tenantId);

        List<Forecast> forecasts;

        if (scenario != null) {
            forecasts = forecastRepository.findByTenantIdAndScenario(tenantId, scenario);
        } else if (department != null) {
            forecasts = forecastRepository.findByTenantIdAndDepartment(tenantId, department);
        } else if (forecastType != null && startDate != null && endDate != null) {
            forecasts = forecastRepository.findByTenantIdAndForecastTypeAndStartDateBetween(
                    tenantId, forecastType, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            forecasts = forecastRepository.findByTenantIdAndStartDateBetween(
                    tenantId, startDate, endDate);
        } else {
            forecasts = forecastRepository.findByTenantId(tenantId);
        }

        // Apply additional filters
        if (forecastType != null && scenario == null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getForecastType() == forecastType)
                    .collect(Collectors.toList());
        }

        BigDecimal totalForecastAmount = forecasts.stream()
                .map(Forecast::getTotalForecastAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalActualAmount = forecasts.stream()
                .map(Forecast::getActualAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long draftCount = forecasts.stream()
                .filter(f -> f.getStatus() == Forecast.ForecastStatus.DRAFT)
                .count();

        long pendingCount = forecasts.stream()
                .filter(f -> f.getStatus() == Forecast.ForecastStatus.PENDING_APPROVAL)
                .count();

        long approvedCount = forecasts.stream()
                .filter(f -> f.getStatus() == Forecast.ForecastStatus.APPROVED)
                .count();

        Map<Forecast.ForecastType, Long> countByType = forecasts.stream()
                .collect(Collectors.groupingBy(Forecast::getForecastType, Collectors.counting()));

        Map<Forecast.ForecastHorizon, Long> countByHorizon = forecasts.stream()
                .filter(f -> f.getForecastHorizon() != null)
                .collect(Collectors.groupingBy(Forecast::getForecastHorizon, Collectors.counting()));

        return ForecastSummary.builder()
                .totalCount(forecasts.size())
                .totalForecastAmount(totalForecastAmount)
                .totalActualAmount(totalActualAmount)
                .varianceAmount(totalActualAmount.subtract(totalForecastAmount))
                .draftCount(draftCount)
                .pendingCount(pendingCount)
                .approvedCount(approvedCount)
                .countByType(countByType)
                .countByHorizon(countByHorizon)
                .build();
    }

    /**
     * Get forecasts pending approval
     *
     * @param department optional department filter
     * @param forecastType optional forecast type filter
     * @param page the page number
     * @param size the page size
     * @return page of pending forecasts
     */
    public Page<Forecast> getPendingApproval(String department, Forecast.ForecastType forecastType,
                                             int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching pending approvals for tenant: {}", tenantId);

        List<Forecast> forecasts = department != null
                ? forecastRepository.findPendingApprovalByTenantIdAndDepartment(tenantId, department)
                : forecastRepository.findPendingApprovalByTenantId(tenantId);

        if (forecastType != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getForecastType() == forecastType)
                    .collect(Collectors.toList());
        }

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "createdAt"));
        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get all forecasts for tenant
     *
     * @return list of forecasts
     */
    public List<Forecast> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all forecasts for tenant: {}", tenantId);

        return forecastRepository.findByTenantId(tenantId);
    }

    /**
     * Get forecasts by status
     *
     * @param status the status
     * @return list of forecasts
     */
    public List<Forecast> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by status: {} for tenant: {}", status, tenantId);

        Forecast.ForecastStatus statusEnum = Forecast.ForecastStatus.valueOf(status);
        return forecastRepository.findByTenantIdAndStatus(tenantId, statusEnum);
    }

    /**
     * Count forecasts by tenant
     *
     * @return count of forecasts
     */
    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return forecastRepository.countByTenantId(tenantId);
    }

    /**
     * Count forecasts by status
     *
     * @param status the status
     * @return count of forecasts
     */
    public long countByStatus(Forecast.ForecastStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return forecastRepository.countByTenantIdAndStatus(tenantId, status);
    }

    /**
     * Sum forecast amounts by status
     *
     * @param status the status
     * @return total amount
     */
    public BigDecimal sumAmountByStatus(Forecast.ForecastStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return forecastRepository.sumTotalForecastAmountByTenantIdAndStatus(tenantId, status);
    }

    /**
     * Get latest forecasts
     *
     * @param forecastType optional forecast type filter
     * @param limit the maximum number of results
     * @param status optional status filter
     * @return list of latest forecasts
     */
    public List<Forecast> getLatestForecasts(Forecast.ForecastType forecastType,
                                              int limit, Forecast.ForecastStatus status) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching latest forecasts for tenant: {}", tenantId);

        List<Forecast> forecasts = forecastRepository.findLatestByTenantId(tenantId, limit);

        if (forecastType != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getForecastType() == forecastType)
                    .collect(Collectors.toList());
        }

        if (status != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getStatus() == status)
                    .collect(Collectors.toList());
        }

        return forecasts.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get archived forecasts
     *
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @param page the page number
     * @param size the page size
     * @return page of archived forecasts
     */
    public Page<Forecast> getArchived(Instant startDate, Instant endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching archived forecasts for tenant: {}", tenantId);

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "updatedAt"));

        List<Forecast> forecasts = forecastRepository.findArchivedByTenantId(tenantId);

        if (startDate != null && endDate != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getUpdatedAt() != null
                            && !f.getUpdatedAt().isBefore(startDate)
                            && !f.getUpdatedAt().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts by creator
     *
     * @param createdBy the creator user ID
     * @param status optional status filter
     * @param page the page number
     * @param size the page size
     * @return page of forecasts
     */
    public Page<Forecast> getByCreator(String createdBy, Forecast.ForecastStatus status,
                                       int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts by creator: {} in tenant: {}", createdBy, tenantId);

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Forecast> forecasts = forecastRepository.findByTenantIdAndCreatedBy(tenantId, createdBy);

        if (status != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getStatus() == status)
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    /**
     * Get forecasts for comparison
     *
     * @param forecastIds the list of forecast IDs to compare
     * @return list of forecasts
     */
    public List<Forecast> getForecastsForComparison(List<String> forecastIds) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for comparison in tenant: {}", tenantId);

        return forecastRepository.findByTenantIdAndForecastIdIn(tenantId, forecastIds);
    }

    /**
     * Get variance analysis between forecasts
     *
     * @param forecastId the base forecast ID
     * @param comparisonForecastId the comparison forecast ID
     * @return variance analysis result
     */
    public VarianceAnalysis getVarianceAnalysis(String forecastId, String comparisonForecastId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching variance analysis for forecasts: {} and {}",
                forecastId, comparisonForecastId);

        Forecast baseForecast = forecastRepository.findByForecastIdAndTenantId(forecastId, tenantId)
                .orElseThrow(() -> new NotFoundException("Forecast", forecastId));

        Forecast comparisonForecast = forecastRepository.findByForecastIdAndTenantId(
                        comparisonForecastId, tenantId)
                .orElseThrow(() -> new NotFoundException("Forecast", comparisonForecastId));

        BigDecimal varianceAmount = baseForecast.getTotalForecastAmount()
                .subtract(comparisonForecast.getTotalForecastAmount());

        BigDecimal variancePercentage = BigDecimal.ZERO;
        if (comparisonForecast.getTotalForecastAmount() != null
                && comparisonForecast.getTotalForecastAmount().compareTo(BigDecimal.ZERO) != 0) {
            variancePercentage = varianceAmount
                    .divide(comparisonForecast.getTotalForecastAmount(), 4,
                            java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        return VarianceAnalysis.builder()
                .baseForecastId(forecastId)
                .comparisonForecastId(comparisonForecastId)
                .baseAmount(baseForecast.getTotalForecastAmount())
                .comparisonAmount(comparisonForecast.getTotalForecastAmount())
                .varianceAmount(varianceAmount)
                .variancePercentage(variancePercentage)
                .baseType(baseForecast.getForecastType())
                .comparisonType(comparisonForecast.getForecastType())
                .build();
    }

    /**
     * Forecast summary record
     */
    @lombok.Builder
    @lombok.Data
    public static class ForecastSummary {
        private long totalCount;
        private BigDecimal totalForecastAmount;
        private BigDecimal totalActualAmount;
        private BigDecimal varianceAmount;
        private long draftCount;
        private long pendingCount;
        private long approvedCount;
        private Map<Forecast.ForecastType, Long> countByType;
        private Map<Forecast.ForecastHorizon, Long> countByHorizon;
    }

    /**
     * Variance analysis record
     */
    @lombok.Builder
    @lombok.Data
    public static class VarianceAnalysis {
        private String baseForecastId;
        private String comparisonForecastId;
        private BigDecimal baseAmount;
        private BigDecimal comparisonAmount;
        private BigDecimal varianceAmount;
        private BigDecimal variancePercentage;
        private Forecast.ForecastType baseType;
        private Forecast.ForecastType comparisonType;
    }
}
