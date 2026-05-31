package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.*;
import com.gogidix.hr.globalhrdashboard.domain.repository.*;
import com.gogidix.hr.globalhrdashboard.shared.exception.NotFoundException;
import com.gogidix.hr.globalhrdashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Query Service for Dashboard Views
 * Handles all read operations for executive dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardQueryService {

    private final GlobalWorkforceMetricRepository globalWorkforceMetricRepository;
    private final RegionalMetricRepository regionalMetricRepository;
    private final CountryHeadcountRepository countryHeadcountRepository;
    private final ComplianceMetricRepository complianceMetricRepository;
    private final DiversityMetricRepository diversityMetricRepository;
    private final RetentionMetricRepository retentionMetricRepository;

    /**
     * Get CHRO dashboard (all HR metrics)
     */
    public CHRODashboardView getCHRODashboard(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching CHRO dashboard for period: {} tenant: {}", period, tenantId);

        List<GlobalWorkforceMetric> globalMetrics =
                globalWorkforceMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        Integer globalHeadcount = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, period);

        Double avgRetentionRate = retentionMetricRepository.getAverageRetentionRate(tenantId, period);
        Double avgComplianceScore = complianceMetricRepository.getAverageComplianceScore(tenantId, period);
        Double avgDiversityScore = diversityMetricRepository.getAverageOverallDiversityScore(tenantId, period);

        long atRiskCountries = complianceMetricRepository.findAtRiskByTenantId(tenantId).stream()
                .filter(m -> period.equals(m.getPeriod()))
                .count();

        return new CHRODashboardView(
                period,
                globalHeadcount != null ? globalHeadcount : 0,
                avgRetentionRate != null ? avgRetentionRate : 0.0,
                avgComplianceScore != null ? avgComplianceScore : 0.0,
                avgDiversityScore != null ? avgDiversityScore : 0.0,
                atRiskCountries,
                globalMetrics
        );
    }

    /**
     * Get CEO dashboard (workforce summary)
     */
    public CEODashboardView getCEODashboard(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching CEO dashboard for period: {} tenant: {}", period, tenantId);

        Integer totalHeadcount = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, period);

        List<CountryHeadcount> byCountry = countryHeadcountRepository.findByTenantIdAndPeriod(tenantId, period);

        int totalCountries = (int) byCountry.stream()
                .filter(h -> h.getDepartment() == null)
                .count();

        int totalRegions = (int) byCountry.stream()
                .map(CountryHeadcount::getRegionCode)
                .distinct()
                .count();

        Double avgRetentionRate = retentionMetricRepository.getAverageRetentionRate(tenantId, period);

        Long yoyChange = calculateYoYChange(tenantId, period);

        List<RegionalSummary> regionalSummaries = getRegionalSummaries(tenantId, period);

        return new CEODashboardView(
                period,
                totalHeadcount != null ? totalHeadcount : 0,
                totalCountries,
                totalRegions,
                avgRetentionRate != null ? avgRetentionRate : 0.0,
                yoyChange != null ? yoyChange : 0,
                regionalSummaries
        );
    }

    /**
     * Get compliance dashboard
     */
    public ComplianceDashboardView getComplianceDashboard(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance dashboard for period: {} tenant: {}", period, tenantId);

        List<ComplianceMetric> metrics = complianceMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        long compliantCount = metrics.stream().filter(m -> m.getStatus() == ComplianceStatus.COMPLIANT).count();
        long atRiskCount = metrics.stream().filter(m -> m.getStatus() == ComplianceStatus.AT_RISK).count();
        long nonCompliantCount = metrics.stream().filter(m -> m.getStatus() == ComplianceStatus.NON_COMPLIANT).count();

        List<ComplianceMetric> atRiskCountries = complianceMetricRepository.findAtRiskByTenantId(tenantId).stream()
                .filter(m -> period.equals(m.getPeriod()))
                .collect(Collectors.toList());

        List<ComplianceMetric> nonCompliantCountries = complianceMetricRepository.findNonCompliantByTenantId(tenantId).stream()
                .filter(m -> period.equals(m.getPeriod()))
                .collect(Collectors.toList());

        List<ComplianceMetric> withCriticalIssues = metrics.stream()
                .filter(ComplianceMetric::hasCriticalIssues)
                .collect(Collectors.toList());

        Double avgScore = metrics.stream()
                .mapToDouble(ComplianceMetric::getComplianceScore)
                .average()
                .orElse(0.0);

        ComplianceStatus globalStatus = ComplianceStatus.fromScore(avgScore);

        return new ComplianceDashboardView(
                period,
                metrics.size(),
                compliantCount,
                atRiskCount,
                nonCompliantCount,
                avgScore,
                globalStatus,
                atRiskCountries,
                nonCompliantCountries,
                withCriticalIssues
        );
    }

    /**
     * Get diversity dashboard
     */
    public DiversityDashboardView getDiversityDashboard(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity dashboard for period: {} tenant: {}", period, tenantId);

        List<DiversityMetric> metrics = diversityMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        int totalEmployees = metrics.stream()
                .mapToInt(m -> m.getTotalEmployees() != null ? m.getTotalEmployees() : 0)
                .sum();

        Double avgGenderScore = diversityMetricRepository.getAverageGenderDiversityScore(tenantId, period);
        Double avgNationalScore = diversityMetricRepository.getAverageNationalDiversityScore(tenantId, period);
        Double avgOverallScore = diversityMetricRepository.getAverageOverallDiversityScore(tenantId, period);

        Map<String, Integer> globalGenderDistribution = calculateGlobalGenderDistribution(metrics);
        Map<String, Integer> globalNationalityDistribution = calculateGlobalNationalityDistribution(metrics);

        List<DiversityMetric> topPerformers = metrics.stream()
                .filter(m -> m.getOverallDiversityScore() != null)
                .sorted(Comparator.comparing(DiversityMetric::getOverallDiversityScore).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return new DiversityDashboardView(
                period,
                totalEmployees,
                avgGenderScore != null ? avgGenderScore : 0.0,
                avgNationalScore != null ? avgNationalScore : 0.0,
                avgOverallScore != null ? avgOverallScore : 0.0,
                globalGenderDistribution,
                globalNationalityDistribution,
                topPerformers
        );
    }

    /**
     * Get country-specific dashboard
     */
    public CountryDashboardView getCountryDashboard(String countryCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching country dashboard for: {} period: {} tenant: {}", countryCode, period, tenantId);

        CountryHeadcount headcount = countryHeadcountRepository.findByTenantIdAndCountryCodeAndPeriod(
                tenantId, countryCode, period).stream()
                .filter(h -> h.getDepartment() == null)
                .findFirst()
                .orElse(null);

        ComplianceMetric compliance = complianceMetricRepository.findByTenantIdAndCountryCodeAndPeriod(
                tenantId, countryCode, period).orElse(null);

        DiversityMetric diversity = diversityMetricRepository.findByTenantIdAndCountryCodeAndPeriod(
                tenantId, countryCode, period).orElse(null);

        RetentionMetric retention = retentionMetricRepository.findByTenantIdAndCountryCodeAndPeriod(
                tenantId, countryCode, period).orElse(null);

        return new CountryDashboardView(
                countryCode,
                period,
                headcount,
                compliance,
                diversity,
                retention
        );
    }

    /**
     * Get regional summary
     */
    public RegionalSummaryDashboard getRegionalSummary(String regionCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional summary for: {} period: {} tenant: {}", regionCode, period, tenantId);

        List<CountryHeadcount> headcounts = countryHeadcountRepository.findByTenantIdAndRegionCodeAndPeriod(
                tenantId, regionCode, period);

        int totalHeadcount = headcounts.stream()
                .mapToInt(h -> h.getTotalHeadcount() != null ? h.getTotalHeadcount() : 0)
                .sum();

        List<ComplianceMetric> compliance = complianceMetricRepository.findByTenantIdAndRegionCode(tenantId, regionCode)
                .stream()
                .filter(m -> period.equals(m.getPeriod()))
                .collect(Collectors.toList());

        List<RetentionMetric> retention = retentionMetricRepository.findByTenantIdAndRegionCode(tenantId, regionCode)
                .stream()
                .filter(m -> period.equals(m.getPeriod()))
                .collect(Collectors.toList());

        Double avgComplianceScore = compliance.stream()
                .mapToDouble(ComplianceMetric::getComplianceScore)
                .average()
                .orElse(0.0);

        Double avgRetentionRate = retention.stream()
                .mapToDouble(RetentionMetric::getRetentionRate)
                .average()
                .orElse(0.0);

        return new RegionalSummaryDashboard(
                regionCode,
                period,
                totalHeadcount,
                headcounts.size(),
                avgComplianceScore,
                avgRetentionRate
        );
    }

    /**
     * Get all periods available
     */
    public List<String> getAvailablePeriods() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching available periods for tenant: {}", tenantId);

        Set<String> periods = new HashSet<>();

        periods.addAll(globalWorkforceMetricRepository.findByTenantId(tenantId).stream()
                .map(GlobalWorkforceMetric::getPeriod).collect(Collectors.toSet()));

        periods.addAll(countryHeadcountRepository.findByTenantId(tenantId).stream()
                .map(CountryHeadcount::getPeriod).collect(Collectors.toSet()));

        return periods.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * Get latest period
     */
    public String getLatestPeriod() {
        List<String> periods = getAvailablePeriods();
        return periods.isEmpty() ? getCurrentPeriod() : periods.get(0);
    }

    /**
     * Get metrics updated since
     */
    public Instant getLastUpdateTimestamp() {
        String tenantId = RequestContextHolder.getTenantId();
        List<GlobalWorkforceMetric> metrics = globalWorkforceMetricRepository.findLatestByTenantId(tenantId, 1);
        return metrics.isEmpty() ? Instant.now() : metrics.get(0).getUpdatedAt();
    }

    private List<RegionalSummary> getRegionalSummaries(String tenantId, String period) {
        List<RegionalMetric> regionalMetrics = regionalMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        Map<String, List<RegionalMetric>> byRegion = regionalMetrics.stream()
                .collect(Collectors.groupingBy(RegionalMetric::getRegionCode));

        List<RegionalSummary> summaries = new ArrayList<>();

        for (Map.Entry<String, List<RegionalMetric>> entry : byRegion.entrySet()) {
            String regionCode = entry.getKey();
            List<RegionalMetric> metrics = entry.getValue();

            int totalCountries = metrics.stream()
                    .mapToInt(m -> m.getCountries() != null ? m.getCountries().size() : 0)
                    .sum();

            summaries.add(new RegionalSummary(
                    regionCode,
                    totalCountries,
                    metrics.size()
            ));
        }

        return summaries;
    }

    private Long calculateYoYChange(String tenantId, String currentPeriod) {
        try {
            YearMonth current = YearMonth.parse(currentPeriod, DateTimeFormatter.ofPattern("yyyy-MM"));
            YearMonth previous = current.minusYears(1);
            String previousPeriod = previous.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            Integer currentTotal = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, currentPeriod);
            Integer previousTotal = countryHeadcountRepository.sumTotalHeadcountByTenantIdAndPeriod(tenantId, previousPeriod);

            if (currentTotal != null && previousTotal != null) {
                return (long) currentTotal - previousTotal;
            }
        } catch (Exception e) {
            log.warn("Could not calculate YoY change for period: {}", currentPeriod, e);
        }
        return 0L;
    }

    private Map<String, Integer> calculateGlobalGenderDistribution(List<DiversityMetric> metrics) {
        Map<String, Integer> global = new HashMap<>();

        for (DiversityMetric metric : metrics) {
            if (metric.getGenderDistribution() != null) {
                for (Map.Entry<String, Integer> entry : metric.getGenderDistribution().entrySet()) {
                    global.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }

        return global;
    }

    private Map<String, Integer> calculateGlobalNationalityDistribution(List<DiversityMetric> metrics) {
        Map<String, Integer> global = new HashMap<>();

        for (DiversityMetric metric : metrics) {
            if (metric.getNationalityDistribution() != null) {
                for (Map.Entry<String, Integer> entry : metric.getNationalityDistribution().entrySet()) {
                    global.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }

        return global;
    }

    private String getCurrentPeriod() {
        return YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    /**
     * CHRO Dashboard View
     */
    public record CHRODashboardView(
            String period,
            int globalHeadcount,
            double averageRetentionRate,
            double averageComplianceScore,
            double averageDiversityScore,
            long atRiskCountries,
            List<GlobalWorkforceMetric> keyMetrics
    ) {}

    /**
     * CEO Dashboard View
     */
    public record CEODashboardView(
            String period,
            int totalHeadcount,
            int totalCountries,
            int totalRegions,
            double averageRetentionRate,
            long yearOverYearChange,
            List<RegionalSummary> regionalSummaries
    ) {}

    /**
     * Compliance Dashboard View
     */
    public record ComplianceDashboardView(
            String period,
            long totalCountries,
            long compliantCount,
            long atRiskCount,
            long nonCompliantCount,
            double averageScore,
            ComplianceStatus globalStatus,
            List<ComplianceMetric> atRiskCountries,
            List<ComplianceMetric> nonCompliantCountries,
            List<ComplianceMetric> withCriticalIssues
    ) {}

    /**
     * Diversity Dashboard View
     */
    public record DiversityDashboardView(
            String period,
            int totalEmployees,
            double averageGenderDiversityScore,
            double averageNationalDiversityScore,
            double averageOverallDiversityScore,
            Map<String, Integer> globalGenderDistribution,
            Map<String, Integer> globalNationalityDistribution,
            List<DiversityMetric> topPerformingCountries
    ) {}

    /**
     * Country Dashboard View
     */
    public record CountryDashboardView(
            String countryCode,
            String period,
            CountryHeadcount headcount,
            ComplianceMetric compliance,
            DiversityMetric diversity,
            RetentionMetric retention
    ) {}

    /**
     * Regional Summary Dashboard
     */
    public record RegionalSummaryDashboard(
            String regionCode,
            String period,
            int totalHeadcount,
            int totalCountries,
            double averageComplianceScore,
            double averageRetentionRate
    ) {}

    /**
     * Regional Summary
     */
    public record RegionalSummary(
            String regionCode,
            int totalCountries,
            int totalMetrics
    ) {}
}
