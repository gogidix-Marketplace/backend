package com.gogidix.corporatecms.infrastructure.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Scheduled tasks for background processing.
 */
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final ContentService contentService;

    @Scheduled(cron = "${content.publish-cron:0 */5 * * * *}")
    public void publishScheduledContent() {
        log.debug("Checking for scheduled content to publish");
        try {
            contentService.publishScheduledContent();
        } catch (Exception e) {
            log.error("Error publishing scheduled content", e);
        }
    }
}
