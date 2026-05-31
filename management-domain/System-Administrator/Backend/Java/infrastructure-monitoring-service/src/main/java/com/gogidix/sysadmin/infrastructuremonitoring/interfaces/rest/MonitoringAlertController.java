package com.gogidix.sysadmin.infrastructuremonitoring.interfaces.rest;

import com.gogidix.sysadmin.infrastructuremonitoring.application.dto.MonitoringAlertDTO;
import com.gogidix.sysadmin.infrastructuremonitoring.application.service.MonitoringAlertService;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.MonitoringAlert;
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
@RequestMapping("/api/monitoring-alerts")
public class MonitoringAlertController {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringAlertController.class);

    private final MonitoringAlertService alertService;

    public MonitoringAlertController(MonitoringAlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoringAlertDTO> getById(@PathVariable String id) {
        logger.info("GET /api/monitoring-alerts/{}", id);
        return ResponseEntity.ok(alertService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<MonitoringAlertDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        logger.info("GET /api/monitoring-alerts?page={}&size={}", page, size);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(alertService.getAll(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MonitoringAlertDTO>> getByStatus(@PathVariable MonitoringAlert.AlertStatus status) {
        logger.info("GET /api/monitoring-alerts/status/{}", status);
        return ResponseEntity.ok(alertService.getByStatus(status));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<MonitoringAlertDTO>> getBySeverity(@PathVariable MonitoringAlert.AlertSeverity severity) {
        logger.info("GET /api/monitoring-alerts/severity/{}", severity);
        return ResponseEntity.ok(alertService.getBySeverity(severity));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    public ResponseEntity<List<MonitoringAlertDTO>> getByInfrastructure(@PathVariable String infrastructureId) {
        logger.info("GET /api/monitoring-alerts/infrastructure/{}", infrastructureId);
        return ResponseEntity.ok(alertService.getByInfrastructure(infrastructureId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<MonitoringAlertDTO>> getActiveAlerts() {
        logger.info("GET /api/monitoring-alerts/active");
        return ResponseEntity.ok(alertService.getActiveAlerts());
    }

    @PostMapping
    public ResponseEntity<MonitoringAlertDTO> create(@Valid @RequestBody MonitoringAlertDTO dto) {
        logger.info("POST /api/monitoring-alerts - Creating: {}", dto.getTitle());
        MonitoringAlertDTO created = alertService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<MonitoringAlertDTO> acknowledge(
            @PathVariable String id,
            @RequestParam String acknowledgedBy) {
        logger.info("POST /api/monitoring-alerts/{}/acknowledge by {}", id, acknowledgedBy);
        return ResponseEntity.ok(alertService.acknowledge(id, acknowledgedBy));
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<MonitoringAlertDTO> resolve(
            @PathVariable String id,
            @RequestParam String resolvedBy,
            @RequestParam(required = false) String resolutionNotes) {
        logger.info("POST /api/monitoring-alerts/{}/resolve by {}", id, resolvedBy);
        return ResponseEntity.ok(alertService.resolve(id, resolvedBy, resolutionNotes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("DELETE /api/monitoring-alerts/{}", id);
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cleanup")
    public ResponseEntity<Void> cleanup(@RequestParam(defaultValue = "30") int daysToKeep) {
        RequestContext context = RequestContextHolder.require();
        logger.info("POST /api/monitoring-alerts/cleanup?daysToKeep={} for tenant: {}", daysToKeep, context.tenantId());
        alertService.cleanupOldResolvedAlerts(daysToKeep);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<AlertStats> getStats() {
        RequestContext context = RequestContextHolder.require();
        logger.info("GET /api/monitoring-alerts/stats/summary for tenant: {}", context.tenantId());

        AlertStats stats = new AlertStats();
        stats.total = alertService.getTotalCount();
        stats.open = alertService.getCountByStatus(MonitoringAlert.AlertStatus.OPEN);
        stats.acknowledged = alertService.getCountByStatus(MonitoringAlert.AlertStatus.ACKNOWLEDGED);
        stats.resolved = alertService.getCountByStatus(MonitoringAlert.AlertStatus.RESOLVED);
        stats.critical = alertService.getCountBySeverityAndStatus(
                MonitoringAlert.AlertSeverity.CRITICAL, MonitoringAlert.AlertStatus.OPEN);
        stats.high = alertService.getCountBySeverityAndStatus(
                MonitoringAlert.AlertSeverity.HIGH, MonitoringAlert.AlertStatus.OPEN);

        return ResponseEntity.ok(stats);
    }

    public static class AlertStats {
        public long total;
        public long open;
        public long acknowledged;
        public long resolved;
        public long critical;
        public long high;

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }

        public long getOpen() { return open; }
        public void setOpen(long open) { this.open = open; }

        public long getAcknowledged() { return acknowledged; }
        public void setAcknowledged(long acknowledged) { this.acknowledged = acknowledged; }

        public long getResolved() { return resolved; }
        public void setResolved(long resolved) { this.resolved = resolved; }

        public long getCritical() { return critical; }
        public void setCritical(long critical) { this.critical = critical; }

        public long getHigh() { return high; }
        public void setHigh(long high) { this.high = high; }
    }
}
