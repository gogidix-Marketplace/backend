package com.gogidix.sysadmin.infrastructuremonitoring.interfaces.rest;

import com.gogidix.sysadmin.infrastructuremonitoring.application.dto.InfrastructureMonitoringDTO;
import com.gogidix.sysadmin.infrastructuremonitoring.application.service.InfrastructureMonitoringService;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.InfrastructureMonitoring;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContext;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContextHolder;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/infrastructure-monitoring")
public class InfrastructureMonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(InfrastructureMonitoringController.class);

    private final InfrastructureMonitoringService monitoringService;

    public InfrastructureMonitoringController(InfrastructureMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfrastructureMonitoringDTO> getById(@PathVariable String id) {
        logger.info("GET /api/infrastructure-monitoring/{}", id);
        return ResponseEntity.ok(monitoringService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<InfrastructureMonitoringDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        logger.info("GET /api/infrastructure-monitoring?page={}&size={}", page, size);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(monitoringService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InfrastructureMonitoringDTO>> getByStatus(@PathVariable InfrastructureMonitoring.MonitoringStatus status) {
        logger.info("GET /api/infrastructure-monitoring/status/{}", status);
        return ResponseEntity.ok(monitoringService.getByStatus(status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<InfrastructureMonitoringDTO>> getByType(@PathVariable InfrastructureMonitoring.InfrastructureType type) {
        logger.info("GET /api/infrastructure-monitoring/type/{}", type);
        return ResponseEntity.ok(monitoringService.getByType(type));
    }

    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<InfrastructureMonitoringDTO>> getByEnvironment(@PathVariable String environment) {
        logger.info("GET /api/infrastructure-monitoring/environment/{}", environment);
        return ResponseEntity.ok(monitoringService.getByEnvironment(environment));
    }

    @GetMapping("/search")
    public ResponseEntity<List<InfrastructureMonitoringDTO>> search(@RequestParam String q) {
        logger.info("GET /api/infrastructure-monitoring/search?q={}", q);
        return ResponseEntity.ok(monitoringService.search(q));
    }

    @GetMapping("/stale")
    public ResponseEntity<List<InfrastructureMonitoringDTO>> getStale(
            @RequestParam(defaultValue = "5") int thresholdMinutes) {
        logger.info("GET /api/infrastructure-monitoring/stale?thresholdMinutes={}", thresholdMinutes);
        return ResponseEntity.ok(monitoringService.getStaleMonitors(thresholdMinutes));
    }

    @PostMapping
    public ResponseEntity<InfrastructureMonitoringDTO> create(@Valid @RequestBody InfrastructureMonitoringDTO dto) {
        logger.info("POST /api/infrastructure-monitoring - Creating: {}", dto.getName());
        InfrastructureMonitoringDTO created = monitoringService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InfrastructureMonitoringDTO> update(
            @PathVariable String id,
            @Valid @RequestBody InfrastructureMonitoringDTO dto) {
        logger.info("PUT /api/infrastructure-monitoring/{} - Updating", id);
        return ResponseEntity.ok(monitoringService.update(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InfrastructureMonitoringDTO> updateStatus(
            @PathVariable String id,
            @RequestParam InfrastructureMonitoring.MonitoringStatus status) {
        logger.info("PATCH /api/infrastructure-monitoring/{}/status - Setting status to {}", id, status);
        return ResponseEntity.ok(monitoringService.updateStatus(id, status));
    }

    @PostMapping("/{id}/metrics")
    public ResponseEntity<InfrastructureMonitoringDTO> addMetric(
            @PathVariable String id,
            @RequestBody InfrastructureMonitoring.MetricSnapshot metric) {
        logger.info("POST /api/infrastructure-monitoring/{}/metrics", id);
        return ResponseEntity.ok(monitoringService.addMetric(id, metric));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("DELETE /api/infrastructure-monitoring/{}", id);
        monitoringService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<MonitoringStats> getStats() {
        RequestContext context = RequestContextHolder.require();
        logger.info("GET /api/infrastructure-monitoring/stats/summary for tenant: {}", context.tenantId());

        MonitoringStats stats = new MonitoringStats();
        stats.total = monitoringService.getTotalCount();
        stats.healthy = monitoringService.getCountByStatus(InfrastructureMonitoring.MonitoringStatus.HEALTHY);
        stats.degraded = monitoringService.getCountByStatus(InfrastructureMonitoring.MonitoringStatus.DEGRADED);
        stats.unhealthy = monitoringService.getCountByStatus(InfrastructureMonitoring.MonitoringStatus.UNHEALTHY);
        stats.unknown = monitoringService.getCountByStatus(InfrastructureMonitoring.MonitoringStatus.UNKNOWN);
        stats.maintenance = monitoringService.getCountByStatus(InfrastructureMonitoring.MonitoringStatus.MAINTENANCE);

        return ResponseEntity.ok(stats);
    }

    public static class MonitoringStats {
        public long total;
        public long healthy;
        public long degraded;
        public long unhealthy;
        public long unknown;
        public long maintenance;

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }

        public long getHealthy() { return healthy; }
        public void setHealthy(long healthy) { this.healthy = healthy; }

        public long getDegraded() { return degraded; }
        public void setDegraded(long degraded) { this.degraded = degraded; }

        public long getUnhealthy() { return unhealthy; }
        public void setUnhealthy(long unhealthy) { this.unhealthy = unhealthy; }

        public long getUnknown() { return unknown; }
        public void setUnknown(long unknown) { this.unknown = unknown; }

        public long getMaintenance() { return maintenance; }
        public void setMaintenance(long maintenance) { this.maintenance = maintenance; }
    }
}
