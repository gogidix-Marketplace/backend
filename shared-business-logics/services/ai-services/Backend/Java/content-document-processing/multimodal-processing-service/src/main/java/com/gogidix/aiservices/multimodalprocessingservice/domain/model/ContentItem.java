package com.gogidix.aiservices.multimodalprocessingservice.domain.model;

import java.util.Map;

public record ContentItem(
        ContentModality modality,
        String url,
        Map<String, Object> metadata
) {
    public ContentItem {
        if (modality == null) {
            throw new IllegalArgumentException("Modality cannot be null");
        }
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        if (metadata == null) {
            metadata = Map.of();
        }
    }

    public String getFormat() {
        String lowerUrl = url.toLowerCase();
        int lastDot = lowerUrl.lastIndexOf('.');
        if (lastDot > 0) {
            return lowerUrl.substring(lastDot + 1);
        }
        return "";
    }

    public boolean isFormatSupported() {
        return modality.getSupportedFormats().contains(getFormat());
    }
}
