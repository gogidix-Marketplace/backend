package com.gogidix.dashboard.aggregation.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Feign client for Social Commerce Service integration.
 */
@FeignClient(name = "social-commerce-service", url = "${social-commerce.service.url:http://localhost:8903}")
public interface SocialCommerceClient {

    @GetMapping("/api/v1/aggregation")
    Map<String, Object> getAggregatedData(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate
    );

    @GetMapping("/api/v1/summary")
    Map<String, Object> getSummary(@RequestHeader("X-Tenant-ID") String tenantId);

    @GetMapping("/api/v1/metrics/real-time")
    Map<String, Object> getRealTimeMetrics(@RequestHeader("X-Tenant-ID") String tenantId);
}
