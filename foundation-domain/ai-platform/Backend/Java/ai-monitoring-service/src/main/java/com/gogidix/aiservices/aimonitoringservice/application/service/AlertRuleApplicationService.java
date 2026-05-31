package com.gogidix.aiservices.aimonitoringservice.application.service;

import com.gogidix.aiservices.aimonitoringservice.application.dto.AlertRuleResponseDto;
import com.gogidix.aiservices.aimonitoringservice.application.dto.CreateAlertRuleRequestDto;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRule;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ConditionType;
import com.gogidix.aiservices.aimonitoringservice.domain.repository.AlertRuleRepository;
import com.gogidix.aiservices.aimonitoringservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException;

import java.util.List;

/**
 * Application service for managing alert rules.
 */
public class AlertRuleApplicationService {

    private final AlertRuleRepository repository;

    public AlertRuleApplicationService(AlertRuleRepository repository) {
        this.repository = repository;
    }

    public AlertRuleResponseDto createRule(String tenantId, CreateAlertRuleRequestDto request) {
        AlertRule rule = new AlertRule(
                tenantId,
                request.name(),
                request.metric(),
                request.condition(),
                request.threshold()
        );

        if (request.notificationChannels() != null) {
            request.notificationChannels().forEach(rule::addNotificationChannel);
        }

        if (request.minAlertInterval() != null) {
            rule.updateMinAlertInterval(request.minAlertInterval());
        }

        rule.validate();
        AlertRule saved = repository.save(rule);

        return AlertRuleResponseDto.from(saved);
    }

    public AlertRuleResponseDto getRuleById(String alertId, String tenantId) {
        AlertRule rule = repository.findByAlertIdAndTenantId(alertId, tenantId)
                .orElseThrow(() -> new NotFoundException("AlertRule", alertId));
        return AlertRuleResponseDto.from(rule);
    }

    public List<AlertRuleResponseDto> getRulesByTenant(String tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(AlertRuleResponseDto::from)
                .toList();
    }

    public void deleteRule(String alertId, String tenantId) {
        if (!repository.findByAlertIdAndTenantId(alertId, tenantId).isPresent()) {
            throw new NotFoundException("AlertRule", alertId);
        }
        repository.deleteByAlertIdAndTenantId(alertId, tenantId);
    }

    public void activateRule(String alertId, String tenantId) {
        AlertRule rule = repository.findByAlertIdAndTenantId(alertId, tenantId)
                .orElseThrow(() -> new NotFoundException("AlertRule", alertId));
        rule.activate();
        repository.save(rule);
    }

    public void deactivateRule(String alertId, String tenantId) {
        AlertRule rule = repository.findByAlertIdAndTenantId(alertId, tenantId)
                .orElseThrow(() -> new NotFoundException("AlertRule", alertId));
        rule.deactivate();
        repository.save(rule);
    }

    public void triggerAlert(String alertId, String tenantId) {
        AlertRule rule = repository.findByAlertIdAndTenantId(alertId, tenantId)
                .orElseThrow(() -> new NotFoundException("AlertRule", alertId));
        if (rule.canTrigger()) {
            rule.recordTrigger();
            repository.save(rule);
            // Send notifications here
        }
    }
}
