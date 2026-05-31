package com.gogidix.aiservices.multimodalprocessingservice.domain.policy;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentItem;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentModality;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.OutputFormat;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class MultimodalProcessingPolicy {
    private static final int MAX_CONTENT_ITEMS = 10;
    private static final int EMBEDDING_DIMENSION = 768;
    private static final double SIMILARITY_THRESHOLD = 0.7;
    private static final Duration MAX_VIDEO_LENGTH = Duration.ofMinutes(10);
    private static final Set<String> FUSION_STRATEGIES = Set.of("concat", "average", "attention");

    public boolean isValidItemCount(List<ContentItem> items) {
        return items != null && !items.isEmpty() && items.size() <= MAX_CONTENT_ITEMS;
    }

    public int getMaxContentItems() {
        return MAX_CONTENT_ITEMS;
    }

    public boolean isValidUrl(String urlString) {
        if (urlString == null || urlString.trim().isEmpty()) {
            return false;
        }
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            return Set.of("http", "https", "s3", "gs").contains(protocol);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public boolean isValidVideoLength(Duration duration) {
        return duration != null && !duration.isNegative() && duration.compareTo(MAX_VIDEO_LENGTH) <= 0;
    }

    public Duration getMaxVideoLength() {
        return MAX_VIDEO_LENGTH;
    }

    public int getEmbeddingDimension() {
        return EMBEDDING_DIMENSION;
    }

    public boolean isValidEmbeddingDimension(int dimension) {
        return dimension == EMBEDDING_DIMENSION;
    }

    public double getSimilarityThreshold() {
        return SIMILARITY_THRESHOLD;
    }

    public boolean isValidOutputFormat(OutputFormat format) {
        return format != null;
    }

    public OutputFormat parseOutputFormat(String format) {
        return OutputFormat.fromString(format);
    }

    public Set<String> getFusionStrategies() {
        return FUSION_STRATEGIES;
    }

    public String getDefaultFusionStrategy() {
        return "average";
    }

    public boolean isValidFusionStrategy(String strategy) {
        return FUSION_STRATEGIES.contains(strategy);
    }

    public long getMaxSizeForModality(ContentModality modality) {
        return (long) modality.getMaxSizeMB() * 1024 * 1024;
    }

    public boolean isValidContentSize(ContentModality modality, long sizeBytes) {
        return sizeBytes > 0 && sizeBytes <= getMaxSizeForModality(modality);
    }
}
