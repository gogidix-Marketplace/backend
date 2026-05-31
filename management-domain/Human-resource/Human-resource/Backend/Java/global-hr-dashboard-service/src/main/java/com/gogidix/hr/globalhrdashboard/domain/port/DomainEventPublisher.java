package com.gogidix.hr.globalhrdashboard.domain.port;

import com.gogidix.hr.globalhrdashboard.domain.event.*;

public interface DomainEventPublisher {
    void publishAggregationLevelCreated(AggregationLevelCreatedEvent event);
    void publishAggregationLevelUpdated(AggregationLevelUpdatedEvent event);
    void publishAggregationLevelDeleted(AggregationLevelDeletedEvent event);
    void publishComplianceMetricCreated(ComplianceMetricCreatedEvent event);
    void publishComplianceMetricUpdated(ComplianceMetricUpdatedEvent event);
    void publishComplianceMetricDeleted(ComplianceMetricDeletedEvent event);
    void publishComplianceStatusCreated(ComplianceStatusCreatedEvent event);
    void publishComplianceStatusUpdated(ComplianceStatusUpdatedEvent event);
    void publishComplianceStatusDeleted(ComplianceStatusDeletedEvent event);
    void publishCountryHeadcountCreated(CountryHeadcountCreatedEvent event);
    void publishCountryHeadcountUpdated(CountryHeadcountUpdatedEvent event);
    void publishCountryHeadcountDeleted(CountryHeadcountDeletedEvent event);
    void publishDiversityMetricCreated(DiversityMetricCreatedEvent event);
    void publishDiversityMetricUpdated(DiversityMetricUpdatedEvent event);
    void publishDiversityMetricDeleted(DiversityMetricDeletedEvent event);
    void publishExecutiveLevelCreated(ExecutiveLevelCreatedEvent event);
    void publishExecutiveLevelUpdated(ExecutiveLevelUpdatedEvent event);
    void publishExecutiveLevelDeleted(ExecutiveLevelDeletedEvent event);
    void publishGlobalWorkforceMetricCreated(GlobalWorkforceMetricCreatedEvent event);
    void publishGlobalWorkforceMetricUpdated(GlobalWorkforceMetricUpdatedEvent event);
    void publishGlobalWorkforceMetricDeleted(GlobalWorkforceMetricDeletedEvent event);
    void publishMetricCategoryCreated(MetricCategoryCreatedEvent event);
    void publishMetricCategoryUpdated(MetricCategoryUpdatedEvent event);
    void publishMetricCategoryDeleted(MetricCategoryDeletedEvent event);
    void publishMetricTrendCreated(MetricTrendCreatedEvent event);
    void publishMetricTrendUpdated(MetricTrendUpdatedEvent event);
    void publishMetricTrendDeleted(MetricTrendDeletedEvent event);
    void publishRegionalMetricCreated(RegionalMetricCreatedEvent event);
    void publishRegionalMetricUpdated(RegionalMetricUpdatedEvent event);
    void publishRegionalMetricDeleted(RegionalMetricDeletedEvent event);
    void publishRetentionMetricCreated(RetentionMetricCreatedEvent event);
    void publishRetentionMetricUpdated(RetentionMetricUpdatedEvent event);
    void publishRetentionMetricDeleted(RetentionMetricDeletedEvent event);
}
