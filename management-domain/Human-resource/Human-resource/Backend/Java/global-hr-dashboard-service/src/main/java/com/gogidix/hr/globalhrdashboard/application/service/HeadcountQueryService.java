package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.CountryHeadcount;
import com.gogidix.hr.globalhrdashboard.domain.repository.CountryHeadcountRepository;
import com.gogidix.hr.globalhrdashboard.shared.exception.NotFoundException;
import com.gogidix.hr.globalhrdashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Query Service for Headcount Metrics
 * Handles all read operations for headcount data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HeadcountQueryService {

    private final CountryHeadcountRepository countryHeadcountRepository;

    /**
     * Get global headcount total for a period
     */
    public GlobalHeadcount getGlobalHeadcount(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching global headcount for period: {} tenant: {}", period, tenantId);

        Integer total = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, period);
        List<CountryHeadcount> byCountry = countryHeadcountRepository.findByTenantIdAndPeriod(tenantId, period);

        int permanent = byCountry.stream()
                .mapToInt(c -> c.getPermanentEmployees() != null ? c.getPermanentEmployees() : 0)
                .sum();
        int contractors = byCountry.stream()
                .mapToInt(c -> c.getContractors() != null ? c.getContractors() : 0)
                .sum();
        int interns = byCountry.stream()
                .mapToInt(c -> c.getInterns() != null ? c.getInterns() : 0)
                .sum();

        return new GlobalHeadcount(total, permanent, contractors, interns, byCountry.size());
    }

    /**
     * Get headcount by country
     */
    public CountryHeadcount getHeadcountByCountry(String countryCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount for country: {} period: {} tenant: {}", countryCode, period, tenantId);

        List<CountryHeadcount> results = countryHeadcountRepository.findByTenantIdAndCountryCodeAndPeriod(
                tenantId, countryCode, period);

        if (results.isEmpty()) {
            throw new NotFoundException("CountryHeadcount",
                    String.format("country=%s,period=%s", countryCode, period));
        }

        // Aggregate all departments for the country
        return aggregateCountryHeadcount(results, countryCode, period);
    }

    /**
     * Get headcount by department
     */
    public List<CountryHeadcount> getHeadcountByDepartment(String department, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount for department: {} period: {} tenant: {}", department, period, tenantId);

        return countryHeadcountRepository.findByTenantIdAndPeriod(tenantId, period).stream()
                .filter(h -> department.equals(h.getDepartment()))
                .collect(Collectors.toList());
    }

    /**
     * Get headcount by region
     */
    public List<CountryHeadcount> getHeadcountByRegion(String regionCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount for region: {} period: {} tenant: {}", regionCode, period, tenantId);

        return countryHeadcountRepository.findByTenantIdAndRegionCodeAndPeriod(tenantId, regionCode, period);
    }

    /**
     * Get headcount trend over time
     */
    public List<CountryHeadcount> getHeadcountTrend(String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount trend from {} to {} for tenant: {}", startPeriod, endPeriod, tenantId);

        List<CountryHeadcount> allData = countryHeadcountRepository.findByTenantId(tenantId);

        return allData.stream()
                .filter(h -> h.getPeriod().compareTo(startPeriod) >= 0 && h.getPeriod().compareTo(endPeriod) <= 0)
                .filter(h -> h.getCountryCode().equals("GLOBAL") || h.getDepartment() == null)
                .sorted(Comparator.comparing(CountryHeadcount::getPeriod))
                .collect(Collectors.toList());
    }

    /**
     * Get headcount trend for a specific country
     */
    public List<CountryHeadcount> getHeadcountTrendByCountry(String countryCode, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount trend for country: {} from {} to {} tenant: {}",
                countryCode, startPeriod, endPeriod, tenantId);

        return countryHeadcountRepository.findTrendDataByCountry(tenantId, countryCode, startPeriod, endPeriod);
    }

    /**
     * Get headcount trend for a specific department
     */
    public List<CountryHeadcount> getHeadcountTrendByDepartment(String department, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount trend for department: {} from {} to {} tenant: {}",
                department, startPeriod, endPeriod, tenantId);

        return countryHeadcountRepository.findTrendDataByDepartment(tenantId, department, startPeriod, endPeriod);
    }

    /**
     * Get all countries
     */
    public List<String> getAllCountries() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all countries for tenant: {}", tenantId);
        return countryHeadcountRepository.findDistinctCountriesByTenantId(tenantId);
    }

    /**
     * Get all departments
     */
    public List<String> getAllDepartments() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all departments for tenant: {}", tenantId);
        return countryHeadcountRepository.findDistinctDepartmentsByTenantId(tenantId);
    }

    /**
     * Get headcount by ID
     */
    public CountryHeadcount getHeadcountById(String headcountId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount by ID: {} for tenant: {}", headcountId, tenantId);
        return countryHeadcountRepository.findByIdAndTenantId(headcountId, tenantId)
                .orElseThrow(() -> new NotFoundException("CountryHeadcount", headcountId));
    }

    /**
     * Get headcount updated since
     */
    public List<CountryHeadcount> getHeadcountUpdatedSince(Instant lastUpdated) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching headcount updated since: {} for tenant: {}", lastUpdated, tenantId);
        return countryHeadcountRepository.findByTenantIdAndLastUpdatedAfter(tenantId, lastUpdated);
    }

    /**
     * Get latest headcount for a period
     */
    public List<CountryHeadcount> getLatestByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest headcount for period: {} tenant: {}", period, tenantId);
        return countryHeadcountRepository.findLatestByPeriod(tenantId, period);
    }

    /**
     * Get countries with highest headcount
     */
    public List<CountryHeadcount> getTopCountriesByHeadcount(String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching top {} countries by headcount for period: {} tenant: {}",
                limit, period, tenantId);

        List<CountryHeadcount> allHeadcount = countryHeadcountRepository.findByTenantIdAndPeriod(tenantId, period);

        return allHeadcount.stream()
                .filter(h -> h.getDepartment() == null) // Only get country-level totals
                .filter(h -> h.getTotalHeadcount() != null)
                .sorted(Comparator.comparing(CountryHeadcount::getTotalHeadcount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get headcount summary by department for a period
     */
    public Map<String, Integer> getDepartmentSummary(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching department summary for period: {} tenant: {}", period, tenantId);

        List<CountryHeadcount> allHeadcount = countryHeadcountRepository.findByTenantIdAndPeriod(tenantId, period);

        return allHeadcount.stream()
                .filter(h -> h.getDepartment() != null)
                .collect(Collectors.groupingBy(
                        CountryHeadcount::getDepartment,
                        Collectors.summingInt(h -> h.getTotalHeadcount() != null ? h.getTotalHeadcount() : 0)
                ));
    }

    /**
     * Get headcount growth metrics
     */
    public HeadcountGrowth getHeadcountGrowth(String currentPeriod, String previousPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating headcount growth between {} and {} for tenant: {}",
                previousPeriod, currentPeriod, tenantId);

        Integer currentTotal = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, currentPeriod);
        Integer previousTotal = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, previousPeriod);

        if (currentTotal == null) currentTotal = 0;
        if (previousTotal == null) previousTotal = 0;

        int absoluteChange = currentTotal - previousTotal;
        double percentChange = previousTotal > 0 ? ((double) absoluteChange / previousTotal) * 100 : 0.0;

        return new HeadcountGrowth(currentTotal, previousTotal, absoluteChange, percentChange);
    }

    /**
     * Check if headcount exists
     */
    public boolean headcountExists(String headcountId) {
        String tenantId = RequestContextHolder.getTenantId();
        return countryHeadcountRepository.existsByIdAndTenantId(headcountId, tenantId);
    }

    /**
     * Count headcount by country
     */
    public long countByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        return countryHeadcountRepository.countByTenantIdAndCountryCode(tenantId, countryCode);
    }

    private CountryHeadcount aggregateCountryHeadcount(List<CountryHeadcount> headcounts, String countryCode, String period) {
        int total = headcounts.stream()
                .mapToInt(h -> h.getTotalHeadcount() != null ? h.getTotalHeadcount() : 0)
                .sum();
        int permanent = headcounts.stream()
                .mapToInt(h -> h.getPermanentEmployees() != null ? h.getPermanentEmployees() : 0)
                .sum();
        int contractors = headcounts.stream()
                .mapToInt(h -> h.getContractors() != null ? h.getContractors() : 0)
                .sum();
        int interns = headcounts.stream()
                .mapToInt(h -> h.getInterns() != null ? h.getInterns() : 0)
                .sum();

        CountryHeadcount aggregated = new CountryHeadcount(
                headcounts.get(0).getTenantId(),
                countryCode,
                headcounts.get(0).getCountryName(),
                headcounts.get(0).getRegionCode(),
                total,
                period
        );

        aggregated.setPermanentEmployees(permanent);
        aggregated.setContractors(contractors);
        aggregated.setInterns(interns);

        return aggregated;
    }

    /**
     * Global headcount record
     */
    public record GlobalHeadcount(
            int totalHeadcount,
            int permanentEmployees,
            int contractors,
            int interns,
            int totalCountries
    ) {}

    /**
     * Headcount growth record
     */
    public record HeadcountGrowth(
            int currentTotal,
            int previousTotal,
            int absoluteChange,
            double percentChange
    ) {}
}
