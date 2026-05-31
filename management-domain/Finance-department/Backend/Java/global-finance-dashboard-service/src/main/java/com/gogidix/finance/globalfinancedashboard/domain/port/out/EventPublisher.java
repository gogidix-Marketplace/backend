package com.gogidix.finance.globalfinancedashboard.domain.port.out;

import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardSharedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.MetricCalculatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetUpdatedEvent;

/**
 * Output port for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes dashboard created event
     */
    void publish(DashboardCreatedEvent event);

    /**
     * Publishes dashboard updated event
     */
    void publish(DashboardUpdatedEvent event);

    /**
     * Publishes dashboard shared event
     */
    void publish(DashboardSharedEvent event);

    /**
     * Publishes widget created event
     */
    void publish(WidgetCreatedEvent event);

    /**
     * Publishes widget updated event
     */
    void publish(WidgetUpdatedEvent event);

    /**
     * Publishes metric calculated event
     */
    void publish(MetricCalculatedEvent event);

    /**
     * Publishes generic dashboard event
     */
    void publish(String topic, Object event);
}
