package com.gogidix.finance.exchangerate.infrastructure.event;

import com.gogidix.finance.exchangerate.domain.event.ExchangeRateEvent;
import com.gogidix.finance.exchangerate.domain.port.out.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventPublisherImpl implements EventPublisher {

    @Override
    public void publish(ExchangeRateEvent event) {
        log.info("Publishing event: {} for pair: {}", event.getEventType(), event.getCurrencyPair());
        // In production, this would publish to Kafka, RabbitMQ, or another message broker
    }
}
