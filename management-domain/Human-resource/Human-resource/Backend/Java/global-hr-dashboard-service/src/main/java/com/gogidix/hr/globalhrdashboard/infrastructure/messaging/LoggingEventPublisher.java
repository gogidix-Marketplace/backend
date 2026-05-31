package com.gogidix.hr.globalhrdashboard.infrastructure.messaging;

import com.gogidix.hr.globalhrdashboard.domain.event.*;
import com.gogidix.hr.globalhrdashboard.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAggregationLevelCreated(AggregationLevelCreatedEvent event) {
        log.info("AggregationLevel created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAggregationLevelUpdated(AggregationLevelUpdatedEvent event) {
        log.info("AggregationLevel updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAggregationLevelDeleted(AggregationLevelDeletedEvent event) {
        log.info("AggregationLevel deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceMetricCreated(ComplianceMetricCreatedEvent event) {
        log.info("ComplianceMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceMetricUpdated(ComplianceMetricUpdatedEvent event) {
        log.info("ComplianceMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceMetricDeleted(ComplianceMetricDeletedEvent event) {
        log.info("ComplianceMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceStatusCreated(ComplianceStatusCreatedEvent event) {
        log.info("ComplianceStatus created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceStatusUpdated(ComplianceStatusUpdatedEvent event) {
        log.info("ComplianceStatus updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceStatusDeleted(ComplianceStatusDeletedEvent event) {
        log.info("ComplianceStatus deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryHeadcountCreated(CountryHeadcountCreatedEvent event) {
        log.info("CountryHeadcount created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryHeadcountUpdated(CountryHeadcountUpdatedEvent event) {
        log.info("CountryHeadcount updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryHeadcountDeleted(CountryHeadcountDeletedEvent event) {
        log.info("CountryHeadcount deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDiversityMetricCreated(DiversityMetricCreatedEvent event) {
        log.info("DiversityMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDiversityMetricUpdated(DiversityMetricUpdatedEvent event) {
        log.info("DiversityMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDiversityMetricDeleted(DiversityMetricDeletedEvent event) {
        log.info("DiversityMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExecutiveLevelCreated(ExecutiveLevelCreatedEvent event) {
        log.info("ExecutiveLevel created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExecutiveLevelUpdated(ExecutiveLevelUpdatedEvent event) {
        log.info("ExecutiveLevel updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExecutiveLevelDeleted(ExecutiveLevelDeletedEvent event) {
        log.info("ExecutiveLevel deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishGlobalWorkforceMetricCreated(GlobalWorkforceMetricCreatedEvent event) {
        log.info("GlobalWorkforceMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishGlobalWorkforceMetricUpdated(GlobalWorkforceMetricUpdatedEvent event) {
        log.info("GlobalWorkforceMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishGlobalWorkforceMetricDeleted(GlobalWorkforceMetricDeletedEvent event) {
        log.info("GlobalWorkforceMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricCategoryCreated(MetricCategoryCreatedEvent event) {
        log.info("MetricCategory created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricCategoryUpdated(MetricCategoryUpdatedEvent event) {
        log.info("MetricCategory updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricCategoryDeleted(MetricCategoryDeletedEvent event) {
        log.info("MetricCategory deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricTrendCreated(MetricTrendCreatedEvent event) {
        log.info("MetricTrend created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricTrendUpdated(MetricTrendUpdatedEvent event) {
        log.info("MetricTrend updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricTrendDeleted(MetricTrendDeletedEvent event) {
        log.info("MetricTrend deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalMetricCreated(RegionalMetricCreatedEvent event) {
        log.info("RegionalMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalMetricUpdated(RegionalMetricUpdatedEvent event) {
        log.info("RegionalMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalMetricDeleted(RegionalMetricDeletedEvent event) {
        log.info("RegionalMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRetentionMetricCreated(RetentionMetricCreatedEvent event) {
        log.info("RetentionMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRetentionMetricUpdated(RetentionMetricUpdatedEvent event) {
        log.info("RetentionMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRetentionMetricDeleted(RetentionMetricDeletedEvent event) {
        log.info("RetentionMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
