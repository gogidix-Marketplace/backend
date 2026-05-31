package com.gogidix.ecommerce.pricing.infrastructure.messaging.event;

import com.gogidix.ecommerce.pricing.domain.event.PricingDomainEvent;
import com.gogidix.ecommerce.pricing.domain.event.PricingRuleCreatedEvent;
import com.gogidix.ecommerce.pricing.domain.event.PricingRuleUpdatedEvent;
import com.gogidix.ecommerce.pricing.domain.event.PricingRuleDeletedEvent;
import com.gogidix.ecommerce.pricing.domain.event.PriceCalculatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PricingDomainEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PricingDomainEventHandler.class);

    @EventListener
    public void handlePricingRuleCreated(PricingRuleCreatedEvent event) {
        log.info("Pricing rule created: ruleId={}, tenantId={}, ruleCode={}",
                event.ruleId(), event.tenantId(), event.ruleCode());
    }

    @EventListener
    public void handlePricingRuleUpdated(PricingRuleUpdatedEvent event) {
        log.info("Pricing rule updated: ruleId={}, tenantId={}, ruleCode={}",
                event.ruleId(), event.tenantId(), event.ruleCode());
    }

    @EventListener
    public void handlePricingRuleDeleted(PricingRuleDeletedEvent event) {
        log.info("Pricing rule deleted: ruleId={}, tenantId={}, ruleCode={}",
                event.ruleId(), event.tenantId(), event.ruleCode());
    }

    @EventListener
    public void handlePriceCalculated(PriceCalculatedEvent event) {
        log.info("Price calculated: tenantId={}, productId={}, originalPrice={}, finalPrice={}, rule={}",
                event.tenantId(), event.productId(), event.originalPrice(), event.finalPrice(), event.appliedRuleCode());
    }
}
