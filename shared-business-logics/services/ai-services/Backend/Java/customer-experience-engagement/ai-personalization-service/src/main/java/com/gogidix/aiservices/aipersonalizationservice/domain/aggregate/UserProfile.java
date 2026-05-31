package com.gogidix.aiservices.aipersonalizationservice.domain.aggregate;

import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserProfile {
    private static final int MAX_BEHAVIOR_HISTORY = 1000;
    private static final int MIN_INTERACTIONS_FOR_RECOMMENDATIONS = 5;
    private static final int INTERACTIONS_FOR_ACTIVE_SEGMENT = 10;
    private static final int INTERACTIONS_FOR_VIP_SEGMENT = 100;
    private static final int PURCHASES_FOR_VIP_SEGMENT = 10;
    private static final Duration INACTIVE_THRESHOLD = Duration.ofDays(30);
    private static final Duration CHURNED_THRESHOLD = Duration.ofDays(90);

    private final UUID profileId;
    private final UUID userId;
    private Segment segment;
    private UserAttributes attributes;
    private final Map<String, Double> affinityScores;
    private final List<BehaviorEvent> behaviorHistory;
    private final Instant createdAt;
    private Instant updatedAt;
    private int modificationCount;

    private UserProfile(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        try {
            UUID.fromString(userId.toString()); // Validate UUID format
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UUID format", e);
        }
        this.profileId = UUID.randomUUID();
        this.userId = userId;
        this.segment = Segment.NEW_USER;
        this.attributes = null;
        this.affinityScores = new ConcurrentHashMap<>();
        this.behaviorHistory = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.modificationCount = 0;
    }

    public static UserProfile create(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        try {
            UUID uuid = UUID.fromString(userId);
            return new UserProfile(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format", e);
        }
    }

    public static UserProfile create(UUID userId) {
        return new UserProfile(userId);
    }

    public UUID getProfileId() {
        return profileId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Segment getSegment() {
        return segment;
    }

    public UserAttributes getAttributes() {
        return attributes;
    }

    public Map<String, Double> getAffinityScores() {
        return Collections.unmodifiableMap(affinityScores);
    }

    public List<BehaviorEvent> getBehaviorHistory() {
        return Collections.unmodifiableList(behaviorHistory);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public int getModificationCount() {
        return modificationCount;
    }

    public void updateAttributes(UserAttributes attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        if (attributes.getDemographics() != null &&
                attributes.getDemographics().getAge() != null) {
            int age = attributes.getDemographics().getAge();
            if (age < 0 || age > 120) {
                throw new IllegalArgumentException("Age must be between 0 and 120");
            }
        }
        this.attributes = attributes;
        touch();
    }

    public void updateInterests(List<String> interests) {
        List<String> uniqueInterests = interests.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (this.attributes == null) {
            this.attributes = UserAttributes.builder()
                    .interests(uniqueInterests)
                    .build();
        } else {
            this.attributes = this.attributes.withInterests(uniqueInterests);
        }
        touch();
    }

    public void addBehaviorEvent(BehaviorEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Behavior event cannot be null");
        }

        // Check for duplicates
        boolean isDuplicate = behaviorHistory.stream()
                .anyMatch(e -> e.equals(event));
        if (isDuplicate) {
            return;
        }

        // Insert in chronological order
        int insertIndex = 0;
        for (int i = behaviorHistory.size() - 1; i >= 0; i--) {
            if (behaviorHistory.get(i).getTimestamp().isBefore(event.getTimestamp())) {
                insertIndex = i + 1;
                break;
            }
        }
        behaviorHistory.add(insertIndex, event);

        // Limit history size
        while (behaviorHistory.size() > MAX_BEHAVIOR_HISTORY) {
            behaviorHistory.remove(0);
        }

        touch();
    }

    public void recalculateSegment() {
        int interactionCount = getInteractionCount();
        long purchaseCount = behaviorHistory.stream()
                .filter(e -> e.getEventType() == EventType.PURCHASE)
                .count();

        // Check for churned/inactive
        Instant latestActivity = behaviorHistory.isEmpty()
                ? createdAt
                : behaviorHistory.get(behaviorHistory.size() - 1).getTimestamp();
        Duration timeSinceLastActivity = Duration.between(latestActivity, Instant.now());

        if (timeSinceLastActivity.compareTo(CHURNED_THRESHOLD) > 0 && interactionCount >= 10) {
            this.segment = Segment.CHURNED;
        } else if (timeSinceLastActivity.compareTo(INACTIVE_THRESHOLD) > 0 && interactionCount >= 5) {
            this.segment = Segment.INACTIVE;
        } else if (interactionCount >= INTERACTIONS_FOR_VIP_SEGMENT && purchaseCount >= PURCHASES_FOR_VIP_SEGMENT) {
            this.segment = Segment.VIP;
        } else if (interactionCount >= INTERACTIONS_FOR_ACTIVE_SEGMENT) {
            this.segment = Segment.ACTIVE;
        } else {
            this.segment = Segment.NEW_USER;
        }

        touch();
    }

    public void recalculateAffinityScores() {
        affinityScores.clear();

        // Event type weights
        Map<EventType, Double> weights = new EnumMap<>(EventType.class);
        weights.put(EventType.PURCHASE, 5.0);
        weights.put(EventType.SHARE, 2.0);
        weights.put(EventType.LIKE, 1.5);
        weights.put(EventType.CLICK, 1.0);
        weights.put(EventType.VIEW, 0.5);
        weights.put(EventType.SEARCH, 1.0);

        Map<String, Double> rawScores = new HashMap<>();

        for (BehaviorEvent event : behaviorHistory) {
            double weight = weights.getOrDefault(event.getEventType(), 1.0);

            // Apply time decay
            double age = Duration.between(event.getTimestamp(), Instant.now()).toDays();
            double decay = Math.exp(-age / 30.0); // 30-day half-life
            double adjustedWeight = weight * decay;

            // Extract category from properties
            String category = event.getProperty("category") != null
                    ? event.getProperty("category").toString()
                    : event.getItemId();

            rawScores.merge(category, adjustedWeight, Double::sum);

            // Also track item affinity
            affinityScores.merge(event.getItemId(), adjustedWeight, Double::sum);
        }

        // Normalize scores
        double maxScore = rawScores.values().stream().max(Double::compare).orElse(1.0);
        if (maxScore > 0) {
            rawScores.forEach((key, value) -> {
                double normalized = Math.min(1.0, value / maxScore);
                affinityScores.put(key, normalized);
            });
        }

        touch();
    }

    public int getInteractionCount() {
        return behaviorHistory.size();
    }

    public long getPurchaseCount() {
        return behaviorHistory.stream()
                .filter(e -> e.getEventType() == EventType.PURCHASE)
                .count();
    }

    public boolean isEligibleForRecommendations() {
        return getInteractionCount() >= MIN_INTERACTIONS_FOR_RECOMMENDATIONS;
    }

    public boolean hasMinimumAffinity() {
        return !affinityScores.isEmpty() &&
                affinityScores.values().stream().anyMatch(score -> score >= 0.3);
    }

    private void touch() {
        this.updatedAt = Instant.now();
        this.modificationCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(profileId, that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }
}
