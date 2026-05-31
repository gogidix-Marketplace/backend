package com.gogidix.digitalmarketing.analytics.infrastructure.messaging;

import com.gogidix.digitalmarketing.analytics.domain.event.*;
import com.gogidix.digitalmarketing.analytics.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAnalyticsReportCreated(AnalyticsReportCreatedEvent event) {
        log.info("AnalyticsReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsReportUpdated(AnalyticsReportUpdatedEvent event) {
        log.info("AnalyticsReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsReportDeleted(AnalyticsReportDeletedEvent event) {
        log.info("AnalyticsReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignAnalyticsCreated(CampaignAnalyticsCreatedEvent event) {
        log.info("CampaignAnalytics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignAnalyticsUpdated(CampaignAnalyticsUpdatedEvent event) {
        log.info("CampaignAnalytics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignAnalyticsDeleted(CampaignAnalyticsDeletedEvent event) {
        log.info("CampaignAnalytics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignMetricCreated(CampaignMetricCreatedEvent event) {
        log.info("CampaignMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignMetricUpdated(CampaignMetricUpdatedEvent event) {
        log.info("CampaignMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCampaignMetricDeleted(CampaignMetricDeletedEvent event) {
        log.info("CampaignMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelAnalyticsCreated(ChannelAnalyticsCreatedEvent event) {
        log.info("ChannelAnalytics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelAnalyticsUpdated(ChannelAnalyticsUpdatedEvent event) {
        log.info("ChannelAnalytics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelAnalyticsDeleted(ChannelAnalyticsDeletedEvent event) {
        log.info("ChannelAnalytics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelMetricCreated(ChannelMetricCreatedEvent event) {
        log.info("ChannelMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelMetricUpdated(ChannelMetricUpdatedEvent event) {
        log.info("ChannelMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishChannelMetricDeleted(ChannelMetricDeletedEvent event) {
        log.info("ChannelMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMarketingMetricCreated(MarketingMetricCreatedEvent event) {
        log.info("MarketingMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMarketingMetricUpdated(MarketingMetricUpdatedEvent event) {
        log.info("MarketingMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMarketingMetricDeleted(MarketingMetricDeletedEvent event) {
        log.info("MarketingMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricCreated(MetricCreatedEvent event) {
        log.info("Metric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricUpdated(MetricUpdatedEvent event) {
        log.info("Metric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricDeleted(MetricDeletedEvent event) {
        log.info("Metric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
