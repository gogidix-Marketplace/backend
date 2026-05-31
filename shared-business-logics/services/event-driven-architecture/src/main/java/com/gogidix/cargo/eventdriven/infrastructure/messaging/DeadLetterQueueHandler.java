package com.gogidix.cargo.eventdriven.infrastructure.messaging;

import com.gogidix.cargo.eventdriven.domain.model.DeadLetterEntry;
import com.gogidix.cargo.eventdriven.domain.policy.EventRetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeadLetterQueueHandler {
    private static final Logger log = LoggerFactory.getLogger(DeadLetterQueueHandler.class);
    private final EventRetryPolicy retryPolicy;
    private final Map<String, DeadLetterEntry> deadLetters = new ConcurrentHashMap<>();

    public DeadLetterQueueHandler(EventRetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public void handleFailedEvent(String topic, String key, String payload, String errorMessage) {
        DeadLetterEntry entry = new DeadLetterEntry();
        entry.setId(key);
        entry.setOriginalTopic(topic);
        entry.setEventKey(key);
        entry.setEventPayload(payload);
        entry.setErrorMessage(errorMessage);
        deadLetters.put(key, entry);
        log.warn("Event moved to dead letter queue: topic={}, key={}", topic, key);
    }

    public boolean shouldRetry(String key) {
        DeadLetterEntry entry = deadLetters.get(key);
        if (entry == null) return false;
        boolean shouldRetry = retryPolicy.shouldRetry(entry.getRetryCount());
        if (shouldRetry) { entry.incrementRetry(); } else { entry.markExhausted(); }
        return shouldRetry;
    }

    public List<DeadLetterEntry> getPendingDeadLetters() {
        List<DeadLetterEntry> pending = new ArrayList<>();
        for (DeadLetterEntry entry : deadLetters.values()) {
            if (entry.getStatus() == DeadLetterEntry.DeadLetterStatus.PENDING ||
                entry.getStatus() == DeadLetterEntry.DeadLetterStatus.RETRYING) {
                pending.add(entry);
            }
        }
        return pending;
    }

    public void markResolved(String key) {
        DeadLetterEntry entry = deadLetters.get(key);
        if (entry != null) { entry.markResolved(); }
    }
}
