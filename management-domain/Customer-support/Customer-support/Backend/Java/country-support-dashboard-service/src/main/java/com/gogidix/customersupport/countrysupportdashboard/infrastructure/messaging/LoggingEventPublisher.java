package com.gogidix.customersupport.countrysupportdashboard.infrastructure.messaging;

import com.gogidix.customersupport.countrysupportdashboard.domain.event.*;
import com.gogidix.customersupport.countrysupportdashboard.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCountrySpecificMetricsCreated(CountrySpecificMetricsCreatedEvent event) {
        log.info("CountrySpecificMetrics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountrySpecificMetricsUpdated(CountrySpecificMetricsUpdatedEvent event) {
        log.info("CountrySpecificMetrics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountrySpecificMetricsDeleted(CountrySpecificMetricsDeletedEvent event) {
        log.info("CountrySpecificMetrics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalTicketStatsCreated(RegionalTicketStatsCreatedEvent event) {
        log.info("RegionalTicketStats created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalTicketStatsUpdated(RegionalTicketStatsUpdatedEvent event) {
        log.info("RegionalTicketStats updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionalTicketStatsDeleted(RegionalTicketStatsDeletedEvent event) {
        log.info("RegionalTicketStats deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
