package com.gogidix.globalbusinessmanagement.businessintelligence.infrastructure.messaging;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.event.*;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishBIReportCreated(BIReportCreatedEvent event) {
        log.info("BIReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBIReportUpdated(BIReportUpdatedEvent event) {
        log.info("BIReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBIReportDeleted(BIReportDeletedEvent event) {
        log.info("BIReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishForecastCreated(ForecastCreatedEvent event) {
        log.info("Forecast created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishForecastUpdated(ForecastUpdatedEvent event) {
        log.info("Forecast updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishForecastDeleted(ForecastDeletedEvent event) {
        log.info("Forecast deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInsightCreated(InsightCreatedEvent event) {
        log.info("Insight created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInsightUpdated(InsightUpdatedEvent event) {
        log.info("Insight updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishInsightDeleted(InsightDeletedEvent event) {
        log.info("Insight deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTrendAnalysisCreated(TrendAnalysisCreatedEvent event) {
        log.info("TrendAnalysis created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTrendAnalysisUpdated(TrendAnalysisUpdatedEvent event) {
        log.info("TrendAnalysis updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTrendAnalysisDeleted(TrendAnalysisDeletedEvent event) {
        log.info("TrendAnalysis deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
