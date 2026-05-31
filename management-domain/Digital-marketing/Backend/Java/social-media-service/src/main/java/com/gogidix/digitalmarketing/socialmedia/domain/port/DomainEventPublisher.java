package com.gogidix.digitalmarketing.socialmedia.domain.port;

import com.gogidix.digitalmarketing.socialmedia.domain.event.*;

public interface DomainEventPublisher {
    void publishSocialAccountCreated(SocialAccountCreatedEvent event);
    void publishSocialAccountUpdated(SocialAccountUpdatedEvent event);
    void publishSocialAccountDeleted(SocialAccountDeletedEvent event);
    void publishSocialCalendarCreated(SocialCalendarCreatedEvent event);
    void publishSocialCalendarUpdated(SocialCalendarUpdatedEvent event);
    void publishSocialCalendarDeleted(SocialCalendarDeletedEvent event);
    void publishSocialContentCreated(SocialContentCreatedEvent event);
    void publishSocialContentUpdated(SocialContentUpdatedEvent event);
    void publishSocialContentDeleted(SocialContentDeletedEvent event);
    void publishSocialEngagementCreated(SocialEngagementCreatedEvent event);
    void publishSocialEngagementUpdated(SocialEngagementUpdatedEvent event);
    void publishSocialEngagementDeleted(SocialEngagementDeletedEvent event);
    void publishSocialPostCreated(SocialPostCreatedEvent event);
    void publishSocialPostUpdated(SocialPostUpdatedEvent event);
    void publishSocialPostDeleted(SocialPostDeletedEvent event);
}
