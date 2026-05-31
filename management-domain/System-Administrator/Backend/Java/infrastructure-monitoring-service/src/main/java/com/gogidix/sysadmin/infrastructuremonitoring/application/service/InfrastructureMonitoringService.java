package com.gogidix.sysadmin.infrastructuremonitoring.application.service;

import com.gogidix.sysadmin.infrastructuremonitoring.application.dto.InfrastructureMonitoringDTO;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.InfrastructureMonitoring;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.MonitoringAlert;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.repository.InfrastructureMonitoringRepository;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.repository.MonitoringAlertRepository;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContext;
import com.gogidix.sysadmin.infrastructuremonitoring.shared.requestcontext.RequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InfrastructureMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(InfrastructureMonitoringService.class);

    private final InfrastructureMonitoringRepository monitoringRepository;
    private final MonitoringAlertRepository alertRepository;

    public InfrastructureMonitoringService(InfrastructureMonitoringRepository monitoringRepository,
                                          MonitoringAlertRepository alertRepository) {
        this.monitoringRepository = monitoringRepository;
        this.alertRepository = alertRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "infrastructureMonitoring", key = "#id")
    public InfrastructureMonitoringDTO getById(String id) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Fetching infrastructure monitoring with id: {} for tenant: {}", id, context.tenantId());

        return monitoringRepository.findByTenantIdAndId(context.tenantId(), id)
                .map(InfrastructureMonitoringDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Infrastructure monitoring not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<InfrastructureMonitoringDTO> getAll(Pageable pageable) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Fetching all infrastructure monitoring for tenant: {}", context.tenantId());

        List<InfrastructureMonitoring> all = monitoringRepository.findByTenantId(context.tenantId());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<InfrastructureMonitoringDTO> content = start < all.size()
                ? all.subList(start, end).stream()
                        .map(InfrastructureMonitoringDTO::fromEntity)
                        .collect(Collectors.toList())
                : List.of();

        return new PageImpl<>(content, pageable, all.size());
    }

    @Transactional(readOnly = true)
    public List<InfrastructureMonitoringDTO> getByStatus(InfrastructureMonitoring.MonitoringStatus status) {
        RequestContext context = RequestContextHolder.require();
        return monitoringRepository.findByTenantIdAndStatus(context.tenantId(), status).stream()
                .map(InfrastructureMonitoringDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InfrastructureMonitoringDTO> getByType(InfrastructureMonitoring.InfrastructureType type) {
        RequestContext context = RequestContextHolder.require();
        return monitoringRepository.findByTenantIdAndType(context.tenantId(), type).stream()
                .map(InfrastructureMonitoringDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InfrastructureMonitoringDTO> getByEnvironment(String environment) {
        RequestContext context = RequestContextHolder.require();
        return monitoringRepository.findByTenantIdAndEnvironment(context.tenantId(), environment).stream()
                .map(InfrastructureMonitoringDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InfrastructureMonitoringDTO> search(String searchTerm) {
        RequestContext context = RequestContextHolder.require();
        return monitoringRepository.searchByTenantId(context.tenantId(), searchTerm).stream()
                .map(InfrastructureMonitoringDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "infrastructureMonitoring", allEntries = true)
    public InfrastructureMonitoringDTO create(InfrastructureMonitoringDTO dto) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Creating infrastructure monitoring: {} for tenant: {}", dto.getName(), context.tenantId());

        validateInfrastructureMonitoring(dto);

        InfrastructureMonitoring entity = dto.toEntity();
        entity.setTenantId(context.tenantId());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setStatus(InfrastructureMonitoring.MonitoringStatus.UNKNOWN);

        InfrastructureMonitoring saved = monitoringRepository.save(entity);
        logger.info("Created infrastructure monitoring with id: {}", saved.getId());

        return InfrastructureMonitoringDTO.fromEntity(saved);
    }

    @CacheEvict(value = "infrastructureMonitoring", key = "#id")
    public InfrastructureMonitoringDTO update(String id, InfrastructureMonitoringDTO dto) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Updating infrastructure monitoring: {} for tenant: {}", id, context.tenantId());

        InfrastructureMonitoring existing = monitoringRepository.findByTenantIdAndId(context.tenantId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Infrastructure monitoring not found with id: " + id));

        validateInfrastructureMonitoring(dto);

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getType() != null) existing.setType(dto.getType());
        if (dto.getHost() != null) existing.setHost(dto.getHost());
        if (dto.getPort() != null) existing.setPort(dto.getPort());
        if (dto.getRegion() != null) existing.setRegion(dto.getRegion());
        if (dto.getEnvironment() != null) existing.setEnvironment(dto.getEnvironment());
        if (dto.getTags() != null) existing.setTags(dto.getTags());

        if (dto.getHealthCheckConfig() != null) {
            InfrastructureMonitoring.HealthCheckConfiguration config = existing.getHealthCheckConfig();
            if (config == null) {
                config = new InfrastructureMonitoring.HealthCheckConfiguration();
            }
            if (dto.getHealthCheckConfig().getProtocol() != null) {
                config.setProtocol(dto.getHealthCheckConfig().getProtocol());
            }
            if (dto.getHealthCheckConfig().getPath() != null) {
                config.setPath(dto.getHealthCheckConfig().getPath());
            }
            if (dto.getHealthCheckConfig().getIntervalSeconds() != null) {
                config.setIntervalSeconds(dto.getHealthCheckConfig().getIntervalSeconds());
            }
            if (dto.getHealthCheckConfig().getTimeoutSeconds() != null) {
                config.setTimeoutSeconds(dto.getHealthCheckConfig().getTimeoutSeconds());
            }
            if (dto.getHealthCheckConfig().getRetryCount() != null) {
                config.setRetryCount(dto.getHealthCheckConfig().getRetryCount());
            }
            existing.setHealthCheckConfig(config);
        }

        existing.setUpdatedAt(Instant.now());

        InfrastructureMonitoring updated = monitoringRepository.save(existing);
        logger.info("Updated infrastructure monitoring: {}", id);

        return InfrastructureMonitoringDTO.fromEntity(updated);
    }

    @CacheEvict(value = "infrastructureMonitoring", key = "#id")
    public void delete(String id) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Deleting infrastructure monitoring: {} for tenant: {}", id, context.tenantId());

        InfrastructureMonitoring existing = monitoringRepository.findByTenantIdAndId(context.tenantId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Infrastructure monitoring not found with id: " + id));

        monitoringRepository.delete(existing);
        logger.info("Deleted infrastructure monitoring: {}", id);
    }

    public InfrastructureMonitoringDTO updateStatus(String id, InfrastructureMonitoring.MonitoringStatus status) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Updating status for infrastructure monitoring: {} to {} for tenant: {}",
                id, status, context.tenantId());

        InfrastructureMonitoring existing = monitoringRepository.findByTenantIdAndId(context.tenantId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Infrastructure monitoring not found with id: " + id));

        InfrastructureMonitoring.MonitoringStatus previousStatus = existing.getStatus();
        existing.updateStatus(status);

        if (status == InfrastructureMonitoring.MonitoringStatus.UNHEALTHY &&
                previousStatus != InfrastructureMonitoring.MonitoringStatus.UNHEALTHY) {
            createAlertForUnhealthyInfrastructure(existing);
        }

        InfrastructureMonitoring updated = monitoringRepository.save(existing);
        return InfrastructureMonitoringDTO.fromEntity(updated);
    }

    public InfrastructureMonitoringDTO addMetric(String id, InfrastructureMonitoring.MetricSnapshot metric) {
        RequestContext context = RequestContextHolder.require();
        logger.info("Adding metric to infrastructure monitoring: {} for tenant: {}", id, context.tenantId());

        InfrastructureMonitoring existing = monitoringRepository.findByTenantIdAndId(context.tenantId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Infrastructure monitoring not found with id: " + id));

        existing.addMetric(metric);
        existing.setUpdatedAt(Instant.now());

        InfrastructureMonitoring updated = monitoringRepository.save(existing);
        return InfrastructureMonitoringDTO.fromEntity(updated);
    }

    @Transactional(readOnly = true)
    public long getTotalCount() {
        RequestContext context = RequestContextHolder.require();
        return monitoringRepository.count();
    }

    @Transactional(readOnly = true)
    public long getCountByStatus(InfrastructureMonitoring.MonitoringStatus status) {
        RequestContext context = RequestContextHolder.require();
        return monitoringRepository.countByTenantIdAndStatus(context.tenantId(), status);
    }

    @Transactional(readOnly = true)
    public List<InfrastructureMonitoringDTO> getStaleMonitors(int staleThresholdMinutes) {
        RequestContext context = RequestContextHolder.require();
        Instant threshold = Instant.now().minusSeconds(staleThresholdMinutes * 60L);
        return monitoringRepository.findByTenantIdAndLastCheckedBefore(context.tenantId(), threshold).stream()
                .map(InfrastructureMonitoringDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateInfrastructureMonitoring(InfrastructureMonitoringDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Type is required");
        }
        if (dto.getHost() == null || dto.getHost().isBlank()) {
            throw new IllegalArgumentException("Host is required");
        }
    }

    private void createAlertForUnhealthyInfrastructure(InfrastructureMonitoring infrastructure) {
        MonitoringAlert alert = MonitoringAlert.builder()
                .tenantId(infrastructure.getTenantId())
                .infrastructureId(infrastructure.getId())
                .infrastructureName(infrastructure.getName())
                .severity(MonitoringAlert.AlertSeverity.HIGH)
                .type(MonitoringAlert.AlertType.SERVICE_DOWN)
                .title("Infrastructure Unhealthy: " + infrastructure.getName())
                .description("Infrastructure " + infrastructure.getName() + " (" + infrastructure.getType() +
                        ") at " + infrastructure.getHost() + " is reporting as UNHEALTHY.")
                .build();

        alertRepository.save(alert);
        logger.warn("Created alert for unhealthy infrastructure: {}", infrastructure.getId());
    }
}
