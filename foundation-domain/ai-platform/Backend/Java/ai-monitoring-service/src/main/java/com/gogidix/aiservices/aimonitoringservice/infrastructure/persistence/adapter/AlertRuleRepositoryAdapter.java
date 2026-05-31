package com.gogidix.aiservices.aimonitoringservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRule;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.repository.AlertRuleRepository;
import com.gogidix.aiservices.aimonitoringservice.infrastructure.persistence.document.AlertRuleDocument;
import com.gogidix.aiservices.aimonitoringservice.infrastructure.persistence.repository.SpringDataAlertRuleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AlertRuleRepositoryAdapter implements AlertRuleRepository {

    private final SpringDataAlertRuleRepository springRepository;

    public AlertRuleRepositoryAdapter(SpringDataAlertRuleRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public AlertRule save(AlertRule rule) {
        AlertRuleDocument document = toDocument(rule);
        AlertRuleDocument saved = springRepository.save(document);
        return toEntity(saved);
    }

    @Override
    public Optional<AlertRule> findById(String id) {
        return springRepository.findById(id).map(this::toEntity);
    }

    @Override
    public Optional<AlertRule> findByAlertIdAndTenantId(String alertId, String tenantId) {
        return Optional.ofNullable(springRepository.findByAlertIdAndTenantId(alertId, tenantId))
                .map(this::toEntity);
    }

    @Override
    public List<AlertRule> findByTenantId(String tenantId) {
        return springRepository.findByTenantId(tenantId).stream()
                .map(this::toEntity).toList();
    }

    @Override
    public List<AlertRule> findByTenantIdAndStatus(String tenantId, AlertRuleStatus status) {
        return springRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(this::toEntity).toList();
    }

    @Override
    public void deleteById(String id) {
        springRepository.deleteById(id);
    }

    @Override
    public void deleteByAlertIdAndTenantId(String alertId, String tenantId) {
        springRepository.deleteByAlertIdAndTenantId(alertId, tenantId);
    }

    private AlertRuleDocument toDocument(AlertRule entity) {
        AlertRuleDocument doc = new AlertRuleDocument();
        doc.setId(entity.getId());
        doc.setAlertId(entity.getAlertId());
        doc.setTenantId(entity.getTenantId());
        doc.setName(entity.getName());
        doc.setMetric(entity.getMetric());
        doc.setCondition(entity.getCondition());
        doc.setThreshold(entity.getThreshold());
        doc.setNotificationChannels(new java.util.ArrayList<>(entity.getNotificationChannels()));
        doc.setStatus(entity.getStatus());
        doc.setMinAlertInterval(entity.getMinAlertInterval());
        doc.setCreatedAt(entity.getCreatedAt());
        doc.setUpdatedAt(entity.getUpdatedAt());
        doc.setLastTriggeredAt(entity.getLastTriggeredAt());
        doc.setTriggerCount(entity.getTriggerCount());
        return doc;
    }

    private AlertRule toEntity(AlertRuleDocument doc) {
        return AlertRule.builder()
                .id(doc.getId())
                .alertId(doc.getAlertId())
                .tenantId(doc.getTenantId())
                .name(doc.getName())
                .metric(doc.getMetric())
                .condition(doc.getCondition())
                .threshold(doc.getThreshold())
                .notificationChannels(doc.getNotificationChannels())
                .status(doc.getStatus())
                .minAlertInterval(doc.getMinAlertInterval())
                .build();
    }
}
