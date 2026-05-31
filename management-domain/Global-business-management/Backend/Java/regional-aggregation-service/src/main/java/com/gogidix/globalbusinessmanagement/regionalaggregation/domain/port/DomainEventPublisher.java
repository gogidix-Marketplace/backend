package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.port;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.event.*;

public interface DomainEventPublisher {
    void publishAggregatedMetricsCreated(AggregatedMetricsCreatedEvent event);
    void publishAggregatedMetricsUpdated(AggregatedMetricsUpdatedEvent event);
    void publishAggregatedMetricsDeleted(AggregatedMetricsDeletedEvent event);
    void publishCountryContributionCreated(CountryContributionCreatedEvent event);
    void publishCountryContributionUpdated(CountryContributionUpdatedEvent event);
    void publishCountryContributionDeleted(CountryContributionDeletedEvent event);
    void publishRegionalDataCreated(RegionalDataCreatedEvent event);
    void publishRegionalDataUpdated(RegionalDataUpdatedEvent event);
    void publishRegionalDataDeleted(RegionalDataDeletedEvent event);
}
