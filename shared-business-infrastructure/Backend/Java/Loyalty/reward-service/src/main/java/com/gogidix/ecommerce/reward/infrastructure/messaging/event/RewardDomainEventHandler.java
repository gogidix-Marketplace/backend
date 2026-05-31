package com.gogidix.ecommerce.reward.infrastructure.messaging.event;

import com.gogidix.ecommerce.reward.domain.event.RewardCreatedEvent;
import com.gogidix.ecommerce.reward.domain.event.RewardUpdatedEvent;
import com.gogidix.ecommerce.reward.domain.event.RewardDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RewardDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(RewardDomainEventHandler.class);

    @EventListener
    public void handleCreated(RewardCreatedEvent event) {
        log.info("Reward created: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleUpdated(RewardUpdatedEvent event) {
        log.info("Reward updated: id={}, tenant={}", event.entityId(), event.tenantId());
    }

    @EventListener
    public void handleDeleted(RewardDeletedEvent event) {
        log.info("Reward deleted: id={}, tenant={}", event.entityId(), event.tenantId());
    }
}
