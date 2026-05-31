package com.gogidix.digitalmarketing.emailmarketing.infrastructure.messaging;

import com.gogidix.digitalmarketing.emailmarketing.domain.event.*;
import com.gogidix.digitalmarketing.emailmarketing.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishEmailCampaignCreated(EmailCampaignCreatedEvent event) {
        log.info("EmailCampaign created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailCampaignUpdated(EmailCampaignUpdatedEvent event) {
        log.info("EmailCampaign updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailCampaignDeleted(EmailCampaignDeletedEvent event) {
        log.info("EmailCampaign deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailListCreated(EmailListCreatedEvent event) {
        log.info("EmailList created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailListUpdated(EmailListUpdatedEvent event) {
        log.info("EmailList updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailListDeleted(EmailListDeletedEvent event) {
        log.info("EmailList deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailMessageCreated(EmailMessageCreatedEvent event) {
        log.info("EmailMessage created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailMessageUpdated(EmailMessageUpdatedEvent event) {
        log.info("EmailMessage updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailMessageDeleted(EmailMessageDeletedEvent event) {
        log.info("EmailMessage deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailMetricsCreated(EmailMetricsCreatedEvent event) {
        log.info("EmailMetrics created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailMetricsUpdated(EmailMetricsUpdatedEvent event) {
        log.info("EmailMetrics updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailMetricsDeleted(EmailMetricsDeletedEvent event) {
        log.info("EmailMetrics deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailSubscriberCreated(EmailSubscriberCreatedEvent event) {
        log.info("EmailSubscriber created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailSubscriberUpdated(EmailSubscriberUpdatedEvent event) {
        log.info("EmailSubscriber updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailSubscriberDeleted(EmailSubscriberDeletedEvent event) {
        log.info("EmailSubscriber deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailTemplateCreated(EmailTemplateCreatedEvent event) {
        log.info("EmailTemplate created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailTemplateUpdated(EmailTemplateUpdatedEvent event) {
        log.info("EmailTemplate updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmailTemplateDeleted(EmailTemplateDeletedEvent event) {
        log.info("EmailTemplate deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
