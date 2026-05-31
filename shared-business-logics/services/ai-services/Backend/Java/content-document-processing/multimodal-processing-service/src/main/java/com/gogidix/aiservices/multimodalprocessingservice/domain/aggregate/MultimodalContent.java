package com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MultimodalContent {
    private static final int MAX_CONTENT_ITEMS = 10;
    private static final int EMBEDDING_DIMENSION = 768;

    private final UUID contentId;
    private final String userId;
    private final List<ContentItem> items;
    private final Map<ContentModality, float[]> embeddings;
    private float[] fusedEmbedding;
    private String summary;
    private List<String> tags;
    private final Instant createdAt;
    private Instant updatedAt;

    private MultimodalContent(List<ContentItem> items, String userId) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Content items cannot be null or empty");
        }
        if (items.size() > MAX_CONTENT_ITEMS) {
            throw new IllegalArgumentException("Cannot process more than " + MAX_CONTENT_ITEMS + " content items");
        }

        this.contentId = UUID.randomUUID();
        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.embeddings = new ConcurrentHashMap<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static MultimodalContent create(List<ContentItem> items, String userId) {
        return new MultimodalContent(items, userId);
    }

    public void addEmbedding(ContentModality modality, float[] embedding) {
        if (embedding.length != EMBEDDING_DIMENSION) {
            throw new IllegalArgumentException("Embedding dimension must be " + EMBEDDING_DIMENSION);
        }
        embeddings.put(modality, embedding);
        touch();
    }

    public void generateFusedEmbedding() {
        if (embeddings.isEmpty()) {
            throw new IllegalStateException("No embeddings to fuse");
        }

        // Average fusion strategy
        fusedEmbedding = new float[EMBEDDING_DIMENSION];
        for (float[] embedding : embeddings.values()) {
            for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
                fusedEmbedding[i] += embedding[i];
            }
        }

        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            fusedEmbedding[i] /= embeddings.size();
        }

        touch();
    }

    public float[] getEmbedding(ContentModality modality) {
        return embeddings.get(modality);
    }

    public float[] getFusedEmbedding() {
        return fusedEmbedding;
    }

    public double calculateSimilarity(float[] embedding1, float[] embedding2) {
        if (embedding1.length != embedding2.length) {
            throw new IllegalArgumentException("Embeddings must have the same length");
        }

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += embedding1[i] * embedding1[i];
            norm2 += embedding2[i] * embedding2[i];
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public List<MultimodalContent> findSimilar(List<MultimodalContent> candidates, double threshold) {
        if (fusedEmbedding == null) {
            return List.of();
        }

        return candidates.stream()
                .filter(c -> c.getFusedEmbedding() != null)
                .filter(c -> calculateSimilarity(fusedEmbedding, c.getFusedEmbedding()) >= threshold)
                .collect(Collectors.toList());
    }

    public void setSummary(String summary) {
        this.summary = summary;
        touch();
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        touch();
    }

    public Set<ContentModality> getModalities() {
        return items.stream()
                .map(ContentItem::modality)
                .collect(Collectors.toSet());
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // Getters
    public UUID getContentId() { return contentId; }
    public String getUserId() { return userId; }
    public List<ContentItem> getItems() { return items; }
    public Map<ContentModality, float[]> getEmbeddings() { return embeddings; }
    public String getSummary() { return summary; }
    public List<String> getTags() { return tags != null ? tags : List.of(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultimodalContent that = (MultimodalContent) o;
        return Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId);
    }
}
