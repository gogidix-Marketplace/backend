package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum ActivityType {
    EMAIL_SENT("email_sent"),
    EMAIL_OPENED("email_opened"),
    EMAIL_CLICKED("email_clicked"),
    CALL_MADE("call_made"),
    CALL_COMPLETED("call_completed"),
    MEETING_SCHEDULED("meeting_scheduled"),
    MEETING_COMPLETED("meeting_completed"),
    NOTE_ADDED("note_added"),
    TASK_CREATED("task_created"),
    TASK_COMPLETED("task_completed"),
    DEMO_SCHEDULED("demo_scheduled"),
    DEMO_COMPLETED("demo_completed"),
    PROPOSAL_SENT("proposal_sent"),
    PROPOSAL_VIEWED("proposal_viewed"),
    MESSAGE_SENT("message_sent"),
    MESSAGE_RECEIVED("message_received"),
    WEBINAR_ATTENDED("webinar_attended"),
    CONTENT_DOWNLOADED("content_downloaded"),
    WEBSITE_VISIT("website_visit"),
    FORM_SUBMITTED("form_submitted"),
    SOCIAL_ENGAGEMENT("social_engagement"),
    STATUS_CHANGED("status_changed"),
    ASSIGNED("assigned"),
    OPPORTUNITY_CREATED("opportunity_created");

    private final String value;

    ActivityType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ActivityType fromString(String value) {
        for (ActivityType type : ActivityType.values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown activity type: " + value);
    }

    public boolean isEngagement() {
        return this == EMAIL_OPENED || this == EMAIL_CLICKED ||
                this == CALL_COMPLETED || this == MEETING_COMPLETED ||
                this == DEMO_COMPLETED || this == PROPOSAL_VIEWED ||
                this == MESSAGE_RECEIVED || this == WEBINAR_ATTENDED ||
                this == CONTENT_DOWNLOADED || this == WEBSITE_VISIT;
    }

    public boolean isOutreach() {
        return this == EMAIL_SENT || this == CALL_MADE ||
                this == MEETING_SCHEDULED || this == DEMO_SCHEDULED ||
                this == PROPOSAL_SENT || this == MESSAGE_SENT;
    }
}
