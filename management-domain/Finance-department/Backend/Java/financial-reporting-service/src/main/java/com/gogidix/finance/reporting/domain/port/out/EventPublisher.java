package com.gogidix.finance.reporting.domain.port.out;

import com.gogidix.finance.reporting.domain.event.ReportGeneratedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 */
public interface EventPublisher {

    void publish(ReportGeneratedEvent event);

    void publishAll(List<Object> events);

    boolean isReady();
}
