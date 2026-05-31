package com.gogidix.sales.dashboard.domain.port.out;

import java.util.List;

import com.gogidix.sales.dashboard.domain.event.AggregationCompletedEvent;
import com.gogidix.sales.dashboard.domain.event.DashboardRefreshedEvent;
import com.gogidix.sales.dashboard.domain.event.MetricUpdatedEvent;
import com.gogidix.sales.dashboard.domain.event.RollupCompletedEvent;
import com.gogidix.sales.dashboard.domain.event.WidgetUpdatedEvent;

/**
 * Event Publisher Interface (Output Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a metric updated event
     */
    void publishMetricEvent(MetricUpdatedEvent event);

    /**
     * Publishes an aggregation completed event
     */
    void publishAggregationEvent(AggregationCompletedEvent event);

    /**
     * Publishes a rollup completed event
     */
    void publishRollupEvent(RollupCompletedEvent event);

    /**
     * Publishes a widget updated event
     */
    void publishWidgetEvent(WidgetUpdatedEvent event);

    /**
     * Publishes a dashboard refreshed event
     */
    void publishDashboardRefreshEvent(DashboardRefreshedEvent event);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();

    /**
     * Publishes all events from a collection
     */
    void publishAll(java.util.List<Object> events);
}
