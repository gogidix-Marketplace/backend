package com.gogidix.sysadmin.infrastructuremonitoring.application.service;

import com.gogidix.sysadmin.infrastructuremonitoring.application.dto.MonitoringAlertDTO;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.MonitoringAlert;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.repository.MonitoringAlertRepository;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContext;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MonitoringAlertService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringAlertService.class);

    private final MonitoringAlertRepository alertRepository;

    public MonitoringAlertService(MonitoringAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Transactional(readOnly = true)
    public MonitoringAlertDTO getById(String id) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Fetching alert with id: {} for tenant: {}", id, context.tenantId());

        return alertRepository.findById(id)
                .filter(alert -> alert.getTenantId().equals(context.tenantId()))
                .map(MonitoringAlertDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<MonitoringAlertDTO> getAll(Pageable pageable) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Fetching all alerts for tenant: {}", context.tenantId());

        List<MonitoringAlert> all = alertRepository.findByTenantId(context.tenantId());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<MonitoringAlertDTO> content = start < all.size()
                ? all.subList(start, end).stream()
                        .map(MonitoringAlertDTO::fromEntity)
                        .collect(Collectors.toList())
                : List.of();

        return new PageImpl<>(content, pageable, all.size());
    }

    @Transactional(readOnly = true)
    public List<MonitoringAlertDTO> getByStatus(MonitoringAlert.AlertStatus status) {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.findByTenantIdAndStatusOrderByCreatedAtDesc(context.tenantId(), status).stream()
                .map(MonitoringAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MonitoringAlertDTO> getBySeverity(MonitoringAlert.AlertSeverity severity) {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.findByTenantIdAndSeverity(context.tenantId(), severity).stream()
                .map(MonitoringAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MonitoringAlertDTO> getActiveAlerts() {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.findByTenantIdAndSeverityAndStatus(
                context.tenantId(),
                MonitoringAlert.AlertSeverity.CRITICAL,
                MonitoringAlert.AlertStatus.OPEN
        ).stream()
                .map(MonitoringAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MonitoringAlertDTO> getByInfrastructure(String infrastructureId) {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.findByTenantIdAndInfrastructureId(context.tenantId(), infrastructureId).stream()
                .map(MonitoringAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "monitoringAlerts", allEntries = true)
    public MonitoringAlertDTO create(MonitoringAlertDTO dto) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Creating alert: {} for tenant: {}", dto.getTitle(), context.tenantId());

        validateAlert(dto);

        MonitoringAlert entity = dto.toEntity();
        entity.setTenantId(context.tenantId());

        MonitoringAlert saved = alertRepository.save(entity);
        logger.info("Created alert with id: {}", saved.getId());

        return MonitoringAlertDTO.fromEntity(saved);
    }

    @CacheEvict(value = "monitoringAlerts", key = "#id")
    public MonitoringAlertDTO acknowledge(String id, String acknowledgedBy) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Acknowledging alert: {} by {} for tenant: {}", id, acknowledgedBy, context.tenantId());

        MonitoringAlert existing = alertRepository.findById(id)
                .filter(alert -> alert.getTenantId().equals(context.tenantId()))
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with id: " + id));

        existing.acknowledge(acknowledgedBy);

        MonitoringAlert updated = alertRepository.save(existing);
        logger.info("Acknowledged alert: {}", id);

        return MonitoringAlertDTO.fromEntity(updated);
    }

    @CacheEvict(value = "monitoringAlerts", key = "#id")
    public MonitoringAlertDTO resolve(String id, String resolvedBy, String resolutionNotes) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Resolving alert: {} by {} for tenant: {}", id, resolvedBy, context.tenantId());

        MonitoringAlert existing = alertRepository.findById(id)
                .filter(alert -> alert.getTenantId().equals(context.tenantId()))
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with id: " + id));

        existing.resolve(resolvedBy, resolutionNotes);

        MonitoringAlert updated = alertRepository.save(existing);
        logger.info("Resolved alert: {}", id);

        return MonitoringAlertDTO.fromEntity(updated);
    }

    @CacheEvict(value = "monitoringAlerts", key = "#id")
    public void delete(String id) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Deleting alert: {} for tenant: {}", id, context.tenantId());

        MonitoringAlert existing = alertRepository.findById(id)
                .filter(alert -> alert.getTenantId().equals(context.tenantId()))
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with id: " + id));

        alertRepository.delete(existing);
        logger.info("Deleted alert: {}", id);
    }

    @Transactional(readOnly = true)
    public long getTotalCount() {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.count();
    }

    @Transactional(readOnly = true)
    public long getCountByStatus(MonitoringAlert.AlertStatus status) {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.countByTenantIdAndStatus(context.tenantId(), status);
    }

    @Transactional(readOnly = true)
    public long getCountBySeverityAndStatus(MonitoringAlert.AlertSeverity severity,
                                            MonitoringAlert.AlertStatus status) {
        RequestContext context = RequestContextHolder.require();
        return alertRepository.countByTenantIdAndSeverityAndStatus(context.tenantId(), severity, status);
    }

    public void cleanupOldResolvedAlerts(int daysToKeep) {
        RequestContext context = RequestContextHolder.require();
        Instant cutoffDate = Instant.now().minusSeconds(daysToKeep * 86400L);
        logger.info("Cleaning up resolved alerts older than {} days for tenant: {}", daysToKeep, context.tenantId());

        alertRepository.deleteByTenantIdAndResolvedAtBefore(context.tenantId(), cutoffDate);
        logger.info("Cleaned up old resolved alerts");
    }

    private void validateAlert(MonitoringAlertDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (dto.getSeverity() == null) {
            throw new IllegalArgumentException("Severity is required");
        }
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Type is required");
        }
    }
}
