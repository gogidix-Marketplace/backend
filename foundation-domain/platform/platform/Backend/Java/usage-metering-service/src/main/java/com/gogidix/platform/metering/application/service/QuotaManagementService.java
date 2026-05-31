package com.gogidix.platform.metering.application.service;

import com.gogidix.platform.metering.application.dto.QuotaDefinitionDto;
import com.gogidix.platform.metering.application.dto.QuotaAlertDto;
import com.gogidix.platform.metering.domain.model.QuotaDefinition;
import com.gogidix.platform.metering.domain.model.QuotaAlert;
import com.gogidix.platform.metering.domain.repository.QuotaDefinitionRepository;
import com.gogidix.platform.metering.domain.repository.QuotaAlertRepository;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for quota management operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuotaManagementService {

    private final QuotaDefinitionRepository quotaDefinitionRepository;
    private final QuotaAlertRepository quotaAlertRepository;

    /**
     * Create quota definition
     */
    @Transactional
    @CacheEvict(value = "quotas", allEntries = true)
    public QuotaDefinitionDto createQuotaDefinition(QuotaDefinitionDto dto) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }
        log.info("Creating quota definition: tenantId={}, name={}", tenantId, dto.getQuotaName());

        QuotaDefinition quota = QuotaDefinition.builder()
            .tenantId(tenantId)
            .quotaName(dto.getQuotaName())
            .quotaDisplayName(dto.getQuotaDisplayName())
            .metricName(dto.getMetricName())
            .softLimit(dto.getSoftLimit())
            .hardLimit(dto.getHardLimit())
            .quotaPeriod(dto.getQuotaPeriod() != null ? QuotaDefinition.QuotaPeriod.valueOf(dto.getQuotaPeriod()) : QuotaDefinition.QuotaPeriod.MONTHLY)
            .softLimitAction(dto.getSoftLimitAction() != null ? QuotaDefinition.LimitAction.valueOf(dto.getSoftLimitAction()) : null)
            .hardLimitAction(dto.getHardLimitAction() != null ? QuotaDefinition.LimitAction.valueOf(dto.getHardLimitAction()) : QuotaDefinition.LimitAction.BLOCK)
            .notificationThresholds(dto.getNotificationThresholds() != null ? dto.getNotificationThresholds() : new Integer[]{80, 90, 100})
            .notificationChannels(dto.getNotificationChannels() != null ? dto.getNotificationChannels() : new String[]{"email"})
            .isActive(true)
            .build();

        QuotaDefinition saved = quotaDefinitionRepository.save(quota);
        return toDto(saved);
    }

    /**
     * Get quota definition by ID
     */
    @Cacheable(value = "quotas", key = "#quotaId")
    public QuotaDefinitionDto getQuotaDefinition(String quotaId) {
        QuotaDefinition quota = quotaDefinitionRepository.findById(quotaId)
            .orElseThrow(() -> new NotFoundException("Quota definition not found: " + quotaId));
        return toDto(quota);
    }

    /**
     * Get all quotas for tenant
     */
    public List<QuotaDefinitionDto> getQuotasForTenant() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        return quotaDefinitionRepository.findByTenantId(tenantId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get active alerts for tenant
     */
    public List<QuotaAlertDto> getActiveAlerts() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        return quotaAlertRepository.findByTenantIdAndIsAcknowledgedOrderByCreatedAtDesc(tenantId, false).stream()
            .map(this::toAlertDto)
            .collect(Collectors.toList());
    }

    /**
     * Acknowledge alert
     */
    @Transactional
    public void acknowledgeAlert(String alertId) {
        RequestContext context = RequestContext.get();
        if (context == null || context.getSubject() == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("User ID not found");
        }
        String userId = context.getSubject();

        QuotaAlert alert = quotaAlertRepository.findById(alertId)
            .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        alert.acknowledge(userId);
        quotaAlertRepository.save(alert);

        log.info("Alert acknowledged: alertId={}, acknowledgedBy={}", alertId, userId);
    }

    private QuotaDefinitionDto toDto(QuotaDefinition quota) {
        return QuotaDefinitionDto.builder()
            .id(quota.getId())
            .tenantId(quota.getTenantId())
            .quotaName(quota.getQuotaName())
            .quotaDisplayName(quota.getQuotaDisplayName())
            .metricName(quota.getMetricName())
            .softLimit(quota.getSoftLimit())
            .hardLimit(quota.getHardLimit())
            .quotaPeriod(quota.getQuotaPeriod().name())
            .softLimitAction(quota.getSoftLimitAction() != null ? quota.getSoftLimitAction().name() : null)
            .hardLimitAction(quota.getHardLimitAction().name())
            .notificationThresholds(quota.getNotificationThresholds())
            .notificationChannels(quota.getNotificationChannels())
            .isActive(quota.isActive())
            .createdAt(quota.getCreatedAt())
            .updatedAt(quota.getUpdatedAt())
            .build();
    }

    private QuotaAlertDto toAlertDto(QuotaAlert alert) {
        return QuotaAlertDto.builder()
            .id(alert.getId())
            .tenantId(alert.getTenantId())
            .quotaId(alert.getQuotaId())
            .alertType(alert.getAlertType().name())
            .severity(alert.getSeverity().name())
            .currentUsage(alert.getCurrentUsage())
            .limitValue(alert.getLimitValue())
            .percentage(alert.getPercentage())
            .message(alert.getMessage())
            .recommendedAction(alert.getRecommendedAction())
            .isAcknowledged(alert.isAcknowledged())
            .acknowledgedBy(alert.getAcknowledgedBy())
            .acknowledgedAt(alert.getAcknowledgedAt())
            .notificationSent(alert.isNotificationSent())
            .notificationChannels(alert.getNotificationChannels())
            .periodStart(alert.getPeriodStart())
            .periodEnd(alert.getPeriodEnd())
            .metadata(alert.getMetadata())
            .createdAt(alert.getCreatedAt())
            .build();
    }
}
