package com.gogidix.dashboard.aggregation.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Feign client for Courier Service integration.
 */
@FeignClient(name = "courier-service", url = "${courier.service.url:http://localhost:8901}")
public interface CourierServiceClient {

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
