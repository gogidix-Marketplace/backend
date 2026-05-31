package com.gogidix.finance.ledger.domain.port.out;

import com.gogidix.finance.ledger.domain.event.JournalEntryEvent;
import com.gogidix.finance.ledger.domain.event.LedgerAccountEvent;

import java.util.List;

/**
 * Event Publisher Output Port
 * Defines the interface for publishing domain events
 */
public interface EventPublisher {

    void publishJournalEntryEvent(JournalEntryEvent event);

    void publishLedgerAccountEvent(LedgerAccountEvent event);

    void publishAll(List<?> events);

    boolean isReady();
}
