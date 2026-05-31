package com.gogidix.digitalmarketing.analytics.domain.port;

import com.gogidix.digitalmarketing.analytics.domain.event.*;

public interface DomainEventPublisher {
    void publishAnalyticsReportCreated(AnalyticsReportCreatedEvent event);
    void publishAnalyticsReportUpdated(AnalyticsReportUpdatedEvent event);
    void publishAnalyticsReportDeleted(AnalyticsReportDeletedEvent event);
    void publishCampaignAnalyticsCreated(CampaignAnalyticsCreatedEvent event);
    void publishCampaignAnalyticsUpdated(CampaignAnalyticsUpdatedEvent event);
    void publishCampaignAnalyticsDeleted(CampaignAnalyticsDeletedEvent event);
    void publishCampaignMetricCreated(CampaignMetricCreatedEvent event);
    void publishCampaignMetricUpdated(CampaignMetricUpdatedEvent event);
    void publishCampaignMetricDeleted(CampaignMetricDeletedEvent event);
    void publishChannelAnalyticsCreated(ChannelAnalyticsCreatedEvent event);
    void publishChannelAnalyticsUpdated(ChannelAnalyticsUpdatedEvent event);
    void publishChannelAnalyticsDeleted(ChannelAnalyticsDeletedEvent event);
    void publishChannelMetricCreated(ChannelMetricCreatedEvent event);
    void publishChannelMetricUpdated(ChannelMetricUpdatedEvent event);
    void publishChannelMetricDeleted(ChannelMetricDeletedEvent event);
    void publishMarketingMetricCreated(MarketingMetricCreatedEvent event);
    void publishMarketingMetricUpdated(MarketingMetricUpdatedEvent event);
    void publishMarketingMetricDeleted(MarketingMetricDeletedEvent event);
    void publishMetricCreated(MetricCreatedEvent event);
    void publishMetricUpdated(MetricUpdatedEvent event);
    void publishMetricDeleted(MetricDeletedEvent event);
}
