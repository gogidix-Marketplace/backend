package com.gogidix.dashboard.aggregation.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.dashboard.aggregation.domain.model.AggregationRequest;
import com.gogidix.dashboard.aggregation.domain.model.AggregationStatus;
import com.gogidix.dashboard.aggregation.domain.model.CachedAggregationResult;
import com.gogidix.dashboard.aggregation.domain.port.in.CreateAggregationCommand;
import com.gogidix.dashboard.aggregation.infrastructure.feign.CourierServiceClient;
import com.gogidix.dashboard.aggregation.infrastructure.feign.WarehouseServiceClient;
import com.gogidix.dashboard.aggregation.infrastructure.feign.SocialCommerceClient;
import com.gogidix.dashboard.shared.exception.DashboardException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Service for cross-service data aggregation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationService {

    private final CourierServiceClient courierServiceClient;
    private final WarehouseServiceClient warehouseServiceClient;
    private final SocialCommerceClient socialCommerceClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CACHE_PREFIX = "aggregation:";
    private static final long CACHE_TTL_MINUTES = 5;

    /**
     * Create and process an aggregation request
     */
    @Transactional
    public AggregationRequest createAndProcessAggregation(CreateAggregationCommand command) {
        log.info("Creating aggregation request: name={}, tenant={}", command.getName(), command.getTenantId());

        AggregationRequest request = AggregationRequest.builder()
                .name(command.getName())
                .description(command.getDescription())
                .tenantId(command.getTenantId())
                .sourceDomains(String.join(",", command.getSourceDomains()))
                .kpiCodes(command.getKpiCodes() != null ? String.join(",", command.getKpiCodes()) : null)
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .aggregationType(command.getAggregationType())
                .groupBy(command.getGroupBy())
                .filters(command.getFilters())
                .createdBy(command.getCreatedBy())
                .build();

        // Process the aggregation asynchronously
        processAggregationAsync(request);

        return request;
    }

    /**
     * Get aggregated data across multiple domains
     */
    public Map<String, Object> getAggregatedData(String tenantId, List<String> domains,
                                                  LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting aggregated data: tenant={}, domains={}", tenantId, domains);

        String cacheKey = generateCacheKey(tenantId, domains, startDate, endDate);

        // Try cache first
        Map<String, Object> cached = getCachedResult(cacheKey);
        if (cached != null) {
            log.debug("Returning cached aggregation result");
            return cached;
        }

        // Aggregate from source services
        Map<String, Object> result = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String domain : domains) {
            switch (domain) {
                case "COURIER_SERVICE":
                    futures.add(CompletableFuture.runAsync(() -> {
                        try {
                            var data = courierServiceClient.getAggregatedData(tenantId, startDate, endDate);
                            result.put("courierService", data);
                        } catch (Exception e) {
                            log.error("Error fetching courier service data", e);
                            result.put("courierService", Map.of("error", e.getMessage()));
                        }
                    }));
                    break;

                case "WAREHOUSE":
                    futures.add(CompletableFuture.runAsync(() -> {
                        try {
                            var data = warehouseServiceClient.getAggregatedData(tenantId, startDate, endDate);
                            result.put("warehouse", data);
                        } catch (Exception e) {
                            log.error("Error fetching warehouse data", e);
                            result.put("warehouse", Map.of("error", e.getMessage()));
                        }
                    }));
                    break;

                case "SOCIAL_COMMERCE":
                    futures.add(CompletableFuture.runAsync(() -> {
                        try {
                            var data = socialCommerceClient.getAggregatedData(tenantId, startDate, endDate);
                            result.put("socialCommerce", data);
                        } catch (Exception e) {
                            log.error("Error fetching social commerce data", e);
                            result.put("socialCommerce", Map.of("error", e.getMessage()));
                        }
                    }));
                    break;
            }
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Add summary
        result.put("summary", createSummary(result));
        result.put("aggregationMetadata", Map.of(
                "tenantId", tenantId,
                "domains", domains,
                "startDate", startDate,
                "endDate", endDate,
                "generatedAt", LocalDateTime.now()
        ));

        // Cache the result
        cacheResult(cacheKey, result);

        return result;
    }

    /**
     * Get cross-domain summary
     */
    public Map<String, Object> getCrossDomainSummary(String tenantId) {
        log.info("Getting cross-domain summary for tenant: {}", tenantId);

        String cacheKey = CACHE_PREFIX + "summary:" + tenantId;

        // Try cache first
        Map<String, Object> cached = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        Map<String, Object> summary = new HashMap<>();

        try {
            summary.put("courierService", courierServiceClient.getSummary(tenantId));
        } catch (Exception e) {
            log.error("Error fetching courier service summary", e);
            summary.put("courierService", Map.of("error", e.getMessage()));
        }

        try {
            summary.put("warehouse", warehouseServiceClient.getSummary(tenantId));
        } catch (Exception e) {
            log.error("Error fetching warehouse summary", e);
            summary.put("warehouse", Map.of("error", e.getMessage()));
        }

        try {
            summary.put("socialCommerce", socialCommerceClient.getSummary(tenantId));
        } catch (Exception e) {
            log.error("Error fetching social commerce summary", e);
            summary.put("socialCommerce", Map.of("error", e.getMessage()));
        }

        summary.put("generatedAt", LocalDateTime.now());

        // Cache for 2 minutes
        redisTemplate.opsForValue().set(cacheKey, summary, 2, TimeUnit.MINUTES);

        return summary;
    }

    /**
     * Get real-time metrics across domains
     */
    public Map<String, Object> getRealTimeMetrics(String tenantId, List<String> domains) {
        log.info("Getting real-time metrics: tenant={}, domains={}", tenantId, domains);

        Map<String, Object> metrics = new HashMap<>();

        for (String domain : domains) {
            try {
                switch (domain) {
                    case "COURIER_SERVICE":
                        metrics.put("courierService", courierServiceClient.getRealTimeMetrics(tenantId));
                        break;
                    case "WAREHOUSE":
                        metrics.put("warehouse", warehouseServiceClient.getRealTimeMetrics(tenantId));
                        break;
                    case "SOCIAL_COMMERCE":
                        metrics.put("socialCommerce", socialCommerceClient.getRealTimeMetrics(tenantId));
                        break;
                }
            } catch (Exception e) {
                log.error("Error fetching real-time metrics for {}", domain, e);
                metrics.put(domain.toLowerCase(), Map.of("error", e.getMessage()));
            }
        }

        metrics.put("timestamp", LocalDateTime.now());

        return metrics;
    }

    private void processAggregationAsync(AggregationRequest request) {
        CompletableFuture.runAsync(() -> {
            try {
                request.markAsProcessing();

                // Simulate processing
                Thread.sleep(1000);

                Map<String, Object> result = getAggregatedData(
                        request.getTenantId(),
                        Arrays.asList(request.getSourceDomains().split(",")),
                        request.getStartDate(),
                        request.getEndDate()
                );

                request.markAsCompleted(objectMapper.writeValueAsString(result));

            } catch (Exception e) {
                log.error("Aggregation failed", e);
                request.markAsFailed(e.getMessage());
            }
        });
    }

    private String generateCacheKey(String tenantId, List<String> domains,
                                    LocalDateTime startDate, LocalDateTime endDate) {
        return String.format("%s%s:%s:%s:%s",
                CACHE_PREFIX,
                tenantId,
                String.join("-", domains),
                startDate != null ? startDate.toString() : "all",
                endDate != null ? endDate.toString() : "now"
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCachedResult(String cacheKey) {
        try {
            return (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception e) {
            log.warn("Error reading from cache", e);
            return null;
        }
    }

    private void cacheResult(String cacheKey, Map<String, Object> result) {
        try {
            redisTemplate.opsForValue().set(cacheKey, result, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Error writing to cache", e);
        }
    }

    private Map<String, Object> createSummary(Map<String, Object> data) {
        Map<String, Object> summary = new HashMap<>();
        int successCount = 0;
        int errorCount = 0;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<?, ?> valueMap = (Map<?, ?>) entry.getValue();
                if (valueMap.containsKey("error")) {
                    errorCount++;
                } else {
                    successCount++;
                }
            }
        }

        summary.put("totalSources", data.size());
        summary.put("successfulSources", successCount);
        summary.put("failedSources", errorCount);
        summary.put("overallStatus", errorCount == 0 ? "SUCCESS" : "PARTIAL");

        return summary;
    }
}
