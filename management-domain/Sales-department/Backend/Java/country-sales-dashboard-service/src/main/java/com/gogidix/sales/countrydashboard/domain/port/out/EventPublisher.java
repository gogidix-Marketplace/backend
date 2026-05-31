package com.gogidix.sales.countrydashboard.domain.port.out;

import com.gogidix.sales.countrydashboard.domain.event.*;

/**
 * Event Publisher Port (Output Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    void publishDashboardCreatedEvent(CountryDashboardCreatedEvent event);

    void publishMetricUpdatedEvent(CountryMetricUpdatedEvent event);

    void publishTerritoryPerformanceUpdatedEvent(TerritoryPerformanceUpdatedEvent event);

    void publishQuotaAdjustedEvent(CountryQuotaAdjustedEvent event);

    void publishComparisonGeneratedEvent(CountryComparisonGeneratedEvent event);

    void publishCurrencyConversionEvent(CurrencyConversionAppliedEvent event);

    boolean isReady();
}
