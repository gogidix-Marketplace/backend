package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.RollupResponseDto;
import com.gogidix.sales.dashboard.domain.model.MetricRollup;
import com.gogidix.sales.dashboard.domain.repository.MetricRollupRepository;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Rollup Query Service
 * Handles all read operations for metric rollups
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RollupQueryService {

    private final MetricRollupRepository rollupRepository;

    public List<RollupResponseDto> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all rollups for tenant: {}", tenantId);

        List<MetricRollup> rollups = rollupRepository.findByTenantId(tenantId);
        return rollups.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "rollups", key = "#rollupId")
    public RollupResponseDto getById(String rollupId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching rollup: {} for tenant: {}", rollupId, tenantId);

        MetricRollup rollup = rollupRepository.findByRollupIdAndTenantId(rollupId, tenantId)
                .orElseThrow(() -> new NotFoundException("Rollup", rollupId));

        return toDto(rollup);
    }

    public List<RollupResponseDto> getByType(MetricRollup.RollupType type) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching rollups by type: {} for tenant: {}", type, tenantId);

        List<MetricRollup> rollups = rollupRepository.findByTenantIdAndRollupType(tenantId, type);
        return rollups.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RollupResponseDto> getByKey(String key) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching rollups by key: {} for tenant: {}", key, tenantId);

        List<MetricRollup> rollups = rollupRepository.findByTenantIdAndRollupKey(tenantId, key);
        return rollups.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RollupResponseDto getGlobalRollup() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching global rollup for tenant: {}", tenantId);

        List<MetricRollup> globalRollups = rollupRepository.findGlobalRollupByTenantId(tenantId);

        return globalRollups.stream()
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Global Rollup", "N/A"));
    }

    public List<RollupResponseDto> getRegionalRollups() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching regional rollups for tenant: {}", tenantId);

        List<MetricRollup> regionalRollups = rollupRepository.findRegionalRollupsByTenantId(tenantId);
        return regionalRollups.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RollupResponseDto> getChildRollups(String parentRollupId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching child rollups of parent: {} for tenant: {}", parentRollupId, tenantId);

        List<MetricRollup> childRollups = rollupRepository.findChildRollups(parentRollupId, tenantId);
        return childRollups.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getPerformanceSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Getting performance summary for tenant: {}", tenantId);

        List<MetricRollup> rollups = rollupRepository.findByTenantId(tenantId);

        Map<String, Object> summary = new HashMap<>();

        BigDecimal totalRevenue = rollups.stream()
                .filter(r -> r.getMetrics() != null && r.getMetrics().getRevenue() != null)
                .map(r -> r.getMetrics().getRevenue().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDeals = rollups.stream()
                .filter(r -> r.getMetrics() != null)
                .mapToInt(r -> r.getMetrics().getDeals() != null ? r.getMetrics().getDeals() : 0)
                .sum();

        BigDecimal avgWinRate = rollups.stream()
                .filter(r -> r.getMetrics() != null && r.getMetrics().getWinRate() != null)
                .map(r -> r.getMetrics().getWinRate())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(Math.max(1, rollups.size())), 2, java.math.RoundingMode.HALF_UP);

        long onTrackCount = rollups.stream()
                .filter(r -> r.isOverallOnTrack())
                .count();

        summary.put("totalRollups", rollups.size());
        summary.put("totalRevenue", totalRevenue);
        summary.put("totalDeals", totalDeals);
        summary.put("averageWinRate", avgWinRate);
        summary.put("onTrackCount", onTrackCount);
        summary.put("onTrackPercentage", rollups.isEmpty() ? 0 :
                (onTrackCount * 100.0 / rollups.size()));

        return summary;
    }

    public Map<String, Object> compareRollups(List<String> rollupIds, String metric) {
        log.debug("Comparing rollups: {} by metric: {}", rollupIds, metric);

        Map<String, Object> comparison = new HashMap<>();

        for (String rollupId : rollupIds) {
            MetricRollup rollup = rollupRepository.findByRollupIdAndTenantId(rollupId, RequestContextHolder.getTenantId())
                    .orElse(null);

            if (rollup != null && rollup.getMetrics() != null) {
                Map<String, Object> rollupData = new HashMap<>();
                rollupData.put("name", rollup.getRollupName());
                rollupData.put("type", rollup.getRollupType());

                switch (metric.toLowerCase()) {
                    case "revenue":
                        rollupData.put("value", rollup.getMetrics().getRevenue());
                        break;
                    case "deals":
                        rollupData.put("value", rollup.getMetrics().getDeals());
                        break;
                    case "winrate":
                        rollupData.put("value", rollup.getMetrics().getWinRate());
                        break;
                    case "pipeline":
                        rollupData.put("value", rollup.getMetrics().getPipelineValue());
                        break;
                    default:
                        rollupData.put("value", rollup.getMetrics().getRevenue());
                }

                comparison.put(rollupId, rollupData);
            }
        }

        return comparison;
    }

    public List<RollupResponseDto> getLeaderboard(String metric, int limit, MetricRollup.RollupType type) {
        log.debug("Getting leaderboard for metric: {}, limit: {}, type: {}", metric, limit, type);

        List<MetricRollup> rollups = type != null ?
                rollupRepository.findByTenantIdAndRollupType(RequestContextHolder.getTenantId(), type) :
                rollupRepository.findByTenantId(RequestContextHolder.getTenantId());

        Comparator<MetricRollup> comparator = getComparatorForMetric(metric);

        return rollups.stream()
                .sorted(comparator.reversed())
                .limit(limit)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RollupResponseDto toDto(MetricRollup rollup) {
        return RollupResponseDto.builder()
                .id(rollup.getId())
                .rollupId(rollup.getRollupId())
                .tenantId(rollup.getTenantId())
                .rollupType(rollup.getRollupType().name())
                .rollupKey(rollup.getRollupKey())
                .rollupName(rollup.getRollupName())
                .startDate(rollup.getTimePeriod().getStartDate())
                .endDate(rollup.getTimePeriod().getEndDate())
                .periodType(rollup.getTimePeriod().getPeriodType())
                .revenue(toMoneyDto(rollup.getMetrics() != null ? rollup.getMetrics().getRevenue() : null))
                .deals(rollup.getMetrics() != null ? rollup.getMetrics().getDeals() : 0)
                .winRate(rollup.getMetrics() != null ? toDouble(rollup.getMetrics().getWinRate()) : null)
                .pipelineValue(toMoneyDto(rollup.getMetrics() != null ? rollup.getMetrics().getPipelineValue() : null))
                .opportunities(rollup.getMetrics() != null ? rollup.getMetrics().getOpportunities() : 0)
                .averageDealSize(toMoneyDto(rollup.getMetrics() != null ? rollup.getMetrics().getAverageDealSize() : null))
                .growthRate(rollup.getMetrics() != null ? toDouble(rollup.getMetrics().getGrowthRate()) : null)
                .overallStatus(rollup.getPerformance() != null ? rollup.getPerformance().getOverallStatus() : null)
                .score(rollup.getPerformance() != null ? toDouble(rollup.getPerformance().getScore()) : null)
                .trend(rollup.getPerformance() != null ? rollup.getPerformance().getTrend() : null)
                .riskLevel(rollup.getPerformance() != null ? rollup.getPerformance().getRiskLevel() : null)
                .isRealtime(rollup.getIsRealtime())
                .rollupTime(rollup.getRollupTime())
                .createdAt(rollup.getCreatedAt())
                .updatedAt(rollup.getUpdatedAt())
                .build();
    }

    private RollupResponseDto.MoneyDto toMoneyDto(MetricRollup.Money money) {
        if (money == null) {
            return null;
        }
        return RollupResponseDto.MoneyDto.builder()
                .amount(toDouble(money.getAmount()))
                .currency(money.getCurrency())
                .build();
    }

    private Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }

    private Comparator<MetricRollup> getComparatorForMetric(String metric) {
        return switch (metric.toLowerCase()) {
            case "revenue" -> Comparator.comparing(
                    r -> r.getMetrics() != null && r.getMetrics().getRevenue() != null ?
                            r.getMetrics().getRevenue().getAmount() : BigDecimal.ZERO,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            case "deals" -> Comparator.comparing(
                    r -> r.getMetrics() != null ? r.getMetrics().getDeals() : 0,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            case "winrate" -> Comparator.comparing(
                    r -> r.getMetrics() != null ? r.getMetrics().getWinRate() : BigDecimal.ZERO,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            case "pipeline" -> Comparator.comparing(
                    r -> r.getMetrics() != null && r.getMetrics().getPipelineValue() != null ?
                            r.getMetrics().getPipelineValue().getAmount() : BigDecimal.ZERO,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            default -> Comparator.comparing(
                    r -> r.getMetrics() != null && r.getMetrics().getRevenue() != null ?
                            r.getMetrics().getRevenue().getAmount() : BigDecimal.ZERO,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
        };
    }
}
