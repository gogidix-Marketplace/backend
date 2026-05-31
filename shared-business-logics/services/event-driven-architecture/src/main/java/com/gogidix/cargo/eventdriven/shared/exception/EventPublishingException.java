package com.gogidix.cargo.eventdriven.shared.exception;

public class EventPublishingException extends RuntimeException {
    private final String topic;
    private final String eventId;

    public EventPublishingException(String topic, String eventId, Throwable cause) {
        super("Failed to publish event " + eventId + " to topic " + topic, cause);
        this.topic = topic;
        this.eventId = eventId;
    }

    public String getTopic() { return topic; }
    public String getEventId() { return eventId; }
}
