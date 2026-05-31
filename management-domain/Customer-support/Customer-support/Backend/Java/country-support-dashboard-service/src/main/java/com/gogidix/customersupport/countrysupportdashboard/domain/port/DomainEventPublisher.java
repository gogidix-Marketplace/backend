package com.gogidix.customersupport.countrysupportdashboard.domain.port;

import com.gogidix.customersupport.countrysupportdashboard.domain.event.*;

public interface DomainEventPublisher {
    void publishCountrySpecificMetricsCreated(CountrySpecificMetricsCreatedEvent event);
    void publishCountrySpecificMetricsUpdated(CountrySpecificMetricsUpdatedEvent event);
    void publishCountrySpecificMetricsDeleted(CountrySpecificMetricsDeletedEvent event);
    void publishRegionalTicketStatsCreated(RegionalTicketStatsCreatedEvent event);
    void publishRegionalTicketStatsUpdated(RegionalTicketStatsUpdatedEvent event);
    void publishRegionalTicketStatsDeleted(RegionalTicketStatsDeletedEvent event);
}
