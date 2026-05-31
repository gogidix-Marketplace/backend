package com.gogidix.digitalmarketing.emailmarketing.domain.port;

import com.gogidix.digitalmarketing.emailmarketing.domain.event.*;

public interface DomainEventPublisher {
    void publishEmailCampaignCreated(EmailCampaignCreatedEvent event);
    void publishEmailCampaignUpdated(EmailCampaignUpdatedEvent event);
    void publishEmailCampaignDeleted(EmailCampaignDeletedEvent event);
    void publishEmailListCreated(EmailListCreatedEvent event);
    void publishEmailListUpdated(EmailListUpdatedEvent event);
    void publishEmailListDeleted(EmailListDeletedEvent event);
    void publishEmailMessageCreated(EmailMessageCreatedEvent event);
    void publishEmailMessageUpdated(EmailMessageUpdatedEvent event);
    void publishEmailMessageDeleted(EmailMessageDeletedEvent event);
    void publishEmailMetricsCreated(EmailMetricsCreatedEvent event);
    void publishEmailMetricsUpdated(EmailMetricsUpdatedEvent event);
    void publishEmailMetricsDeleted(EmailMetricsDeletedEvent event);
    void publishEmailSubscriberCreated(EmailSubscriberCreatedEvent event);
    void publishEmailSubscriberUpdated(EmailSubscriberUpdatedEvent event);
    void publishEmailSubscriberDeleted(EmailSubscriberDeletedEvent event);
    void publishEmailTemplateCreated(EmailTemplateCreatedEvent event);
    void publishEmailTemplateUpdated(EmailTemplateUpdatedEvent event);
    void publishEmailTemplateDeleted(EmailTemplateDeletedEvent event);
}
