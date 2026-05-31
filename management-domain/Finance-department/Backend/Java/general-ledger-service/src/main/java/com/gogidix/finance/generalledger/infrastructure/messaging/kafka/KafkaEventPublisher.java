package com.gogidix.finance.generalledger.infrastructure.messaging.kafka;

import com.gogidix.finance.generalledger.application.dto.response.ErrorResponseDto;
import com.gogidix.finance.ledger.domain.event.JournalEntryEvent;
import com.gogidix.finance.ledger.domain.event.LedgerAccountEvent;
import com.gogidix.finance.ledger.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher
 * Publishes general ledger events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.journal-entry-events:general-ledger-service.journal-entry-events}")
    private String journalEntryEventsTopic;

    @Value("${spring.kafka.topics.ledger-account-events:general-ledger-service.ledger-account-events}")
    private String ledgerAccountEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publishJournalEntryEvent(JournalEntryEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getJournalEntryId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(journalEntryEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published journal entry event: {} to topic: {}",
                        event.getEventId(), journalEntryEventsTopic);
                } else {
                    log.error("Failed to publish journal entry event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing journal entry event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishLedgerAccountEvent(LedgerAccountEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getAccountId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(ledgerAccountEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published ledger account event: {} to topic: {}",
                        event.getEventId(), ledgerAccountEventsTopic);
                } else {
                    log.error("Failed to publish ledger account event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing ledger account event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<?> events) {
        for (Object event : events) {
            if (event instanceof JournalEntryEvent journalEntryEvent) {
                publishJournalEntryEvent(journalEntryEvent);
            } else if (event instanceof LedgerAccountEvent ledgerAccountEvent) {
                publishLedgerAccountEvent(ledgerAccountEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            // Kafka health check - verify connection is available
            // In production, this would check the Kafka producer's health
            return kafkaTemplate != null;
        } catch (Exception e) {
            log.error("Kafka publisher not ready", e);
            return false;
        }
    }
}
