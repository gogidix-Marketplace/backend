package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;

/**
 * Enumeration representing the workflow status of content.
 */
@Getter
public enum ContentStatus {
    DRAFT("draft", "Content is being drafted"),
    PENDING_REVIEW("pending_review", "Content submitted for review"),
    PENDING_APPROVAL("pending_approval", "Content awaiting final approval"),
    APPROVED("approved", "Content approved and ready to publish"),
    PUBLISHED("published", "Content is live"),
    SCHEDULED("scheduled", "Content scheduled for future publishing"),
    ARCHIVED("archived", "Content archived"),
    REJECTED("rejected", "Content rejected during review");

    private final String code;
    private final String description;

    ContentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContentStatus fromCode(String code) {
        for (ContentStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown content status: " + code);
    }

    public boolean canTransitionTo(ContentStatus newStatus) {
        return switch (this) {
            case DRAFT -> newStatus == PENDING_REVIEW || newStatus == ARCHIVED;
            case PENDING_REVIEW -> newStatus == DRAFT || newStatus == PENDING_APPROVAL || newStatus == REJECTED;
            case PENDING_APPROVAL -> newStatus == PENDING_REVIEW || newStatus == APPROVED || newStatus == REJECTED;
            case APPROVED -> newStatus == PUBLISHED || newStatus == SCHEDULED || newStatus == PENDING_REVIEW || newStatus == ARCHIVED;
            case PUBLISHED -> newStatus == ARCHIVED;
            case SCHEDULED -> newStatus == APPROVED || newStatus == PUBLISHED || newStatus == PENDING_REVIEW || newStatus == ARCHIVED;
            case ARCHIVED -> newStatus == DRAFT;
            case REJECTED -> newStatus == DRAFT || newStatus == ARCHIVED;
        };
    }
}
