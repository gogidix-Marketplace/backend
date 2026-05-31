package com.gogidix.digitalmarketing.socialmedia.infrastructure.messaging;

import com.gogidix.digitalmarketing.socialmedia.domain.event.*;
import com.gogidix.digitalmarketing.socialmedia.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishSocialAccountCreated(SocialAccountCreatedEvent event) {
        log.info("SocialAccount created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialAccountUpdated(SocialAccountUpdatedEvent event) {
        log.info("SocialAccount updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialAccountDeleted(SocialAccountDeletedEvent event) {
        log.info("SocialAccount deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialCalendarCreated(SocialCalendarCreatedEvent event) {
        log.info("SocialCalendar created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialCalendarUpdated(SocialCalendarUpdatedEvent event) {
        log.info("SocialCalendar updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialCalendarDeleted(SocialCalendarDeletedEvent event) {
        log.info("SocialCalendar deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialContentCreated(SocialContentCreatedEvent event) {
        log.info("SocialContent created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialContentUpdated(SocialContentUpdatedEvent event) {
        log.info("SocialContent updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialContentDeleted(SocialContentDeletedEvent event) {
        log.info("SocialContent deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialEngagementCreated(SocialEngagementCreatedEvent event) {
        log.info("SocialEngagement created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialEngagementUpdated(SocialEngagementUpdatedEvent event) {
        log.info("SocialEngagement updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialEngagementDeleted(SocialEngagementDeletedEvent event) {
        log.info("SocialEngagement deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialPostCreated(SocialPostCreatedEvent event) {
        log.info("SocialPost created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialPostUpdated(SocialPostUpdatedEvent event) {
        log.info("SocialPost updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSocialPostDeleted(SocialPostDeletedEvent event) {
        log.info("SocialPost deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
