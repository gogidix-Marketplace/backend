package com.gogidix.monitoring.performance.interfaces.rest;

import com.gogidix.monitoring.performance.application.service.MetricCollectionService;
import com.gogidix.monitoring.performance.application.service.MetricQueryService;
import com.gogidix.monitoring.performance.domain.model.MetricData;
import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;
import com.gogidix.monitoring.performance.infrastructure.messaging.event.MetricEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * REST controller for metric collection and query.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricCollectionService collectionService;
    private final MetricQueryService queryService;

    @PostMapping
    public ResponseEntity<MetricData> collectMetric(@Valid @RequestBody MetricData metric) {
        return ResponseEntity.ok(collectionService.collectMetric(metric));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<MetricData>> collectMetrics(@Valid @RequestBody List<MetricData> metrics) {
        return ResponseEntity.ok(collectionService.collectMetrics(metrics));
    }

    @GetMapping
    public ResponseEntity<List<MetricData>> queryMetrics(
            @RequestParam String tenantId,
            @RequestParam(required = false) String serviceId,
            @RequestParam(required = false) String metricName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return ResponseEntity.ok(queryService.queryMetrics(tenantId, serviceId, metricName, from, to, null));
    }

    @GetMapping("/aggregate")
    public ResponseEntity<Map<String, Double>> getAggregatedMetrics(
            @RequestParam String tenantId,
            @RequestParam(required = false) String serviceId,
            @RequestParam String metricName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(defaultValue = "avg") String aggregation) {
        return ResponseEntity.ok(queryService.getAggregatedMetrics(
                tenantId, serviceId, metricName, from, to, aggregation));
    }
}
