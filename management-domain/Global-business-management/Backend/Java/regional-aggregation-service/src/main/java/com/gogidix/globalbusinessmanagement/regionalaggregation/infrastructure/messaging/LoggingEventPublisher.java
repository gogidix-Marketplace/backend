package com.gogidix.globalbusinessmanagement.regionalaggregation.infrastructure.messaging;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.event.*;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAggregatedMetricsCreated(AggregatedMetricsCreatedEvent event) {
        log.info("AggregatedMetrics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAggregatedMetricsUpdated(AggregatedMetricsUpdatedEvent event) {
        log.info("AggregatedMetrics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAggregatedMetricsDeleted(AggregatedMetricsDeletedEvent event) {
        log.info("AggregatedMetrics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryContributionCreated(CountryContributionCreatedEvent event) {
        log.info("CountryContribution created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryContributionUpdated(CountryContributionUpdatedEvent event) {
        log.info("CountryContribution updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryContributionDeleted(CountryContributionDeletedEvent event) {
        log.info("CountryContribution deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalDataCreated(RegionalDataCreatedEvent event) {
        log.info("RegionalData created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalDataUpdated(RegionalDataUpdatedEvent event) {
        log.info("RegionalData updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalDataDeleted(RegionalDataDeletedEvent event) {
        log.info("RegionalData deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
