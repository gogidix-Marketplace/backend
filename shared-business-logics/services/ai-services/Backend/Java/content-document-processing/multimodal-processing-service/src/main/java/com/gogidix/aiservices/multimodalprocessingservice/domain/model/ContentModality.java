package com.gogidix.aiservices.multimodalprocessingservice.domain.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum ContentModality {
    TEXT(768, 10, Set.of("txt", "pdf", "html", "md")),
    IMAGE(768, 20, Set.of("png", "jpg", "jpeg", "gif", "webp")),
    AUDIO(768, 100, Set.of("mp3", "wav", "ogg", "flac")),
    VIDEO(768, 500, Set.of("mp4", "avi", "mov", "webm")),
    UNKNOWN(768, 0, Set.of());

    private final int embeddingDimension;
    private final int maxSizeMB;
    private final Set<String> supportedFormats;

    ContentModality(int embeddingDimension, int maxSizeMB, Set<String> supportedFormats) {
        this.embeddingDimension = embeddingDimension;
        this.maxSizeMB = maxSizeMB;
        this.supportedFormats = supportedFormats;
    }

    public int getEmbeddingDimension() {
        return embeddingDimension;
    }

    public int getMaxSizeMB() {
        return maxSizeMB;
    }

    public Set<String> getSupportedFormats() {
        return supportedFormats;
    }

    public boolean isValidEmbeddingSize(int size) {
        return size == embeddingDimension;
    }

    public static ContentModality fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return UNKNOWN;
        }

        try {
            return ContentModality.valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    public static ContentModality fromMimeType(String mimeType) {
        if (mimeType == null) {
            return UNKNOWN;
        }

        String lowerMimeType = mimeType.toLowerCase();
        if (lowerMimeType.startsWith("text/")) {
            return TEXT;
        } else if (lowerMimeType.startsWith("image/")) {
            return IMAGE;
        } else if (lowerMimeType.startsWith("audio/")) {
            return AUDIO;
        } else if (lowerMimeType.startsWith("video/")) {
            return VIDEO;
        }

        return UNKNOWN;
    }
}
